package dtu.mennekser.softwarehuset.backend.schema;

import java.io.Serializable;

public class TimeRegistration implements Serializable {
    public int usedTime;
    public final int employeeID;

    public TimeRegistration(int employeeID, int usedTime) {
        this.usedTime = usedTime;
        this.employeeID = employeeID;
    }

    public void setTime(int minutes) {
        this.usedTime = minutes;
    }
}
