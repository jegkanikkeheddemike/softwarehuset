package dtu.mennekser.softwarehuset.backend.actions;

import dtu.mennekser.softwarehuset.backend.javadb.client.ClientTask;

public class ReloadServer {
    public static void main(String[] args) {
        ClientTask task = new ClientTask("koebstoffer.info",database -> {
            System.exit(0);
        }, Throwable::printStackTrace);
    }
}
