package dtu.mennekser.softwarehusetas.backend.javadb.client.tests;

import dtu.mennekser.softwarehusetas.backend.javadb.client.ClientTask;

public class ShutdownTest {
    public static void main(String[] args) {
        ClientTask.SubmitTask("koebstoffer.info", tables -> {
            System.exit(0);
        }, Throwable::printStackTrace);
    }
}
