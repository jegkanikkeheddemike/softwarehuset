package dtu.mennekser.softwarehusetas.backend.javadb.client;

import dtu.mennekser.softwarehusetas.backend.data.DataTask;
import dtu.mennekser.softwarehusetas.backend.javadb.networking.ConnInterface;
import dtu.mennekser.softwarehusetas.backend.javadb.networking.ConnType;

import java.io.IOException;
import java.net.Socket;
import java.util.function.Consumer;

public class ClientTask {
    private final String address;
    private final int port = 7009;

    private final DataTask task;
    private final Consumer<IOException> onError;

    private ClientTask(String address, DataTask task, Consumer<IOException> onError) {
        this.address = address;
        this.task =task;
        this.onError = onError;
        new Thread(this::run).start();
    }

    public static void SubmitTask(String address, DataTask task, Consumer<IOException> onError) {
        new ClientTask(address,task,onError);
    }

    void run() {
        try {
            Socket socket = new Socket(address,port);
            ConnInterface.send(ConnType.Task, socket);
            ConnInterface.send(task, socket);
        } catch (IOException e) {
            onError.accept(e);
        }
    }
}
