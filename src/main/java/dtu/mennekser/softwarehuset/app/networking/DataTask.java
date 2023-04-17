package dtu.mennekser.softwarehuset.app.networking;

import dtu.mennekser.softwarehuset.backend.javadb.client.ClientTask;

/**
 * @author Thor
 */
public class DataTask extends ClientTask {
    public DataTask(dtu.mennekser.softwarehuset.backend.data.DataTask task) {
        super(task, Throwable::printStackTrace);
    }

    public static void SubmitTask(dtu.mennekser.softwarehuset.backend.data.DataTask task) {
        new DataTask(task);
    }
}
