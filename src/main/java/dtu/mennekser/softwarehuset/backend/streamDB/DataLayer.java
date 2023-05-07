package dtu.mennekser.softwarehuset.backend.streamDB;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * @Author Thor
 */
public class DataLayer implements Serializable {
    public int activeConnections = 0;
    public ArrayList<Log> logs = new ArrayList<>();
    public boolean shutdown;
}
