package dtu.mennekser.softwarehuset.backend.streamDB;

import dtu.mennekser.softwarehuset.backend.streamDB.data.Query;
import dtu.mennekser.softwarehuset.backend.streamDB.data.Task;
import dtu.mennekser.softwarehuset.backend.streamDB.data.ServerListener;
import dtu.mennekser.softwarehuset.backend.schema.Log;
import dtu.mennekser.softwarehuset.backend.streamDB.networking.ConnInterface;
import dtu.mennekser.softwarehuset.backend.streamDB.networking.ConnType;

import java.io.IOException;
import java.io.InvalidClassException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Thor
 */
public class SocketLayer<Schema extends DataLayer> {
    ExecutionLayer<Schema> executionLayer;

    public SocketLayer(boolean useDisk, Schema fallback) {
        if (useDisk) {
            //First try to load the database
            String filepath = ExecutionLayer.getTablesSavePath();

            try {
                executionLayer = new ExecutionLayer<>(filepath);
                executionLayer.pubSubmitLog(Log.LogLevel.INFO,"Loaded database from file");
            } catch (Exception e) {
                executionLayer = new ExecutionLayer<>(true, fallback);
                if (e instanceof ClassNotFoundException || e instanceof InvalidClassException) {
                    executionLayer.pubSubmitLog(Log.LogLevel.FATAL,"Loaded database is incompatible with the new one. Wiping all data and creating new.");
                } else if (e instanceof IOException) {
                    executionLayer.pubSubmitLog(Log.LogLevel.INFO,"Existing database does not exist. Creating new");
                }
            }
        } else {
            executionLayer = new ExecutionLayer<>(false,fallback);
        }

        //A thread to keep the database up to date
        new Thread(() -> {
            while (running) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                executionLayer.submitTask(database1 -> {});
            }

        }).start();
    }

    private boolean running;
    public synchronized void start() {
        if (running) {
            throw new IllegalStateException("Already running");
        }
        running = true;
        executionLayer.start();
        new Thread(this::run).start();

    }

    void run() {
        ServerSocket socket;
        try {
            socket = new ServerSocket(7009);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while (running) {
            try {
                Socket client = socket.accept();
                client.setKeepAlive(true);
                ConnType connType = ConnInterface.receive(client);
                if (connType == ConnType.Task) {
                    Task task = ConnInterface.receive(client);
                    executionLayer.submitTask(task);
                }  else {
                    Query<Schema,?> query = ConnInterface.receive(client);
                    ServerListener<Schema,?> subscriber = new ServerListener<>(query,client);
                    executionLayer.submitSubscriber(subscriber);
                    System.out.println("Recieved subscriber");
                }
            } catch (Exception e) {

                StringWriter writer = new StringWriter();
                PrintWriter printWriter = new PrintWriter(writer);
                e.printStackTrace(printWriter);

                executionLayer.pubSubmitLog(Log.LogLevel.ERROR,writer.toString());
            }
        }
        executionLayer.pubSubmitLog(Log.LogLevel.FATAL, "Server has crashed. Exiting");
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.exit(1);
    }
}
