package dtu.mennekser.softwarehuset.backend.schema;

import dtu.mennekser.softwarehuset.backend.streamDB.DataLayer;

import java.util.ArrayList;

public class AppBackend extends DataLayer{

    ArrayList<Employee> employees = new ArrayList<>();
    ArrayList<Project> projects = new ArrayList<>();


    public Session attemptLogin(String username) {
        try {
            Employee found = findEmployee(username);
            return new Session(found);
        } catch (Exception e) {
            return null;
        }
    }

    public int createEmployee(String name) {
        employees.add(new Employee(name, employees.size()));
        return employees.size()-1;
    }
    public Employee findEmployee(String name) {
        for (Employee employee : employees) {
            if (employee.name.equals(name)) {
                return employee;
            }
        }
        throw new RuntimeException("Employee not found");
    }
    public Project findProject(String name) {
        for (Project project : projects) {
            if (project.name.equals(name)) {
                return project;
            }
        }
        return null;
    }

    public ArrayList<Employee> getEmployees() {
        return employees;
    }

    public int createProject(String projectName, Session session) {
        assertLoggedIn(session);
        projects.add(new Project(projectName, projects.size()));

        //automatically assigns the Employee that creates the project
        projects.get(projects.size()-1).assignEmployee(session.employee.id);
        return projects.size()-1;
    }

    public int createActivity(int projectID, String activityName, int budgetedTime ,Session session) {
        assertLoggedIn(session);
        return projects.get(projectID).createActivity(activityName,budgetedTime);
    }

    private void assertLoggedIn(Session session) {
        if (session == null) {
            throw new RuntimeException("Not logged in");
        }
    }

    public ArrayList<Project> getProjectsOfSession(Session session) {
        assertLoggedIn(session);
        ArrayList<Project> assigned = new ArrayList<>();
        for (Project project : projects) {
            if (project.assignedEmployees.contains(session.employee.id)) {
                assigned.add(project);
            }
        }
        return assigned;
    }

    public void setProjectLeader(int projectID, Session session){
        assertLoggedIn(session);
        projects.get(projectID).setProjectLeader(session.employee.id);
    }

    public ArrayList<Employee> getAssignedEmployees(int projectID, Session session) {
        assertLoggedIn(session);
        ArrayList<Employee> assigned = new ArrayList<>();
        for (int id : projects.get(projectID).assignedEmployees) {
            assigned.add(employees.get(id));
        }
        return assigned;
    }
    public ArrayList<Employee> getAssignedActivityEmployees(int projectID, int activityID, Session session) {
        assertLoggedIn(session);
        ArrayList<Employee> assigned = new ArrayList<>();
        for (int id : projects.get(projectID).activities.get(activityID).assignedEmployees) {
            assigned.add(employees.get(id));
        }
        return assigned;
    }


    public ArrayList<Employee> getNotAssignedEmployees(int projectID, Session session) {
        assertLoggedIn(session);
        ArrayList<Employee> notAssigned = new ArrayList<>();

        for (Employee employee : employees) {
            if (!projects.get(projectID).assignedEmployees.contains(employee.id)) {
                notAssigned.add(employee);
            }
        }
        return notAssigned;
    }

    public void addEmployeeToProject(int projectID, String employeeName, Session session) {
        assertLoggedIn(session);
        projects.get(projectID).assignEmployee(findEmployee(employeeName).id);
    }

    public ArrayList<Employee> getEmployeesNotAssignedToActivity(int projectID, int activityID, Session session) {
        assertLoggedIn(session);
        ArrayList<Employee> notAssigned = new ArrayList<>();

        for (Employee employee : employees) {
            if(projects.get(projectID).assignedEmployees.contains(employee.id)) {
                if (!projects.get(projectID).activities.get(activityID).assignedEmployees.contains(employee.id)) {
                    notAssigned.add(employee);
                }
            }
        }
        return notAssigned;
    }
    public void addEmployeeToActivity(int projectID, int activityID, String employeeName, Session session) {
        assertLoggedIn(session);

        Employee foundEmployee = findEmployee(employeeName);

        assertEmployeeInProject(projectID,session.employee.id);

        projects.get(projectID).activities.get(activityID).assignEmployee(foundEmployee.id);

    }

    private void assertEmployeeInProject(int projectID,int employeeID) {
        if (!projects.get(projectID).assignedEmployees.contains(employeeID)) {
            throw new RuntimeException("Employee not in project");
        }
    }
    private void assertEmployeeInActivity(int projectID,int activityID,int employeeID) {
        assertEmployeeInProject(projectID,employeeID);

        if (!projects.get(projectID).activities.get(activityID).assignedEmployees.contains(employeeID)) {
            throw new RuntimeException("Employee not in activity");
        }
    }

    public void registerTime(int projectID, int activityID, String time, Session session) {
        assertLoggedIn(session);


        String[] split = time.split(":");
        int hours = Integer.parseInt(split[0]);
        int minutes = Integer.parseInt(split[1]);

        projects.get(projectID).activities.get(activityID).registerTime(
                session.employee.id,
                hours,
                minutes
        );
    }
    public ArrayList<TimeRegistration> getTimeRegistrationsOfActivity(int projectID, int activityID, Session session) {
        assertLoggedIn(session);
        assertEmployeeInProject(projectID, session.employee.id);

        return projects.get(projectID).activities.get(activityID).timeRegistrations;
    }
    public Employee getProjectLeader(int projectID, Session session) {
        assertLoggedIn(session);
        return employees.get(projects.get(projectID).projectLeaderId);
    }
    public Project getProject(int projectID, Session session) {
        assertLoggedIn(session);
        return projects.get(projectID);
    }
    public Activity getActivity(int projectID, int activityID, Session session) {
        assertLoggedIn(session);
        return projects.get(projectID).activities.get(activityID);
    }
    public void setDescription(int projectID, int activityID, String newDescription, Session session) {
        assertLoggedIn(session);
        getActivity(projectID,activityID,session).setDescription(newDescription);
    }
}