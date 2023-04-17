package dtu.mennekser.softwarehuset.backend.javadb.networking;

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
