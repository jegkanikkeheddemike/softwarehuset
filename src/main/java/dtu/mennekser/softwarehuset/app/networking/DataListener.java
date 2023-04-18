package dtu.mennekser.softwarehuset.app.networking;

import dtu.mennekser.softwarehuset.backend.schema.Database;
import dtu.mennekser.softwarehuset.backend.streamDB.data.Query;
import dtu.mennekser.softwarehuset.backend.streamDB.client.ClientListener;
import javafx.application.Platform;

import java.io.Serializable;
import java.util.function.Consumer;

/**
 * Denne klasse er en wrapper som gør det lette at arbejde med streamDB, men den er projektspecifik.
 * Da den siger at datalayer schemaet er en backend.schema.DataBase;
 * @author Thor
 */
public class DataListener<T extends Serializable> extends ClientListener<Database,T> {

    final int garbageID;
    boolean garbageTagged = false;

    public DataListener(Query<Database,T> query, Consumer<T> callback) {
        super(
                query,
                result -> Platform.runLater(() -> {
                    callback.accept(result);
                }),
                Throwable::printStackTrace
        );
        this.garbageID = GarbageCollector.addSubscriber(this);

    }
}
