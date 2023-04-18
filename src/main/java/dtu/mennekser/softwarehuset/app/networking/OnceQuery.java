package dtu.mennekser.softwarehuset.app.networking;

import dtu.mennekser.softwarehuset.backend.schema.AppBackend;
import dtu.mennekser.softwarehuset.backend.streamDB.data.Query;
import dtu.mennekser.softwarehuset.backend.streamDB.client.ClientQuery;

import java.io.Serializable;

/**
 *Denne klasse er en wrapper som gør det lette at arbejde med streamDB, men den er projektspecifik.
 *  * Da den siger at datalayer schemaet er en backend.schema.DataBase;
 *
 * @author Thor
 */
public class OnceQuery<T extends Serializable> extends ClientQuery<AppBackend,T> {
    public OnceQuery(Query<AppBackend,T> query) {
        super(query, Throwable::printStackTrace);
    }
}
