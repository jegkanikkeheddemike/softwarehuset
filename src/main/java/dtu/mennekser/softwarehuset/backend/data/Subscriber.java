package dtu.mennekser.softwarehuset.backend.data;

import dtu.mennekser.softwarehuset.backend.javadb.networking.ConnInterface;
import dtu.mennekser.softwarehuset.backend.db.Database;
import dtu.mennekser.softwarehuset.backend.javadb.networking.Ping;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Random;

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
        //Ping to check if still alive
        try {
            ConnInterface.send(new Ping(new Random().nextInt()),client);
        } catch (IOException e) {
            return false;
        }


        T newData = query.apply(tables);
        int newHash = customHash(newData);

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
            return false;
        }
        return true;
    }

    static int customHash(Object object) {
        int sum = Objects.hash(object);

        for (Field field : object.getClass().getFields()) {
            try {
                sum += Objects.hash(field.get(object));
            } catch ( Exception e) {
                System.out.println("Illegal hash lol.");
            }
        }

        return sum;
    }
}
