package dtu.mennekser.softwarehuset.backend.schema;

import java.io.Serializable;

/**
 * @Author Thor
 */
public class TimeRegistration implements Serializable {
    public int timeRegistrationID;
    public int usedTime;
    public final int employeeID;

    public TimeRegistration(int employeeID, int usedTime, int timeRegistrationID) {
        this.usedTime = usedTime;
        this.employeeID = employeeID;
        this.timeRegistrationID = timeRegistrationID;
    }

    void setTime(int minutes) {
        this.usedTime = minutes;
    }
}
