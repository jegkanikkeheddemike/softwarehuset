package dtu.mennekser.softwarehuset.app.networking;

import dtu.mennekser.softwarehuset.backend.schema.Database;
import dtu.mennekser.softwarehuset.backend.streamDB.data.Query;
import dtu.mennekser.softwarehuset.backend.streamDB.client.ClientQuery;

import java.io.Serializable;

/**
 *Denne klasse er en wrapper som gør det lette at arbejde med streamDB, men den er projektspecifik.
 *  * Da den siger at datalayer schemaet er en backend.schema.DataBase;
 *
 * @author Thor
 */
public class OnceQuery<T extends Serializable> extends ClientQuery<Database,T> {
    public OnceQuery(Query<Database,T> query) {
        super(query, Throwable::printStackTrace);
    }
}
