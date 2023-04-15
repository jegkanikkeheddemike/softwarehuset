package dtu.mennekser.softwarehuset.backend.actions;

import dtu.mennekser.softwarehuset.backend.db.Log;
import dtu.mennekser.softwarehuset.backend.javadb.client.ClientQuery;
import dtu.mennekser.softwarehuset.backend.javadb.client.ClientSubscriber;

import java.util.ArrayList;
import java.util.Arrays;

public class ReadLogs {
    public static void main(String[] args) {
        ArrayList<Log> logs = new ClientQuery<>(database -> database.logs,Throwable::printStackTrace).fetch();
        for (Log log : logs) {
            System.out.println(log);
        }
    }
}
