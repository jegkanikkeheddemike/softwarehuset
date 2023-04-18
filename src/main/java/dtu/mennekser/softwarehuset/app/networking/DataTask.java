package dtu.mennekser.softwarehuset.app.networking;

import dtu.mennekser.softwarehuset.backend.schema.AppBackend;
import dtu.mennekser.softwarehuset.backend.streamDB.data.Task;
import dtu.mennekser.softwarehuset.backend.streamDB.client.ClientTask;

/**
 * Denne klasse er en wrapper som gør det lette at arbejde med streamDB, men den er projektspecifik.
 *  * Da den siger at datalayer schemaet er en backend.schema.DataBase;
 * @author Thor
 */
public class DataTask extends ClientTask<AppBackend> {
    public DataTask(Task<AppBackend> task) {
        super(task, Throwable::printStackTrace);
    }

    public static void SubmitTask(Task<AppBackend> task) {

        new DataTask(task);
    }
}
