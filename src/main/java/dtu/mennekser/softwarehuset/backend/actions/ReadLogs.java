package dtu.mennekser.softwarehuset.backend.actions;

import dtu.mennekser.softwarehuset.backend.db.Log;
import dtu.mennekser.softwarehuset.backend.javadb.client.ClientSubscriber;

import java.util.ArrayList;
import java.util.Arrays;

public class ReadLogs {
    public static void main(String[] args) {
        ClientSubscriber<ArrayList<Log>> logSubscriber = new ClientSubscriber<>(
            "koebstoffer.info",
            database -> database.logs,
            System.out::println,
            Throwable::printStackTrace
        );
    }
}
