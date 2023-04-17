package dtu.mennekser.softwarehuset.backend.Business;

import dtu.mennekser.softwarehuset.app.networking.DBTask;
import dtu.mennekser.softwarehuset.backend.data.DataTask;

public class ProjectManager {
    public static void createProject(String projectName, int employeeID) {
        DBTask.SubmitTask(
            database -> {
                int projectID = database.createProject(projectName);
                database.projects.get(projectID).assignEmployee(employeeID);
            }
        );
    }
}
