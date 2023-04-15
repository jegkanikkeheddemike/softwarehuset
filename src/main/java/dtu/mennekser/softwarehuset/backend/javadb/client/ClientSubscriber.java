package dtu.mennekser.softwarehuset.backend.javadb.client;

import dtu.mennekser.softwarehuset.backend.data.DataQuery;
import dtu.mennekser.softwarehuset.backend.javadb.networking.ConnInterface;
import dtu.mennekser.softwarehuset.backend.javadb.networking.ConnType;
import dtu.mennekser.softwarehuset.backend.db.Database;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.net.SocketException;
import java.util.function.Consumer;
import java.util.function.Function;

public class ClientSubscriber<T extends Serializable> {
    public final int port = 7009;

    private final Consumer<T> callback;
    private final Function<Database,T> query;
    private final Consumer<IOException> onError;

    private final Thread thread;
    public ClientSubscriber(DataQuery<T> query, Consumer<T> callback, Consumer<IOException> onError) {
        this.callback = callback;
        this.query = query;
        this.onError = onError;
        thread = new Thread(this::run);
        thread.start();
    }

    public void kill() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Socket socket = null;

    private void run(){
        try {
            socket = new Socket(ClientSettings.remoteLocation, port);
            ConnInterface.send(ConnType.Subscribe,socket);
            ConnInterface.send((Function<Database,T> & Serializable) query,socket);
            while (true) {
                T updatedData = ConnInterface.receive(socket);
                callback.accept(updatedData);
            }
        } catch (SocketException e) {
            if (!e.getMessage().equals("Socket closed")) {
                onError.accept(e);
            }
        } catch (IOException e) {
            onError.accept(e);
        }
    }
}
