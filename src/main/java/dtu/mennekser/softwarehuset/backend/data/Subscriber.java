package dtu.mennekser.softwarehuset.backend.data;

import dtu.mennekser.softwarehuset.backend.javadb.networking.ConnInterface;
import dtu.mennekser.softwarehuset.backend.db.Database;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class Subscriber<T extends Serializable> {
    boolean prevDataExists = false;
    int prevHash = 0;
    final DataQuery<T> query;
    final Socket client;

    public Subscriber(DataQuery<T> filter, Socket client) {
        this.query = filter;
        this.client = client;
    }

    /**
     * @return Returns the success status.
     * True is ok status.
     * False means the connection has been severed and the subscriber needs to be killed
     */
    public boolean update(Database tables) {
        T newData = query.apply(tables);
        int newHash = Objects.hash(newData);

        if (prevDataExists) {
            if (newHash==prevHash) {
                return true;
            }
        }

        prevHash = newHash;
        prevDataExists = true;

        try {
            ConnInterface.send(newData, client);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
