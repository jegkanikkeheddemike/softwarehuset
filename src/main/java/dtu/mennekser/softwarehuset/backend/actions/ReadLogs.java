package dtu.mennekser.softwarehuset.backend.actions;

import dtu.mennekser.softwarehuset.backend.schema.AppBackend;
import dtu.mennekser.softwarehuset.backend.streamDB.Log;
import dtu.mennekser.softwarehuset.backend.streamDB.client.ClientQuery;

import java.util.ArrayList;

public class ReadLogs {
    public static void main(String[] args) {
        ArrayList<Log> logs = new ClientQuery<AppBackend,ArrayList<Log>>(database -> database.logs,Throwable::printStackTrace).fetch();
        for (Log log : logs) {
            System.out.println(log);
        }
    }
}
