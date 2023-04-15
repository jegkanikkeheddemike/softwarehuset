package dtu.mennekser.softwarehuset.backend.db;

import java.io.Serializable;

public final class Employee implements Serializable {
    public String name;
    public final int id;
    public Employee(String name, int id) {
        this.name =name; this.id = id;
    }


    @Override
    public String toString() {
        return name;
    }
}
