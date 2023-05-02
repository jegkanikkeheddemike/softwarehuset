package dtu.mennekser.softwarehuset.backend.streamDB;

import dtu.mennekser.softwarehuset.backend.streamDB.data.Task;
import dtu.mennekser.softwarehuset.backend.streamDB.data.ServerListener;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Thor
 */
public class ExecutionLayer<Schema extends DataLayer> {
    private final Schema datalayer;
    final boolean useDisk;

    public ExecutionLayer(boolean useDisk, Schema datalayer) {
        this.useDisk = useDisk;
        this.datalayer = datalayer;
    }

    @SuppressWarnings("unchecked")
    public ExecutionLayer(String filePath) throws IOException, ClassNotFoundException {
        useDisk = true;
        byte[] bin = Files.readAllBytes(Paths.get(filePath));
        ByteArrayInputStream bis = new ByteArrayInputStream(bin);
        ObjectInputStream ois = new ObjectInputStream(bis);
        datalayer = (Schema) ois.readObject();
    }

    final private LinkedBlockingQueue<Task<Schema>> tasks = new LinkedBlockingQueue<>();

    final private LinkedList<ServerListener<Schema,?>> subscribers = new LinkedList<>();

    public void submitTask(Task<Schema> task) {
        try {
            tasks.put(task);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void submitSubscriber(ServerListener<Schema,?> subscriber) {
        synchronized (datalayer) {
            try {
                subscriber.update(datalayer);
                synchronized (subscribers) {
                    subscribers.add(subscriber);
                }
            } catch (ClassCastException e) {
                submitLog(Log.LogLevel.ERROR, "Failed to execute filter. Likely incompatible table versions. "+ e.toString(), true);
            }
        }
    }


    boolean running;

    public synchronized void start() {
        if (running) {
            throw new IllegalStateException("Already running");
        }
        running = true;
        new Thread(this::run).start();
    }

    private void run() {
        while (running) {
            Task<Schema> task;
            try {
                task = tasks.take();
            } catch (InterruptedException e) {
                StringWriter writer = new StringWriter();
                PrintWriter printWriter = new PrintWriter(writer);
                e.printStackTrace(printWriter);
                submitLog(Log.LogLevel.ERROR, writer.toString(),true);
                continue;
            }
            synchronized (datalayer) {
                try {
                    task.accept(datalayer);
                    if (datalayer.shutdown) {
                        running = false;
                    }
                } catch (Exception e) {
                    submitLog(Log.LogLevel.ERROR, "Failed to execute task: " + e, true);
                }

                synchronized (subscribers) {
                    LinkedList<ServerListener<Schema,?>> failed = new LinkedList<>();

                    for (ServerListener<Schema,?> subscriber : subscribers) {
                        try {
                            boolean success = subscriber.update(datalayer);
                            if (!success) {
                                failed.add(subscriber);
                            }
                        } catch (Exception e) {
                            failed.add(subscriber);
                            submitLog(Log.LogLevel.ERROR, "Failed to execute query on nonfirst attempt: " + e, true);
                        }
                    }
                    for (ServerListener<Schema,?> failedSub : failed) {
                        subscribers.remove(failedSub);
                    }
                    datalayer.activeConnections = subscribers.size();
                }
            }
            if (tasks.isEmpty()) {
                //When it is not busy, then block and save the tables to disk
                saveToDisk();
            }
        }
    }

    public static String getSaveDir() {
        return System.getProperty("user.home") + "/javaDB/";
    }
    public static String getTablesSavePath() {
        return getSaveDir() + "tables";
    }
    private void saveToDisk() {
        if (!useDisk) {
            return;
        }

        String filepath = getSaveDir();
        try {
            Files.createDirectories(Path.of(filepath));
        } catch (IOException e) {
            submitLog(Log.LogLevel.ERROR,"Failed to create javaDB directory. " + e, false);
            return;
        }
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(datalayer);
            byte[] tablesBin = bos.toByteArray();

            Files.write(Path.of( getTablesSavePath()), tablesBin);
        } catch (IOException e) {
            submitLog(Log.LogLevel.ERROR, e.toString(), false);
        }
    }
    public void pubSubmitLog(Log.LogLevel logLevel, String message) {
        submitTask(tables1 -> submitLog(logLevel,message,true));
    }

    private void submitLog(Log.LogLevel logLevel, String message, boolean forceUpdate) {
        Log log = new Log(logLevel, message);
        datalayer.logs.add(log);
        System.out.println(log);
        if (forceUpdate) {
            //If tasks is not empty, then just do it.
            if (tasks.isEmpty()) {
                saveToDisk();
            }
        }

    }
}