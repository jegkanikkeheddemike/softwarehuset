package dtu.mennekser.softwarehuset.backend.Business;

import dtu.mennekser.softwarehuset.app.networking.DataTask;
import dtu.mennekser.softwarehuset.backend.data.DataQuery;
import dtu.mennekser.softwarehuset.backend.db.Employee;
import dtu.mennekser.softwarehuset.backend.db.Project;

import java.util.ArrayList;

public class ProjectManager {
    public static void createProject(String projectName) {
        int employeeID = LoginManager.getLoggedInEmployee().id;
        DataTask.SubmitTask(
            database -> {
                int projectID = database.createProject(projectName);
                database.projects.get(projectID).assignEmployee(employeeID);
            }
        );
    }
    public static void createActivity(int projectID, String activityName, String time) {
        DataTask.SubmitTask(database -> {
            database.projects.get(projectID).createActivity(activityName,Integer.parseInt(time));
        });
    }
    public static DataQuery<ArrayList<Project>> getProjectsOfEmployee() {
        int employeeID = LoginManager.getLoggedInEmployee().id;
        return database -> {
            ArrayList<Project> assigned = new ArrayList<>();
            for (Project project : database.projects) {
                if (project.assignedEmployees.contains(employeeID)) {
                    assigned.add(project);
                }
            }
            return assigned;
        };
    }
    public static void setProjectLeader(int projectID) {
        int projectLeaderID = LoginManager.getLoggedInEmployee().id;
        DataTask.SubmitTask(database -> {
            database.projects.get(projectID).projectLeaderId = projectLeaderID;
        });
    }
    public static DataQuery<ArrayList<Employee>> getAssignedEmployees(int projectID) {
        return database -> {
            ArrayList<Employee> assigned = new ArrayList<>();
            for (int id : database.projects.get(projectID).assignedEmployees) {
                assigned.add(database.employees.get(id));
            }
            return assigned;
        };
    }
    public static DataQuery<ArrayList<Employee>> getNotAssignedEmployees(int projectID) {
        return database -> {
            ArrayList<Employee> notAssigned = new ArrayList<>();

            for (Employee employee : database.employees) {
                if (!database.projects.get(projectID).assignedEmployees.contains(employee.id)) {
                    notAssigned.add(employee);
                }
            }
            return notAssigned;
        };
    }
    public static void addEmployeeToProject(int projectID, String employeeName) {
        DataTask.SubmitTask(database -> database.projects.get(projectID).assignEmployee(database.findEmployee(employeeName).id));
    }
    public static DataQuery<ArrayList<Employee>> getEmployeesNotAssignedToActivity(int projectID, int activityID) {
        return database -> {
            ArrayList<Employee> notAssigned = new ArrayList<>();

            for (Employee employee : database.employees) {
                if(database.projects.get(projectID).assignedEmployees.contains(employee.id)) {
                    if (!database.projects.get(projectID).activities.get(activityID).assigned.contains(employee.id)) {
                        notAssigned.add(employee);
                    }
                }
            }
            return notAssigned;
        };
    }
    public static void addEmployeeToActivity(int projectID, int activityID, String employeeName) {

        DataTask.SubmitTask(database -> database.projects.get(projectID).activities.get(activityID).assignEmployee(database.findEmployee(employeeName).id));
    }
}
