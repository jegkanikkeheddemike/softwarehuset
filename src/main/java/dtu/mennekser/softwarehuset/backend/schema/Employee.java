package dtu.mennekser.softwarehuset.backend.schema;


import java.io.Serializable;
import java.util.ArrayList;

public final class Employee implements Serializable {
    public String name;
    public final int id;
    public ArrayList<Vacation> vacations = new ArrayList<>();

    public Employee(String name, int id) {
        this.name =name; this.id = id;
    }


    @Override
    public String toString() {
        return name;
    }
}
