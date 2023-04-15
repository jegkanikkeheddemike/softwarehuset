package dtu.mennekser.softwarehuset.backend.db;

import java.io.Serializable;

public final class Employee implements Serializable {
    public String name;
    public Employee(String name) {
        this.name =name;
    }


    @Override
    public String toString() {
        return name;
    }
}
