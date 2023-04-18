package dtu.mennekser.softwarehuset.backend.streamDB;

import dtu.mennekser.softwarehuset.backend.schema.Log;

import java.io.Serializable;
import java.util.ArrayList;

public class DataLayer implements Serializable {
    public int activeConnections = 0;
    public ArrayList<Log> logs = new ArrayList<>();
}
