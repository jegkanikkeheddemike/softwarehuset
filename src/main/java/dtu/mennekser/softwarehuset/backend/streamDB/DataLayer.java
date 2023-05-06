package dtu.mennekser.softwarehuset.backend.streamDB;

import java.io.Serializable;
import java.util.ArrayList;

public class DataLayer implements Serializable {
    public int activeConnections = 0;
    public ArrayList<Log> logs = new ArrayList<>();
    public boolean shutdown;
}
