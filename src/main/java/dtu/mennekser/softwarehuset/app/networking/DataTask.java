package dtu.mennekser.softwarehuset.app.networking;

import dtu.mennekser.softwarehuset.backend.schema.Database;
import dtu.mennekser.softwarehuset.backend.streamDB.data.Task;
import dtu.mennekser.softwarehuset.backend.streamDB.client.ClientTask;

/**
 * Denne klasse er en wrapper som gør det lette at arbejde med streamDB, men den er projektspecifik.
 *  * Da den siger at datalayer schemaet er en backend.schema.DataBase;
 * @author Thor
 */
public class DataTask extends ClientTask<Database> {
    public DataTask(Task<Database> task) {
        super(task, Throwable::printStackTrace);
    }

    public static void SubmitTask(Task<Database> task) {

        new DataTask(task);
    }
}
