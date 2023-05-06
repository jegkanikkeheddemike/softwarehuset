package dtu.mennekser.softwarehuset.backend.schema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Project implements Serializable {

    public final int id;
    public String name;
    public String client;
    public final String runningNumber;
    public ArrayList<Activity> activities = new ArrayList<>();
    public ArrayList<Integer> assignedEmployees = new ArrayList<>();

    public int projectLeaderId = -1;

    public int startWeek;

    public Project(String name,String client, int id,int startWeek) {
        this.client = client;
        this.name = name;
        this.id = id;
        this.startWeek = startWeek;
        this.runningNumber = new GregorianCalendar().get(Calendar.YEAR) + "" + id;
    }

    public  int getBudgetTime(){
        int timeSum = 0;
        for (Activity activity : activities) {
            timeSum += activity.getBudgetTime();
        }

        return timeSum;
    }

    public int getUsedTime() {
        int timeSum = 0;
        for (Activity activity : activities) {
            timeSum += activity.getUsedTime();
        }

        return timeSum;
    }
    public int remainingTime() {
        int timeSum = 0;
        for (Activity activity : activities) {
            timeSum += (activity.getBudgetTime()-activity.getUsedTime());
        }

        return timeSum;

    }
    int createActivity(String name, int budgetedTime) {
        return createActivity(name,budgetedTime,1,52);
    }

    public int createActivity(String name, int budgetedTime, int startWeek, int endWeek) {
        assert (startWeek >= 1 && startWeek <= 52 && endWeek >=1 && endWeek <= 52);
        activities.add(new Activity(name, budgetedTime,startWeek,endWeek,activities.size()));
        return activities.size()-1;
    }

    void updateActivityWeekBounds(int activityId, int newStartWeek, int newEndWeek) {
        assert (newStartWeek >= 1 && newStartWeek <= 52 && newEndWeek >=1 && newEndWeek <= 52);
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

    void assignEmployee(int employeeID) {
        if (!assignedEmployees.contains(employeeID)) {
            assignedEmployees.add(employeeID);
        } else {
            throw new RuntimeException("Employee already assigned to project");
        }
    }
    void setClient(String name){
        client = name;
    }
    void setProjectLeader(int employeeID) {
        if (projectLeaderId != -1) {
            throw new RuntimeException("Project leader already exists");
        }

        projectLeaderId = employeeID;
    }
    void setStartWeek(int startWeek){
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