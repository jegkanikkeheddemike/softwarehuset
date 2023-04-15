package dtu.mennekser.softwarehuset.app.networking;

import dtu.mennekser.softwarehuset.backend.data.DataTask;
import dtu.mennekser.softwarehuset.backend.javadb.client.ClientTask;

public class DBTask extends ClientTask {
    public DBTask(DataTask task) {
        super(task, Throwable::printStackTrace);
    }
}
