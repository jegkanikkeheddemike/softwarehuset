package dtu.mennekser.softwarehuset.backend.data;

import dtu.mennekser.softwarehuset.backend.javadb.networking.ConnInterface;
import dtu.mennekser.softwarehuset.backend.db.Database;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;

public class Subscriber<T extends Serializable> {
    boolean prevDataExists = false;
    T prevData = null;
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

        System.out.println("Subscriber with prevData " + prevData + " updating");
        System.out.println(query.getClass().getName());
        T newData = query.apply(tables);
        System.out.println("Query found: " + newData);

        if (prevDataExists) {
            if (newData == null) {
                if (prevData == null) {
                    return true;
                }
            } else if (newData.equals(prevData)) {
                return true;
            }
        }

        //WTFFFFFFF den prevData = newData ændrer bare en pointer så man kan ikke sammenligne demmm!!?!?!
        //Lige nu bliver kun arraylist håndteret men det her er en dårlig løsning.
        if (newData instanceof ArrayList<?>) {
            newData = (T) new ArrayList<>((ArrayList<?>)newData);
        }

        prevData = newData;
        prevDataExists = true;
        try {
            System.out.println("Attempting send");
            ConnInterface.send(newData, client);
            System.out.println("Sending successful");
        } catch (IOException e) {
            System.out.println("SUBSCRIBER FAILED!");
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
