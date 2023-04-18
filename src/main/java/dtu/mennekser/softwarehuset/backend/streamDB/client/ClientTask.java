package dtu.mennekser.softwarehuset.backend.streamDB.client;

import dtu.mennekser.softwarehuset.backend.streamDB.DataLayer;
import dtu.mennekser.softwarehuset.backend.streamDB.data.Task;
import dtu.mennekser.softwarehuset.backend.streamDB.networking.ConnInterface;
import dtu.mennekser.softwarehuset.backend.streamDB.networking.ConnType;

import java.io.IOException;
import java.net.Socket;
import java.util.function.Consumer;

/**
 * Denne klasse er clientens måde at opdatere state i DataLayer. Den forbinder og sender en lambdafunktion
 * til serveren. Bemærk at det er meget vigt at ALLE OPDATERINGER AF STATE I DATALAYER SKAL SKE GENNEM DENNE
 * KLASSE. Hvis der bliver opdataret state uden for denne klasse vil ExecutionLayer ikke opdage at state er
 * blevet opdateret, og den vil derfor ikke sende den nye state til de relevante Listeners.
 *
 * @author Thor
 */
public class ClientTask<Schema extends DataLayer> {
    private final int port = 7009;

    private final Task<Schema> task;
    private final Consumer<IOException> onError;

    public ClientTask(Task<Schema> task, Consumer<IOException> onError) {
        this.task =task;
        this.onError = onError;
        new Thread(this::run).start();
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
