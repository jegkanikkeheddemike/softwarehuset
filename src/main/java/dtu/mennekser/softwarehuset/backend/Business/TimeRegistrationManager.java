package dtu.mennekser.softwarehuset.backend.Business;

import dtu.mennekser.softwarehuset.app.networking.DataTask;
import dtu.mennekser.softwarehuset.backend.data.DataQuery;
import dtu.mennekser.softwarehuset.backend.db.TimeRegistration;

import java.util.ArrayList;

public class TimeRegistrationManager {
    public static void registerTime(String time, int projectID, int activityID) {

        String[] split = time.split(":");
        int hours = Integer.parseInt(split[0]);
        int minutes = Integer.parseInt(split[1]);

        int employeeID = LoginManager.getLoggedInEmployee().id;

        DataTask.SubmitTask(database -> {
            database.projects.get(projectID).activities.get(activityID).registerTime(
                employeeID,
                hours,
                minutes
            );
        });
    }
    public static DataQuery<ArrayList<TimeRegistration>> getTimeRegistrations(int projectID, int activityID) {
        return database -> database.projects.get(projectID).activities.get(activityID).timeRegistrations;
    }
}
