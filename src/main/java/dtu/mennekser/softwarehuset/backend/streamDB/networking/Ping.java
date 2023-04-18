package dtu.mennekser.softwarehuset.backend.streamDB.networking;

import java.io.Serializable;

/**
 * @author Thor
 */
public class Ping implements Serializable {
    public int value;
    public Ping(int value) {
        this.value = value;
    }

}
