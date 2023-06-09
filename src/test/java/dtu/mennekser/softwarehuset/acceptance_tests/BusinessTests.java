package dtu.mennekser.softwarehuset.acceptance_tests;

import dtu.mennekser.softwarehuset.app.LoginManager;
import dtu.mennekser.softwarehuset.backend.schema.Employee;
import dtu.mennekser.softwarehuset.backend.schema.Session;
import io.cucumber.java.an.E;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import dtu.mennekser.softwarehuset.backend.schema.AppBackend;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class BusinessTests {

    static AppBackend appBackend;
    static String error;
    static Session session;
    static int projectID;
    static int activityID;
    static int employeeID;
    static AppBackend.ProjectStat projectStat;
    int registrationID;

    public BusinessTests() {
        appBackend = new AppBackend();
        employeeID = appBackend.createEmployee("hann");
    }

    //----------------------------------------------------------//
    //                  Create Project Feature                  //
    //----------------------------------------------------------//
    /**
     * @Author Frederik
     */
    @Given("user is logged in")
    public void user_is_logged_in() {
        //logIn Hanne and get the session
        session = appBackend.attemptLogin("hann");
    }
    @When("user creates a project")
    public void user_creates_a_project() {
            try {
                projectID = appBackend.createProject("Sommerhus infoside" ,"", session, "");
            } catch (Exception e){
                error = e.getMessage();
            }
    }
    @Then("a project is created")
    public void a_project_is_created() {
        //Hmm måske er den her test ikke rigtig god nok
        assertSame("Sommerhus infoside", appBackend.getProject(projectID, session).name);
    }
    @Then("the project is added to the list of the users projects")
    public void the_project_is_added_to_the_list_of_the_users_projects() {
        assertTrue(appBackend.findProject("Sommerhus infoside").assignedEmployees.contains(employeeID));
    }

    @Given("user is not logged in")
    public void user_is_not_logged_in() {
        session = null;
    }

    @Then("error message {string} is given")
    public void error_message_is_given(String string) {
        assertEquals(string, error);
    }

    //----------------------------------------------------------//
    //                  Create Activity Feature                 //
    //----------------------------------------------------------//
    /**
     * @Author Frederik
     */
    @When("a client {string} is added to the project")
    public void a_client_is_added_to_the_project(String string) {
        appBackend.setProjectClient(projectID,string,session);
    }
    @Then("that project has a client {string}")
    public void that_project_has_a_client(String string) {
        assertEquals(appBackend.findProject("Sommerhus infoside").client, string);
    }

    @When("a user creates an activity {string} with {int} hours")
    public void a_user_creates_an_activity(String string, int hours) {
        try {
            activityID = appBackend.createActivity(projectID,string,hours,session);
        } catch (Exception e){
            error = e.getMessage();
        }
    }

    /**
     * @Author Christian
     */
    @When("a user creates an activity {string} with {int} hours, from week {int} to week {int}")
    public void aUserCreatesAnActivityWithHoursFromWeekToWeek(String name, int budgetedTime, int startWeek, int endWeek) {
        try {
            activityID = appBackend.createActivity(projectID,name,budgetedTime,startWeek,endWeek,session);
        } catch (Exception e){
            error = e.getMessage();
        }
    }

    @Then("start time for activity is set to week {int}")
    public void startTimeForActivityIsSetToWeek(int startWeek) {
        // Write code here that turns the phrase above into concrete actions
        assertEquals(startWeek, appBackend.getActivity(projectID, activityID, session).getStartWeek());


    }
    @Then("end time for activity is set to week {int}")
    public void endTimeForActivityIsSetToWeek(int endWeek) {
        // Write code here that turns the phrase above into concrete actions
        assertEquals(endWeek, appBackend.getActivity(projectID, activityID, session).getEndWeek());
    }

    @Then("an activity {string} is created")
    public void an_activity_is_crated(String string) {

        //Hmm måske er den her test ikke rigtig god nok

        assertEquals(string, appBackend.getActivity(projectID, activityID, session).name);
    }
    @Then("the activity {string} is added to the list of activities")
    public void the_activity_is_added_to_the_list_of_activities(String string) {
        assertTrue(appBackend.getProject(projectID,session).activities.contains(appBackend.getActivity(projectID,activityID,session)));
    }

    //----------------------------------------------------------//
    //                  Assign Employee Feature                 //
    //----------------------------------------------------------//
    /**
     * @Author Katinka
     */
    @Given("another Employee {string} exists")
    public void another_employee_exists(String string) {
        appBackend.createEmployee(string);
    }
    @When("another Employee {string} is added to the project")
    public void another_employee_is_added_to_the_project(String string) {
        try {
            appBackend.addEmployeeToProject(projectID, string, session);
        } catch (Exception e){
            error = e.getMessage();
        }
    }
    @Then("the Employee {string} is part of the project")
    public void the_employee_is_part_of_the_project(String string) {
        assertTrue(appBackend.getAssignedEmployees(projectID,session).contains(appBackend.findEmployee(string)));
    }
    //----------------------------------------------------------//
    //                   Assign Project Leader                  //
    //----------------------------------------------------------//
    /**
     * @Author Katinka
     */
    @Given("a project {string} exists")
    public void a_project_exists(String string) {
        session = appBackend.attemptLogin("hann");
        projectID = appBackend.createProject(string,"",session,"");
    }
    @When("the user is assigned as project leader")
    public void the_user_is_assigned_as_project_leader() {

        try {
        appBackend.setProjectLeader(projectID,session);
        } catch (Exception e){
        error = e.getMessage();
        }
    }
    @Then("the user is the project leader of {string}")
    public void the_user_is_the_project_leader_of(String string) {
        assertTrue(appBackend.isProjectLeader(projectID,session.employee.id,session));
    }
    @Given("{string} is project leader")
    public void is_project_leader(String string) {
        session = appBackend.attemptLogin("kars");
        appBackend.setProjectLeader(projectID,session);
        session = appBackend.attemptLogin("hann");
    }


    //----------------------------------------------------------//
    //                Assign Employee To Activity               //
    //----------------------------------------------------------//
    /**
     * @Author Thor
     */
    @Given("an activity {string} exists")
    public void an_activity_exists(String string) {
        activityID = appBackend.createActivity(projectID,string, 100, session);
    }
    @When("the user is assigned to {string}")
    public void the_user_is_assigned_to(String string) {
        try {
            appBackend.addEmployeeToActivity(projectID, activityID, "hann", session);
        } catch (Exception e){
            error = e.getMessage();
        }
    }
    @Then("the user is assigned")
    public void the_user_is_assigned() {
        assertTrue(
                appBackend.getAssignedActivityEmployees(projectID,activityID,session).stream().map(employee -> employee.id).toList().contains(employeeID));
    }

    @When("{string} is assigned to {string}")
    public void isAssignedTo(String string, String string2) {
        try {
            appBackend.addEmployeeToActivity(projectID, activityID, string, session);
        } catch (Exception e){
            error = e.getMessage();
        }
    }

    @Then("{string} is assigned")
    public void isAssigned(String string) {
        assertTrue(
                appBackend.getAssignedActivityEmployees(projectID,activityID,session).stream().map(employee -> employee.id).toList().contains(appBackend.findEmployee(string).id));
    }



    //----------------------------------------------------------//
    //                      Set Budget Time                     //
    //----------------------------------------------------------//
    /**
     * @Author Frederik
     */
    @Given("ProjectLeader is logged in")
    public void project_leader_is_logged_in() {
        session = appBackend.attemptLogin("hann");
        projectID = appBackend.createProject("Mormor's fødselsdag","",session, "");
        appBackend.setProjectLeader(projectID,session);
    }
    @Then("the activity {string} has budgeted time of {int} hours")
    public void the_activity_has_budgeted_time_of_hours(String string, int int1) {
        assertEquals(int1, appBackend.getActivity(projectID, activityID, session).getBudgetTime()/60);
    }

    //----------------------------------------------------------//
    //                       Set start time                     //
    //----------------------------------------------------------//
    /**
     * @Author Tobias
     */
    @When("start time is set to week {int}")
    public void start_time_is_set(int startWeek) {
        try {
            appBackend.setStartTime(projectID, session, startWeek);
        } catch (Exception e) {
            error = e.getMessage();
        }
    }

    @Then("the projects start time exist is in week {int}")
    public void the_projects_start_time_exist_is_in_uge(int int1) {
        assertEquals(int1, appBackend.getStartTime(projectID,session));
    }

    //----------------------------------------------------------//
    //          Set start and end week on activity              //
    //----------------------------------------------------------//
    /**
     * @Author Christian
     */
    @When("a user sets the start time to week {int} and the end time to week {int}")
    public void aUserSetsTheStartTimeToWeekAndTheEndTimeToWeek(int startWeek, int endWeek) {
        appBackend.updateActivityWeekBounds(projectID,activityID,startWeek,endWeek,session);
    }

    //----------------------------------------------------------//
    //               Find time usage on project                 //
    //----------------------------------------------------------//

    /**
     * @Author Christian
     */
    @Given("a project {string} exists with start week {int}")
    public void aProjectExistsWithStartWeek(String string, Integer int1) {
        projectID = appBackend.createProject(string,"Tonny",session,int1.toString());
    }
    @Given("an activity {string} with {int} hours budgeted time exists")
    public void anActivityWithHoursBudgetedTimeExists(String string, Integer int1) {
        activityID = appBackend.createActivity(projectID,string,int1,session);
    }

    @Given("there is {int} hours and {int} minutes registered to the activity")
    public void thereIsHoursRegisteredToTheActivity(Integer int1, Integer int2) {
        appBackend.getActivity(projectID,activityID,session).registerTime(employeeID,int1,int2);
    }
    @Given("there is a project leader of {string}")
    public void thereIsAProjectLeaderOf(String string) {
        appBackend.setProjectLeader(projectID,session);
    }

    @Given("project leader of {string} is not logged in")
    public void projectLeaderOfIsNotLoggedIn(String string) {
        appBackend.createEmployee("Karsten");
        appBackend.addEmployeeToProject(projectID,"kars",session);
        session = appBackend.attemptLogin("kars");
    }
    @Given("project leader of {string} is logged in")
    public void projectLeaderOfIsLoggedIn(String string) {
        session = appBackend.attemptLogin("hann");
    }
    @When("the user checks the time usage of the project")
    public void theUserChecksTheTimeUsageOfTheProject() {
        try {
            projectStat = appBackend.getProjectStats(projectID,session);
        } catch (Exception e) {
            error = e.getMessage();
        }
    }
    @Then("the program outputs {int} hours {int} minutes as worked and {int} hours {int} minutes as remaining")
    public void theProgramOutputsAnd(Integer hWorked, Integer mWorked, Integer hRemaining , Integer mRemaining) {
        assertTrue(hWorked*60 + mWorked == projectStat.timeWorked() && hRemaining*60 + mRemaining == projectStat.timeRemaining());
    }

    //----------------------------------------------------------//
    //                Edit time registrations                   //
    //----------------------------------------------------------//
    /**
     * @Author Christian
     */
    @Given("a time registration of {string} work hours is registered to the activity")
    public void aTimeRegistrationOfWorkHoursAreRegisteredTo(String string) {
        registrationID = appBackend.registerTime(projectID,activityID,string,session);
    }
    @When("the user changes the registration from {string} to {string}")
    public void theUserChangesTheRegistrationTo(String string1 ,String string2) {
        String[] split = string2.split(":");
        int hours = Integer.parseInt(split[0]);
        int minutes = Integer.parseInt(split[1]);
        int newTime = hours*60 + minutes;

        appBackend.editTime(projectID,activityID,registrationID,newTime, session);
    }
    @Then("the time registration is changed to {string}")
    public void theTimeRegistrationIsChangedTo(String string) {
        int time = Integer.parseInt(string.split(":")[0]) * 60 + Integer.parseInt(string.split(":")[1]) ;
        assertEquals(appBackend.getTimeRegistration(projectID, activityID, registrationID, session).usedTime, time);
    }


    //----------------------------------------------------------//
    //         Remove Employees from activity or project        //
    //----------------------------------------------------------//
    /**
     * @Author Tobias
     */
    @Given("{string} is assigned to project {string}")
    public void isAssignedToProject(String string, String string2) {
        appBackend.addEmployeeToProject(projectID,string,session);
    }

    @When("{string} is unassigned from the activity")
    public void isUnassignedFromTheActivity(String string) {
        String username = string.substring(0,4).toLowerCase();
        try{
            appBackend.removeEmployeeFromActivity(projectID,activityID,username,session);
        } catch (Exception e){
            error = e.getMessage();
        }

    }
    @Then("{string} is no longer part of that activity")
    public void isNoLongerPartOfThatActivity(String string) {
        String username = string.substring(0,4).toLowerCase();
        assertTrue(appBackend.getEmployeesNotAssignedToActivity(projectID,activityID,session).
                stream().map(AppBackend.EmployeeNotAssignedToActivity::employee).map(employee -> employee.name).toList().contains(username));
    }

    List<String> names;
    @When("the user requests a list of unassigned employees")
    public void theUserRequestsAListOfUnassignedEmployees() {
        names = appBackend.getNotAssignedEmployees(projectID,session).stream().map(employee -> employee.name).toList();
    }
    @Then("a list containing {string}, {string} and {string} is returned")
    public void aListContainingAndIsReturned(String string, String string2, String string3) {
        assertTrue(names.contains(string) && names.contains(string2) && names.contains(string3));
    }


}
