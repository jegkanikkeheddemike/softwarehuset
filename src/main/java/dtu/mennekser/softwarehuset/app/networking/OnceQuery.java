package dtu.mennekser.softwarehuset.app.networking;

import dtu.mennekser.softwarehuset.backend.javadb.client.ClientQuery;

import java.io.Serializable;

/**
 *
 * @author Thor
 */
public class OnceQuery<T extends Serializable> extends ClientQuery<T> {
    public OnceQuery(dtu.mennekser.softwarehuset.backend.data.DataQuery<T> query) {
        super(query, Throwable::printStackTrace);
    }
}
