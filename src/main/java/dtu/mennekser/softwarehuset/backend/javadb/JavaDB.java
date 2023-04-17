package dtu.mennekser.softwarehuset.backend.javadb;

import dtu.mennekser.softwarehuset.backend.data.DataTask;
import dtu.mennekser.softwarehuset.backend.data.Subscriber;
import dtu.mennekser.softwarehuset.backend.db.Log;
import dtu.mennekser.softwarehuset.backend.db.Database;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Thor
 */
public class JavaDB {
    private final Database database;


    public JavaDB() {
        database = new Database();
    }

    public JavaDB(String filePath) throws IOException, ClassNotFoundException {
        byte[] bin = Files.readAllBytes(Paths.get(filePath));
        ByteArrayInputStream bis = new ByteArrayInputStream(bin);
        ObjectInputStream ois = new ObjectInputStream(bis);
        database = (Database) ois.readObject();
    }

    final private LinkedBlockingQueue<DataTask> tasks = new LinkedBlockingQueue<>();

    final private LinkedList<Subscriber<?>> subscribers = new LinkedList<>();

    public void submitTask(DataTask task) {
        try {
            tasks.put(task);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void submitSubscriber(Subscriber<?> subscriber) {
        synchronized (database) {
            try {
                subscriber.update(database);
                synchronized (subscribers) {
                    subscribers.add(subscriber);
                }
            } catch (ClassCastException e) {
                submitLog(Log.LogLevel.ERROR, "Failed to execute filter. Likely incompatible table versions. "+ e.toString(), true);
            }
        }
    }


    private boolean running;

    public synchronized void start() {
        if (running) {
            throw new IllegalStateException("Already running");
        }
        running = true;
        new Thread(this::run).start();
    }

    private void run() {
        while (running) {
            DataTask task;
            try {
                task = tasks.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
                continue;
            }
            synchronized (database) {
                try {
                    task.accept(database);
                } catch (Exception e) {
                    submitLog(Log.LogLevel.ERROR, "Failed to execute task: " + e, true);
                }

                synchronized (subscribers) {
                    LinkedList<Subscriber<?>> failed = new LinkedList<>();

                    for (Subscriber<?> subscriber : subscribers) {
                        try {
                            boolean success = subscriber.update(database);
                            if (!success) {
                                System.out.println("Success returned false on update attemp: ");
                                failed.add(subscriber);
                            }
                        } catch (Exception e) {
                            failed.add(subscriber);
                            submitLog(Log.LogLevel.ERROR, "Failed to execute query on nonfirst attempt: " + e, true);
                        }

                    }
                    for (Subscriber<?> failedSub : failed) {
                        subscribers.remove(failedSub);
                    }
                    database.activeConnections = subscribers.size();
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
            oos.writeObject(database);
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
        database.logs.add(log);
        System.out.println(log);
        if (forceUpdate) {
            //If tasks is not empty, then just do it.
            if (tasks.isEmpty()) {
                saveToDisk();
            }
        }

    }
}