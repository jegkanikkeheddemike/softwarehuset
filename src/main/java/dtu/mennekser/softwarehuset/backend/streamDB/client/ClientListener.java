package dtu.mennekser.softwarehuset.backend.streamDB.client;

import dtu.mennekser.softwarehuset.backend.streamDB.DataLayer;
import dtu.mennekser.softwarehuset.backend.streamDB.data.Query;
import dtu.mennekser.softwarehuset.backend.streamDB.networking.ConnInterface;
import dtu.mennekser.softwarehuset.backend.streamDB.networking.ConnType;
import dtu.mennekser.softwarehuset.backend.schema.AppBackend;
import dtu.mennekser.softwarehuset.backend.streamDB.networking.Ping;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.net.SocketException;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Den her klasse er Client repræsentationen af en listener.
 * Når den bliver oprettet tager den 3 argumenter:
 * 1. dens query
 * 2. callback til hver gang den får data
 * 3. callback til hvad der skal ske hvis den fejler.
 *
 * Bemærk at den ikke tager en adresse, den bruger i stedet ClientSettings.remoteLocation som adresse
 * Derfor hvis man vil ændre adressen skal man ændre den. Dog vil AppSettings overwrite ClientSettings
 * når der kaldes AppSettings::init()
 *
 * Det er meget vigtigt at DataLayers state IKKE MÅ OPDATERES GENNEM DENNE KLASSE. Hvis DataLayers state ændres
 * i den hers Query, vil ExecutionLayer ikke vide at den skal tjekke for opdateringer i eksisteren Listeners
 * query.
 *
 * @author Thor
 */
public class ClientListener<Schema extends DataLayer,T extends Serializable> {
    public final int port = 7009;

    private final Consumer<T> callback;
    private final Function<Schema,T> query;
    private final Consumer<IOException> onError;

    public ClientListener(Query<Schema,T> query, Consumer<T> callback, Consumer<IOException> onError) {
        this.callback = callback;
        this.query = query;
        this.onError = onError;
        new Thread(this::run).start();
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

    private void run() {
        try {
            socket = new Socket(ClientSettings.remoteLocation, port);
            socket.setKeepAlive(true);
            ConnInterface.send(ConnType.Subscribe, socket);
            ConnInterface.send((Function<AppBackend, T> & Serializable) query, socket);
            while (true) {
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
        } catch (IOException e) {
            onError.accept(e);
        }
    }
}
