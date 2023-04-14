package dtu.mennekser.softwarehuset.backend.db;

import java.io.Serializable;
import java.util.ArrayList;

public class Project implements Serializable {

    String name;
    public ArrayList<Activity> activities = new ArrayList<>();

    public Project(String name) {
        this.name = name;
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

}
