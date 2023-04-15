package dtu.mennekser.softwarehuset.backend.javadb.client;

import dtu.mennekser.softwarehuset.backend.data.DataTask;
import dtu.mennekser.softwarehuset.backend.javadb.networking.ConnInterface;
import dtu.mennekser.softwarehuset.backend.javadb.networking.ConnType;

import java.io.IOException;
import java.net.Socket;
import java.util.function.Consumer;

public class ClientTask {
    private final int port = 7009;

    private final DataTask task;
    private final Consumer<IOException> onError;

    public ClientTask(DataTask task, Consumer<IOException> onError) {
        this.task =task;
        this.onError = onError;
        new Thread(this::run).start();
    }

    public static void SubmitTask(DataTask task, Consumer<IOException> onError) {
        new ClientTask(task,onError);
    }

    void run() {
        try {
            Socket socket = new Socket(ClientSettings.remoteLocation,port);
            ConnInterface.send(ConnType.Task, socket);
            ConnInterface.send(task, socket);
        } catch (IOException e) {
            onError.accept(e);
        }
    }
}
