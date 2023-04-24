package dtu.mennekser.softwarehuset.serverBusiness.acceptance_tests;

import dtu.mennekser.softwarehuset.app.LoginManager;
import dtu.mennekser.softwarehuset.backend.schema.Session;
import io.cucumber.java.an.E;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import dtu.mennekser.softwarehuset.backend.schema.AppBackend;

import static org.junit.Assert.*;

public class BusinessTests {

    static AppBackend appBackend;
    static String error;
    static Session session;
    static int projectID;
    static int activityID;
    static int employeeID;

    public BusinessTests() {
        appBackend = new AppBackend();
        employeeID = appBackend.createEmployee("hann");
    }

    //----------------------------------------------------------//
    //                  Create Project Feature                  //
    //----------------------------------------------------------//

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

    @When("a client {string} is added to the project")
    public void a_client_is_added_to_the_project(String string) {
        appBackend.findProject("Sommerhus infoside").setClient(string);
    }
    @Then("that project has a client {string}")
    public void that_project_has_a_client(String string) {
        assertEquals(appBackend.findProject("Sommerhus infoside").client, string);
    }

    @When("a user creates an activity {string} with {int} hours")
    public void a_user_creates_an_activity(String string, int hours) {
        try {
            activityID = appBackend.getProject(projectID,session).createActivity(string,hours);
        } catch (Exception e){
            error = e.getMessage();
        }
    }

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
        assertEquals(appBackend.getProjectLeader(projectID, session).id, employeeID);
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

    @Given("ProjectLeader is logged in")
    public void project_leader_is_logged_in() {
        session = appBackend.attemptLogin("hann");
        projectID = appBackend.createProject("Mormor's fødselsdag","",session, "");
        appBackend.setProjectLeader(projectID,session);
    }
    @Then("the activity {string} has budgeted time of {int} hours")
    public void the_activity_has_budgeted_time_of_hours(String string, int int1) {
        assertEquals(int1, appBackend.getActivity(projectID, activityID, session).getBudgetTime());
    }

    //----------------------------------------------------------//
    //                       Set start time                     //
    //----------------------------------------------------------//

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

    @When("a user sets the start time to week {int} and the end time to week {int}")
    public void aUserSetsTheStartTimeToWeekAndTheEndTimeToWeek(int startWeek, int endWeek) {
        appBackend.updateActivityWeekBounds(projectID,activityID,startWeek,endWeek,session);
    }
}
