package dtu.mennekser.softwarehuset.backend.actions;

import dtu.mennekser.softwarehuset.backend.schema.AppBackend;
import dtu.mennekser.softwarehuset.backend.streamDB.client.ClientTask;

/**
 * @author Thor
 */
public class ReloadServer {
    public static void main(String[] args) {
        new ClientTask<AppBackend>(database -> {
            System.exit(0);
        }, Throwable::printStackTrace);
    }
}
