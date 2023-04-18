package dtu.mennekser.softwarehuset.backend.schema;

import java.io.Serializable;

public class TimeRegistration implements Serializable {
    public final int usedTime;
    public final int employeeID;
    public final boolean halfhour;
    public TimeRegistration(int employeeID, int usedTime, boolean halfhour) {
        this.usedTime = usedTime;
        this.employeeID = employeeID;
        this.halfhour = halfhour;
    }
}
