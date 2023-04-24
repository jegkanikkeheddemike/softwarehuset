package dtu.mennekser.softwarehuset.backend.schema;

import dtu.mennekser.softwarehuset.backend.streamDB.DataLayer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class AppBackend extends DataLayer {

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
        return employees.size() - 1;
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


    public int createProject(String projectName,String clientName,Session session,String startWeek) {

        assertLoggedIn(session);
        if (clientName.isEmpty()) {
            clientName = "SoftwareHusetAS";
        }

        if(startWeek.isEmpty()){
            startWeek = "0";
        }

        projects.add(new Project(projectName,clientName, projects.size(), Integer.parseInt(startWeek)));
        //automatically assigns the Employee that creates the project
        projects.get(projects.size() - 1).assignEmployee(session.employee.id);
        return projects.size() - 1;
    }

    public int createActivity(int projectID, String activityName, int budgetedTime, Session session) {
        return createActivity(projectID, activityName, budgetedTime, 1, 52, session);
    }

    public int createActivity(int projectID, String activityName, int budgetedTime, int startWeek, int endWeek, Session session) {
        assertLoggedIn(session);
        return projects.get(projectID).createActivity(activityName, budgetedTime, startWeek, endWeek);
    }

    public void updateActivityWeekBounds(int projectId, int activityId, int newStartWeek, int newEndWeek, Session session) {
        assertLoggedIn(session);
        projects.get(projectId).updateActivityWeekBounds(activityId, newStartWeek, newEndWeek);


    }

    public void createVacation(String startWeek, String endWeek, Session session) {
        assertLoggedIn(session);
        employees.get(session.employee.id).vacations.add(new Vacation(Integer.parseInt(startWeek),Integer.parseInt(endWeek),session.employee.vacations.size()));
    }

    private void assertLoggedIn(Session session) {
        if (session == null) {
            throw new RuntimeException("Employee not logged in");
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

    public void setProjectLeader(int projectID, Session session) {
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
            if (projects.get(projectID).assignedEmployees.contains(employee.id)) {
                if (!projects.get(projectID).activities.get(activityID).assignedEmployees.contains(employee.id)) {
                    notAssigned.add(employee);
                }
            }
        }
        return notAssigned;
    }

    public void addEmployeeToActivity(int projectID, int activityID, String employeeName, Session session) {
        assertLoggedIn(session);
        assertNotVacationing(projects.get(projectID).activities.get(activityID).startWeek,
                projects.get(projectID).activities.get(activityID).endWeek,findEmployee(employeeName).id);

        Employee foundEmployee = findEmployee(employeeName);

        assertEmployeeInProject(projectID, session.employee.id);

        projects.get(projectID).activities.get(activityID).assignEmployee(foundEmployee.id);

    }

    private void assertNotVacationing(int start,int end,int employeeID) {
        if(getVacations(employeeID).isEmpty()){return;}

        for(int vac = 0; vac < getVacations(employeeID).size(); vac++){
            if(getVacations(employeeID).get(vac).startWeek <= start
                    && getVacations(employeeID).get(vac).endWeek >= end){
                throw new RuntimeException("Employee on vacation");
            }
        }
    }

    private void assertEmployeeInProject(int projectID,int employeeID) {
        if (!projects.get(projectID).assignedEmployees.contains(employeeID)) {
            throw new RuntimeException("Employee not in project");
        }
    }

    private void assertEmployeeInActivity(int projectID, int activityID, int employeeID) {
        assertEmployeeInProject(projectID, employeeID);

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
        getActivity(projectID, activityID, session).setDescription(newDescription);
    }

    public void setStartTime(int projectID, Session session, int startWeek) {
        assertLoggedIn(session);
        getProject(projectID, session).setStartWeek(startWeek);
    }

    public int getStartTime(int projectID, Session session) {
        assertLoggedIn(session);
        return projects.get(projectID).startWeek;
    }

    public void finishActivity(int projectID, int activityID, Session session) {
        assertLoggedIn(session);
        getActivity(projectID, activityID, session).finished = true;
    }

    public void setBudgetedTime(int projectID, int activityID, int BudgetedTime, Session session) {
        assertLoggedIn(session);
        getActivity(projectID,activityID,session).setBudgetedTime(BudgetedTime);
    }
    public int TimeRemainingActivity(int projectID, int activityID, Session session) {
        assertLoggedIn(session);
        return getActivity(projectID,activityID,session).timeRemaining();
    }
    public int TimeUsedActivity(int projectID, int activityID, Session session) {
        assertLoggedIn(session);
        return getProject(projectID, session).timeUsedActivity(activityID, session.employee.id);
    }


    public record ActiveActivity(Project project, Activity activity) implements Serializable {
    }

    public ArrayList<Vacation> getVacations(int employeeID) {
        return employees.get(employeeID).vacations;
    }

    public ArrayList<ActiveActivity> getActiveActivities(Session session) {
        //Find alle projekter som employee er en del af
        ArrayList<Project> projects = getProjectsOfSession(session);

        ArrayList<ActiveActivity> activities = new ArrayList<>();
        for (Project project : projects) {
            for (Activity activity : project.activities) {
                if (!activity.finished && activity.assignedEmployees.contains(session.employee.id)) {
                    activities.add(new ActiveActivity(project, activity));
                }
            }
        }
        return activities;
    }

    public record ActivityStat(int projectID, String projectName, Activity activity) implements Serializable {}
    public record EmployeeStat(Employee employee, ArrayList<ActivityStat> assignedActivities) implements Serializable {}
    public record ProjectStat(ArrayList<EmployeeStat> employeeStats, ArrayList<Activity> unassignedActivities) implements Serializable{}
    public ProjectStat getProjectStats(int projectID, Session session) {
        assertLoggedIn(session);
        assertEmployeeInProject(projectID,session.employee.id);

        Project project =projects.get(projectID);
        if (project.projectLeaderId != session.employee.id) {
            throw new RuntimeException("Employee is not project leader");
        }
        HashMap<Integer, ArrayList<ActivityStat>> employeeActivities = new HashMap<>();

        for (Project cProject : projects) {
            for (Activity activity : cProject.activities) {
                for (int employeeID : activity.assignedEmployees) {
                    if (!project.assignedEmployees.contains(employeeID)) {
                        continue;
                    }

                    ArrayList<ActivityStat> assignedActivities = employeeActivities.computeIfAbsent(employeeID, k -> new ArrayList<>());
                    assignedActivities.add(new ActivityStat(cProject.id,cProject.name,activity));
                }
            }
        }
        ArrayList<EmployeeStat> employeeStats = new ArrayList<>();
        employeeActivities.forEach((key,value) -> {
            value.addAll(getVacations(key));
            System.out.println(getVacations(key).size());
            employeeStats.add(new EmployeeStat(
                    employees.get(key),value
            ));
        });

        ArrayList<Activity> unassignedActivities = new ArrayList<>(project.activities.stream().filter(activity -> activity.assignedEmployees.isEmpty()).toList());

        return new ProjectStat(employeeStats,unassignedActivities);
    }
}
