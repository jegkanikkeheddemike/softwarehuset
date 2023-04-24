package dtu.mennekser.softwarehuset.backend.schema;

import java.io.Serializable;
import java.util.ArrayList;

public class Project implements Serializable {

    public final int id;
    public String name;
    public String client;
    public ArrayList<Activity> activities = new ArrayList<>();
    public ArrayList<Integer> assignedEmployees = new ArrayList<>();

    public int projectLeaderId = -1;

    public int startWeek;

    public Project(String name,String client, int id,int startWeek) {
        this.client = client;
        this.name = name;
        this.id = id;
        this.startWeek = startWeek;
    }


    public int getUsedTime() {
        int timeSum = 0;
        for (Activity activity : activities) {
            timeSum += activity.getUsedTime();
        }

        return timeSum;
    }
    public int createActivity(String name, int budgetedTime) {
        return createActivity(name,budgetedTime,1,52);
    }

    public int createActivity(String name, int budgetedTime, int startWeek, int endWeek) {
        activities.add(new Activity(name, budgetedTime,startWeek,endWeek,activities.size()));
        return activities.size()-1;
    }

    public void updateActivityWeekBounds(int activityId, int newStartWeek, int newEndWeek) {
        Activity activity = findActivity(activityId);
        if(activity == null) {
            return;
        }
        activity.setStartWeek(newStartWeek);
        activity.setEndWeek(newEndWeek);
    }

    public Activity findActivity(int activityId) {
        for(Activity activity : activities) {
            if(activity.id == activityId) {
                return activity;
            }
        }
        return null;
    }

    public void assignEmployee(int employeeID) {
        if (!assignedEmployees.contains(employeeID)) {
            assignedEmployees.add(employeeID);
        } else {
            throw new RuntimeException("Employee already assigned to project");
        }
    }
    public void setClient(String name){
        client = name;
    }
    public void setProjectLeader(int employeeID) {
        if (projectLeaderId != -1) {
            throw new RuntimeException("Project leader already exists");
        }

        projectLeaderId = employeeID;
    }
    public boolean isProjectLeader(int employeeID) {
        return projectLeaderId == employeeID;
    }

    public void setStartWeek(int startWeek){
        this.startWeek = startWeek;
    }

    public int timeUsedActivity(int activityID, int employeeID) {
        if (employeeID == projectLeaderId) {
            return activities.get(activityID).getUsedTime();
        } else {
            throw new RuntimeException("Employee not project leader");
        }
    }
}
