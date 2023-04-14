package dtu.mennekser.softwarehuset.backend.db;

import java.io.Serializable;

public class TimeRegistration implements Serializable {
    final int usedTime;
    final Employee employee;
    public TimeRegistration(Employee employee, int usedTime) {
        this.usedTime = usedTime;
        this.employee = employee;
    }
}
