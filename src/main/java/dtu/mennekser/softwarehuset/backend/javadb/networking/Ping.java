package dtu.mennekser.softwarehuset.backend.javadb.networking;

import java.io.Serializable;

public class Ping implements Serializable {
    public final int value;
    public Ping(int value) {
        this.value = value;
    }

}
