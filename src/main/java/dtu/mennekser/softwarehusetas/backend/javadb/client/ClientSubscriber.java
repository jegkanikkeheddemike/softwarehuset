package dtu.mennekser.softwarehusetas.backend.javadb.client;

import dtu.mennekser.softwarehusetas.backend.data.DataFilter;
import dtu.mennekser.softwarehusetas.backend.javadb.networking.ConnInterface;
import dtu.mennekser.softwarehusetas.backend.javadb.networking.ConnType;
import dtu.mennekser.softwarehusetas.backend.tables.Tables;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.net.SocketException;
import java.util.function.Consumer;
import java.util.function.Function;

public class ClientSubscriber<T extends Serializable> {
    public final String address;
    public final int port = 7009;

    private final Consumer<T> callback;
    private final Function<Tables,T> filter;
    private final Consumer<IOException> onError;

    private final Thread thread;
    public ClientSubscriber(String address, DataFilter<T> filter, Consumer<T> callback, Consumer<IOException> onError) {
        this.address = address;
        this.callback = callback;
        this.filter = filter;
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
            socket = new Socket(address, port);
            ConnInterface.send(ConnType.Subscribe,socket);
            ConnInterface.send((Function<Tables,T> & Serializable) filter,socket);
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
