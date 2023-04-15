package dtu.mennekser.softwarehuset.backend.db;

import java.io.Serializable;
import java.util.ArrayList;

public class Project implements Serializable {

    public final int id;
    String name;
    public ArrayList<Activity> activities = new ArrayList<>();
    public ArrayList<Integer> assignedEmployees = new ArrayList<>();

    public Project(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public int getUsedTime() {
        int timeSum = 0;
        for (Activity activity : activities) {
            timeSum += activity.getUsedTime();
        }

        return timeSum;
    }
    public void createActivity(String name, int budgetedTime) {
        activities.add(new Activity(name, budgetedTime));
    }

    public void assignEmployee(int employeeID) {
        if (!assignedEmployees.contains(employeeID)) {
            assignedEmployees.add(employeeID);
        }
    }

}
