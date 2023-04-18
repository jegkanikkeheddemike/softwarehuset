package dtu.mennekser.softwarehuset.backend.schema;

import dtu.mennekser.softwarehuset.backend.schema.Employee;

import java.io.Serializable;

public class Session implements Serializable {
    public Employee employee;

    Session(Employee employee) {
        this.employee = employee;
    }

    public Session cloneSession() {
        return new Session(employee);
    }
}
