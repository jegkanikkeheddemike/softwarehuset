package dtu.mennekser.softwarehuset.backend.schema;

import java.io.Serializable;
import java.util.ArrayList;

public class Activity implements Serializable {
    public final int id;
    public String name;
    int budgetedTime;
    public String description ="";
    boolean finished = false;
    public ArrayList<Integer> assignedEmployees = new ArrayList<>();
    public ArrayList<TimeRegistration> timeRegistrations = new ArrayList<>();
    public Activity(String name, int budgetedTime, int id) {
        this.name = name;
        this.budgetedTime = budgetedTime; //TODO DET HER SKAL GØRES ANDERLEDES
        this.id = id;
    }

    public void setBudgetedTime(int budgetedTime) {
        this.budgetedTime = budgetedTime;
    }

    public void assignEmployee(int id) {
        if (!assignedEmployees.contains(id)) {
            assignedEmployees.add(id);
        } else {
            throw new RuntimeException("Employee already assigned to activity");
        }
    }

    public int getUsedTime() {
        int timeSum = 0;
        for (TimeRegistration timeRegistration : timeRegistrations) {
            timeSum += timeRegistration.usedTime;
        }
        return timeSum;
    }
    public void registerTime(int employeeID, int hours, int minutes) {
        //Fancy math that finds out whether half an hour should be registered.
        timeRegistrations.add(new TimeRegistration(employeeID,hours,false));
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
