package dtu.mennekser.softwarehuset.backend.actions;

import dtu.mennekser.softwarehuset.backend.schema.Database;
import dtu.mennekser.softwarehuset.backend.streamDB.client.ClientTask;

/**
 * @author Thor
 */
public class ReloadServer {
    public static void main(String[] args) {
        new ClientTask<Database>(database -> {
            System.exit(0);
        }, Throwable::printStackTrace);
    }
}
