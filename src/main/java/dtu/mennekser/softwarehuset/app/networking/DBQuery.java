package dtu.mennekser.softwarehuset.app.networking;

import dtu.mennekser.softwarehuset.backend.data.DataQuery;
import dtu.mennekser.softwarehuset.backend.javadb.client.ClientQuery;

import java.io.Serializable;

/**
 *
 * @author Thor
 */
public class DBQuery<T extends Serializable> extends ClientQuery<T> {
    public DBQuery(DataQuery<T> query) {
        super(query, Throwable::printStackTrace);
    }
}
