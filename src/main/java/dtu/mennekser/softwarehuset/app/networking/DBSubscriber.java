package dtu.mennekser.softwarehuset.app.networking;

import dtu.mennekser.softwarehuset.backend.data.DataQuery;
import dtu.mennekser.softwarehuset.backend.javadb.client.ClientSubscriber;
import javafx.application.Platform;

import java.io.Serializable;
import java.util.function.Consumer;

/**
 *
 * @author Thor
 */
public class DBSubscriber<T extends Serializable> extends ClientSubscriber<T> {

    final int garbageID;
    boolean garbageTagged = false;

    public DBSubscriber(DataQuery<T> query, Consumer<T> callback) {
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
