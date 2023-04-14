package dtu.mennekser.softwarehuset.backend.data;

import dtu.mennekser.softwarehuset.backend.javadb.networking.ConnInterface;
import dtu.mennekser.softwarehuset.backend.db.Database;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

public class Subscriber<T extends Serializable> {
    T prevData = null;
    final DataQuery<T> filter;
    final Socket client;

    public Subscriber(DataQuery<T> filter, Socket client) {
        this.filter = filter;
        this.client = client;
    }

    /**
     * @return Returns the success status.
     * True is ok status.
     * False means the connection has been severed and the subscriber needs to be killed
     */
    public boolean update(Database tables) {
        T newData = filter.apply(tables);

        if (newData.equals(prevData)) {
            return true;
        }
        prevData = newData;

        try {
            ConnInterface.send(newData, client);
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
