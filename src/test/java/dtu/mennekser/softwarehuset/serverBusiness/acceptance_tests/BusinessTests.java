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

    AppBackend appBackend;
    String error;
    Session session;
    int projectID;
    int activityID;

    public BusinessTests() {
        appBackend = new AppBackend();
        appBackend.createEmployee("Hanne");
    }

    //----------------------------------------------------------//
    //                  Create Project Feature                  //
    //----------------------------------------------------------//

    @Given("user is logged in")
    public void user_is_logged_in() {
        //logIn Hanne and get the session
        session = appBackend.attemptLogin("Hanne");
    }
    @When("user creates a project")
    public void user_creates_a_project() {
            try {
                projectID = appBackend.createProject("Sommerhus infoside" , session);
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
        assertTrue(appBackend.findProject("Sommerhus infoside").assignedEmployees.contains(appBackend.findEmployee("Hanne").id));
    }

    @Given("user is not logged in")
    public void user_is_not_logged_in() {
        if(session != null) {
            LoginManager.logout();}
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
            activityID = appBackend.createActivity(projectID,string,hours,session);
        } catch (Exception e){
            error = e.getMessage();
        }
    }
    @Then("an activity {string} is crated")
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


    //----------------------------------------------------------//
    //                Assign Employee To Activity               //
    //----------------------------------------------------------//


    //----------------------------------------------------------//
    //                      Set Budget Time                     //
    //----------------------------------------------------------//

    @Given("ProjectLeader is logged in")
    public void project_leader_is_logged_in() {
        session = appBackend.attemptLogin("Hanne");
        projectID = appBackend.createProject("Mormor's fødselsdag",session);
        appBackend.setProjectLeader(projectID,session);
    }
    @Then("the activity {string} has budgeted time of {int} hours")
    public void the_activity_has_budgeted_time_of_hours(String string, Integer int1) {
        assertTrue(int1 == appBackend.getActivity(projectID,activityID,session).getBudgetTime());
    }

}
