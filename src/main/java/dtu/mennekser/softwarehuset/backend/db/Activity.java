package dtu.mennekser.softwarehuset.backend.db;

import java.io.Serializable;
import java.util.ArrayList;

public class Activity implements Serializable {
    public final int id;
    public String name;
    int budgetedTime;
    public String description ="";
    boolean finished = false;
    public ArrayList<Integer> assigned = new ArrayList<>();
    public ArrayList<TimeRegistration> timeRegistrations = new ArrayList<>();
    public Activity(String name, int budgetedTime, int id) {
        this.name = name;
        this.budgetedTime = budgetedTime;
        this.id = id;
    }

    public void setBudgetedTime(int budgetedTime) {
        this.budgetedTime = budgetedTime;
    }

    public void assignEmployee(int id) {
        if (!assigned.contains(id)) {
            assigned.add(id);
        }
    }

    public int getUsedTime() {
        int timeSum = 0;
        for (TimeRegistration timeRegistration : timeRegistrations) {
            timeSum += timeRegistration.usedTime;
        }
        return timeSum;
    }

    public void registerTime(int employeeID, int hours, int minuts) {
        //Fancy math that finds out whether half an hour should be registered.

        timeRegistrations.add(new TimeRegistration(employeeID,hours,false));
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
