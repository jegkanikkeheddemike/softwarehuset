package dtu.mennekser.softwarehuset.backend.schema;

import dtu.mennekser.softwarehuset.backend.streamDB.DataLayer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * Filen er skrevet af flere personer
 * Et navn vil stå hvor alle linjer kode der kommer efter er skrevet af den person
 * indtil et nyt navn står osv.
 */
public class AppBackend extends DataLayer {
    /**
     * @Author Thor
     */
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
    /**
     * @Author Christian
     */
    public int createEmployee(String realName) {
        assert realName != null && realName.length() > 0;

        String[] temp = realName.split(" ");
        String name = "";
        if (temp.length >= 2) {                                     //1
            for (int j = 0; j < 2; j++) {                           //2
                for (int i = 0; i < 2; i++) {                       //3
                    name += temp[j].charAt(i % temp[j].length());
                }
            }
        } else {
            for (int i = 0; i < 4; i++) {                           //4
                name += temp[0].charAt(i % temp[0].length());
            }
        }
        employees.add(new Employee(name.toLowerCase(), realName, employees.size()));

        assert employees.get(employees.size() - 1).name.equals(name.toLowerCase());

        return employees.size() - 1;
    }
    /**
     * @Author Frederik
     */
    public Employee findEmployee(String name) {
        assert name != null;
        for (Employee employee : employees) {               //1
            if (employee.name.equals(name)) {               //2
                assert employee.name.equals(name);
                return employee;                            //3
            }
        }
        assert employees.stream().noneMatch(employee -> employee.name.equals(name));
        throw new RuntimeException("Employee not found");   //4

    }

    /**
     * @Author Tobias
     */
    public Project findProject(String name) {
        assert name != null;
        for (Project project : projects) {
            if (project.name.equals(name)) {
                return project;
            }
        }
        assert projects.stream().noneMatch(project -> project.name.equals(name));
        return null;
    }

    public ArrayList<Employee> getEmployees() {
        return employees;
    }

    public void setProjectClient(int projectID, String clientName, Session session) {
        assertLoggedIn(session);
        getProject(projectID,session).setClient(clientName);
    }
    /**
     * @Author Frederik
     */
    public int createProject(String projectName, String clientName, Session session, String startWeek) {
        assertLoggedIn(session);
        if (clientName.isEmpty()) {
            clientName = "SoftwareHusetAS";
        }

        if (startWeek.isEmpty()) {
            startWeek = "0";
        }
        if (projects.stream().anyMatch(project -> project.name.equals(projectName))) {
            throw new RuntimeException("Projectname is already taken");
        }

        projects.add(new Project(projectName, clientName, projects.size(), Integer.parseInt(startWeek)));
        //automatically assigns the Employee that creates the project
        projects.get(projects.size() - 1).assignEmployee(session.employee.id);
        return projects.size() - 1;
    }
    /**
     * @Author Christian
     */
    public int createActivity(int projectID, String activityName, int budgetedTime, Session session) {
        return createActivity(projectID, activityName, budgetedTime, 1, 52, session);
    }

    public int createActivity(int projectID, String activityName, int budgetedTime, int startWeek, int endWeek, Session session) {
        assertLoggedIn(session);
        return projects.get(projectID).createActivity(activityName, budgetedTime * 60, startWeek, endWeek);
    }


    public void updateActivityWeekBounds(int projectId, int activityId, int newStartWeek, int newEndWeek, Session session) {
        assertLoggedIn(session);
        projects.get(projectId).updateActivityWeekBounds(activityId, newStartWeek, newEndWeek);


    }
    /**
     * @Author Katinka
     */
    public void createVacation(String startWeek, String endWeek, Session session) {
        assertLoggedIn(session);
        employees.get(session.employee.id).vacations.add(new Vacation(Integer.parseInt(startWeek), Integer.parseInt(endWeek), session.employee.vacations.size()));
    }

    public void createSickLeave(String employeeName, String startWeek, String endWeek, Session session) {
        assertLoggedIn(session);
        if (endWeek.isEmpty()) {
            endWeek = startWeek;
        }
        int employeeID = findEmployee(employeeName).id;
        employees.get(employeeID).sickLeave.add(new SickLeave(Integer.parseInt(startWeek), Integer.parseInt(endWeek), employees.get(employeeID).sickLeave.size()));
    }
    /**
     * @Author Thor
     */
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
    /**
     * @Author Tobias
     */
    public TimeRegistration getTimeRegistration(int projectID, int activityID, int registrationID, Session session) {
        return getActivity(projectID,activityID,session).timeRegistrations.get(registrationID);
    }

    public void editTime(int projectID, int activityID, int registrationID, int newTime, Session session) {
        assertLoggedIn(session);
        getTimeRegistration(projectID,activityID,registrationID, session).setTime(newTime);
    }

    public record EmployeeNotAssignedToActivity(boolean occupied, Employee employee) implements Serializable {
    }

    public ArrayList<EmployeeNotAssignedToActivity> getEmployeesNotAssignedToActivity(int projectID, int activityID, Session session) {
        assertLoggedIn(session);
        ArrayList<EmployeeNotAssignedToActivity> notAssigned = new ArrayList<>();

        for (Employee employee : employees) {
            if (projects.get(projectID).assignedEmployees.contains(employee.id)) {
                if (!projects.get(projectID).activities.get(activityID).assignedEmployees.contains(employee.id)) {
                    notAssigned.add(new EmployeeNotAssignedToActivity(checkVacationing(projectID, activityID, employee.name), employee));
                }
            }
        }
        return notAssigned;
    }
    /**
     * @Author Katinka
     */
    public void addEmployeeToActivity(int projectID, int activityID,
                                      String employeeName, Session session) {
        //pre-condition
        assert projects.stream().anyMatch(project -> project.id == projectID);
        assert projects.get(projectID).activities.stream().anyMatch(activity -> activity.id == activityID);
        assert employeeName != null;

        assertLoggedIn(session);                                                                //1
        Employee foundEmployee = findEmployee(employeeName);                                    //2

        assertNotVacationing(
                projects.get(projectID).activities.get(activityID).startWeek,
                projects.get(projectID).activities.get(activityID).endWeek,
                foundEmployee.id);                                                              //3

        assertEmployeeInProject(projectID, session.employee.id);                                //4

        projects.get(projectID).activities.get(activityID).assignEmployee(foundEmployee.id);    //5

        //post-condition
        assert projects.get(projectID).activities.get(activityID).assignedEmployees.contains(foundEmployee.id);
    }
    /**
     * @Author Thor
     */
    private boolean checkVacationing(int projectID, int activityID, String employeeName) {
        try {
            assertNotVacationing(projects.get(projectID).activities.get(activityID).startWeek,
                    projects.get(projectID).activities.get(activityID).endWeek, findEmployee(employeeName).id);
        } catch (Exception e) {
            return true;
        }
        return false;
    }
    /**
     * @Author Katinka
     */
    private void assertNotVacationing(int start, int end, int employeeID) {
        if (getVacations(employeeID).isEmpty()) {
            return;
        }

        for (int vac = 0; vac < getVacations(employeeID).size(); vac++) {
            if (getVacations(employeeID).get(vac).startWeek <= start
                    && getVacations(employeeID).get(vac).endWeek >= end) {
                throw new RuntimeException("Employee on vacation");
            }
        }
    }
    /**
     * @Author Thor
     */
    private void assertEmployeeInProject(int projectID, int employeeID) {
        if (!projects.get(projectID).assignedEmployees.contains(employeeID)) {
            throw new RuntimeException("Employee not in project");
        }
    }
    
    public int registerTime(int projectID, int activityID, String time, Session session) {
        assertLoggedIn(session);

        assertValidTimeString(time);

        String[] split = time.split(":");

        int hours = Integer.parseInt(split[0]);
        int minutes = Integer.parseInt(split[1]);

        projects.get(projectID).activities.get(activityID).registerTime(
                session.employee.id,
                hours,
                minutes
        );
        return projects.get(projectID).activities.get(activityID).timeRegistrations.size()-1;
    }

    public static void assertValidTimeString(String time){
        try {
            String[] split = time.split(":");
            Integer.parseInt(split[0]);
            Integer.parseInt(split[1]);
        } catch (Exception e) {
            throw new RuntimeException("Invalid time string");
        }
    }

    public record RegistrationJoinEmployee(String employeeName,
                                           TimeRegistration timeRegistration) implements Serializable {
    }
    /**
     * @Author Katinka
     */
    public ArrayList<RegistrationJoinEmployee> getTimeRegistrationsOfActivity(int projectID, int activityID, Session session) {
        assertLoggedIn(session);
        assertEmployeeInProject(projectID, session.employee.id);

        ArrayList<RegistrationJoinEmployee> timeRegistrations = new ArrayList<>();

        for (var time : projects.get(projectID).activities.get(activityID).timeRegistrations) {
            timeRegistrations.add(new RegistrationJoinEmployee(employees.get(time.employeeID).name, time));
        }
        return timeRegistrations;
    }
    /**
     * @Author Thor
     */
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
    /**
     * @Author Frederik
     */
    public void setStartTime(int projectID, Session session, int startWeek) {
        assertLoggedIn(session);
        getProject(projectID, session).setStartWeek(startWeek);
    }

    public int getStartTime(int projectID, Session session) {
        assertLoggedIn(session);
        return projects.get(projectID).startWeek;
    }
    /**
     * @Author Thor
     */
    public void finishActivity(int projectID, int activityID, Session session) {
        assertLoggedIn(session);
        getActivity(projectID, activityID, session).finished = true;
    }
    /**
     * @Author Tobias
     */
    public void setBudgetedTime(int projectID, int activityID, int BudgetedTime, Session session) {
        assertLoggedIn(session);
        getActivity(projectID, activityID, session).setBudgetedTime(BudgetedTime);
    }

    public int TimeRemainingActivity(int projectID, int activityID, Session session) {
        assertLoggedIn(session);
        return getActivity(projectID, activityID, session).timeRemaining();
    }

    public int TimeUsedActivity(int projectID, int activityID, Session session) {
        assertLoggedIn(session);
        return getProject(projectID, session).timeUsedActivity(activityID, session.employee.id);
    }

    /**
     * @Author Frederik
     */
    public record ActiveActivity(Project project, Activity activity) implements Serializable {
    }

    public record TimeRegisActivity(String projectName, String activityName,int projectID, int activityID,
                                    TimeRegistration timeRegistration) implements Serializable {
        @Override
        public String toString() {
            return projectName + "/" + activityName + " - " + timeRegistration.usedTime/60 + ":" + timeRegistration.usedTime%60;
        }
    }

    /**
     * @Author Katinka
     */
    public ArrayList<Vacation> getVacations(int employeeID) {
        return employees.get(employeeID).vacations;
    }

    public ArrayList<SickLeave> getSickLeaves(int employeeID) {
        return employees.get(employeeID).sickLeave;
    }
    /**
     * @Author Thor
     */
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
    /**
     * @Author Frederik
     */
    public ArrayList<TimeRegisActivity> getTimeRegisActivity(Session session) {
        //Find alle projekter som employee er en del af

        ArrayList<TimeRegisActivity> timeRegisActivities = new ArrayList<>();
        for (Project project : projects) {
            for (Activity activity : project.activities) {
                for (TimeRegistration time : activity.timeRegistrations) {
                    if (time.employeeID == session.employee.id) {
                        timeRegisActivities.add(new TimeRegisActivity(project.name, activity.name, project.id,activity.id,time));
                    }
                }
            }
        }
        return timeRegisActivities;
    }

    public record ActivityStat(int projectID, String projectName, Activity activity) implements Serializable {
    }
    /**
     * @Author Thor
     */
    public record EmployeeStat(Employee employee, ArrayList<ActivityStat> assignedActivities) implements Serializable {
    }

    /**
     * @Author Frederik
     */
    public record ProjectStat(ArrayList<EmployeeStat> employeeStats,
                              ArrayList<Activity> unassignedActivities, int projectWeek, int timeWorked, int timeRemaining) implements Serializable {
    }
    /**
     * @Author Thor
     */
    public ProjectStat getProjectStats(int projectID, Session session) {
        assertLoggedIn(session);
        assertEmployeeInProject(projectID, session.employee.id);

        int timeWorked = projects.get(projectID).getUsedTime();
        int timeRemaining = projects.get(projectID).remainingTime();

        Project project = projects.get(projectID);
        if (project.projectLeaderId != session.employee.id) {
            throw new RuntimeException("Employee is not project leader");
        }
        HashMap<Integer, ArrayList<ActivityStat>> employeeActivities = new HashMap<>();

        for (int employeeId : project.assignedEmployees) {
            ArrayList<ActivityStat> vacationsAndSick = new ArrayList<>();
            //get vacations of Employee
            vacationsAndSick.addAll(getVacations(employeeId).stream().map(vacation -> new ActivityStat(-1, "Vacation", vacation)).toList());
            //get Sick Leaves of Employee
            vacationsAndSick.addAll(getSickLeaves(employeeId).stream().map(vacation -> new ActivityStat(-2, "Sick leave", vacation)).toList());

            employeeActivities.put(employeeId, vacationsAndSick);
        }

        for (Project cProject : projects) {
            for (Activity activity : cProject.activities) {
                for (int employeeID : activity.assignedEmployees) {
                    if (!project.assignedEmployees.contains(employeeID)) {
                        continue;
                    }
                    employeeActivities.get(employeeID).add(new ActivityStat(cProject.id, cProject.name, activity));
                }
            }
        }



        ArrayList<EmployeeStat> employeeStats = new ArrayList<>();
        employeeActivities.forEach((key, value) -> {
            employeeStats.add(new EmployeeStat(employees.get(key), value));
        });


        ArrayList<Activity> unassignedActivities = new ArrayList<>(project.activities.stream().filter(activity -> activity.assignedEmployees.isEmpty()).toList());

        return new ProjectStat(employeeStats, unassignedActivities, projects.get(projectID).startWeek, timeWorked, timeRemaining);
    }

    public void removeEmployeeFromActivity(int projectID,
                                           int activityID, String employeeName, Session session) {
        //pre-condition
        assert projects.stream().anyMatch(project -> project.id == projectID);
        assert projects.get(projectID).activities.stream().anyMatch(activity -> activity.id == activityID);
        assert employeeName != null;

        assertLoggedIn(session);                                            //1
        int employeeID = findEmployee(employeeName).id;                     //2
        //Check if employee on activity
        Activity activity = getActivity(projectID, activityID, session);
        if (!activity.assignedEmployees.contains(employeeID)) {             //3
            throw new RuntimeException("Employee not assigned to activity");
        }

        activity.assignedEmployees.removeIf(id -> id == employeeID);

        //post-condition
        assert !projects.get(projectID).activities.get(activityID).assignedEmployees.contains(employeeID);
    }
    public boolean isProjectLeader(int projectID,int employeeID, Session session) {
        return getProject(projectID,session).projectLeaderId == employeeID;
    }
}
