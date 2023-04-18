package dtu.mennekser.softwarehuset.backend.Business;

import dtu.mennekser.softwarehuset.app.networking.DataTask;
import dtu.mennekser.softwarehuset.backend.schema.Database;
import dtu.mennekser.softwarehuset.backend.streamDB.data.Query;
import dtu.mennekser.softwarehuset.backend.schema.TimeRegistration;

import java.util.ArrayList;

public class TimeRegistrationManager {
    public static void registerTime(String time, int projectID, int activityID) {
        assert LoginManager.getLoggedInEmployee() != null;


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
    public static Query<Database,ArrayList<TimeRegistration>> getTimeRegistrations(int projectID, int activityID) {
        return database -> database.projects.get(projectID).activities.get(activityID).timeRegistrations;
    }
}
