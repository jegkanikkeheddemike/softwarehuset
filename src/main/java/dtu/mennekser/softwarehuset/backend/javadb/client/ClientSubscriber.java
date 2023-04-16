package dtu.mennekser.softwarehuset.backend.javadb.client;

import dtu.mennekser.softwarehuset.backend.data.DataQuery;
import dtu.mennekser.softwarehuset.backend.javadb.networking.ConnInterface;
import dtu.mennekser.softwarehuset.backend.javadb.networking.ConnType;
import dtu.mennekser.softwarehuset.backend.db.Database;
import dtu.mennekser.softwarehuset.backend.javadb.networking.Ping;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.net.Socket;
import java.net.SocketException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public class ClientSubscriber<T extends Serializable> {
    public final int port = 7009;

    private final Consumer<T> callback;
    private final Function<Database,T> query;
    private final Consumer<IOException> onError;

    private final Thread thread;

    private boolean killed = false;
    public ClientSubscriber(DataQuery<T> query, Consumer<T> callback, Consumer<IOException> onError) {
        this.callback = callback;
        this.query = query;
        this.onError = onError;
        thread = new Thread(this::run);
        thread.start();
    }

    public void kill() {
        try {
            killed = true;
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Socket socket = null;

    private void run() {
        try {
            socket = new Socket(ClientSettings.remoteLocation, port);
            socket.setKeepAlive(true);
            ConnInterface.send(ConnType.Subscribe, socket);
            ConnInterface.send((Function<Database, T> & Serializable) query, socket);
            while (true) {
                if (killed) {
                    System.out.println("Connection killed");
                    return;
                }
                Object data = ConnInterface.receive(socket);
                if (data instanceof Ping) {
                    continue;
                }

                callback.accept((T) data);
            }
        } catch (SocketException e) {
            if (!e.getMessage().equals("Socket closed")) {
                onError.accept(e);
            }
            System.out.println("Connection closed");
        } catch (IOException e) {
            onError.accept(e);
        }
    }
}
