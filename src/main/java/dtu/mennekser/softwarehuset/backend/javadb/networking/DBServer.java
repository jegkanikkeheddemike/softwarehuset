package dtu.mennekser.softwarehuset.backend.javadb.networking;

import dtu.mennekser.softwarehuset.backend.data.DataQuery;
import dtu.mennekser.softwarehuset.backend.data.DataTask;
import dtu.mennekser.softwarehuset.backend.data.ServerListener;
import dtu.mennekser.softwarehuset.backend.javadb.JavaDB;
import dtu.mennekser.softwarehuset.backend.db.Log;

import java.io.IOException;
import java.io.InvalidClassException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Thor
 */
public class DBServer {
    final JavaDB database;

    public static void main(String[] args) {
        //Server side code
        DBServer server = new DBServer();
        server.start();

        //A thread to keep the database up to date
        new Thread(() -> {
            while (server.running) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                server.database.submitTask(database1 -> {});
            }

        }).start();
    }

    public DBServer() {
        //First try to load the database
        String filepath = JavaDB.getTablesSavePath();
        JavaDB loadedDB;
        try {
            loadedDB = new JavaDB(filepath);
            loadedDB.pubSubmitLog(Log.LogLevel.INFO,"Loaded database from file");
        } catch (Exception e) {
            loadedDB = new JavaDB();
            if (e instanceof ClassNotFoundException || e instanceof InvalidClassException) {
                loadedDB.pubSubmitLog(Log.LogLevel.FATAL,"Loaded database is incompatible with the new one. Wiping all data and creating new.");
            } else if (e instanceof IOException) {
                loadedDB.pubSubmitLog(Log.LogLevel.INFO,"Existing database does not exist. Creating new");
            }
        }
        database = loadedDB;
    }

    private boolean running;
    public synchronized void start() {
        if (running) {
            throw new IllegalStateException("Already running");
        }
        running = true;
        database.start();
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
                    DataTask task = ConnInterface.receive(client);
                    database.submitTask(task);
                }  else {
                    DataQuery<?> query = ConnInterface.receive(client);
                    ServerListener<?> subscriber = new ServerListener<>(query,client);
                    database.submitSubscriber(subscriber);
                    System.out.println("Recieved subscriber");
                }
            } catch (Exception e) {

                StringWriter writer = new StringWriter();
                PrintWriter printWriter = new PrintWriter(writer);
                e.printStackTrace(printWriter);

                database.pubSubmitLog(Log.LogLevel.ERROR,writer.toString());
            }
        }
        database.pubSubmitLog(Log.LogLevel.FATAL, "Server has crashed. Exiting");
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
