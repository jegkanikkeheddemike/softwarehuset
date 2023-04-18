package dtu.mennekser.softwarehuset.backend.actions;

import dtu.mennekser.softwarehuset.backend.schema.Database;
import dtu.mennekser.softwarehuset.backend.schema.Log;
import dtu.mennekser.softwarehuset.backend.streamDB.client.ClientQuery;

import java.util.ArrayList;

public class ReadLogs {
    public static void main(String[] args) {
        ArrayList<Log> logs = new ClientQuery<Database,ArrayList<Log>>(database -> database.logs,Throwable::printStackTrace).fetch();
        for (Log log : logs) {
            System.out.println(log);
        }
    }
}
