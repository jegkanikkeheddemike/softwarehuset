package dtu.mennekser.softwarehuset.serverBusiness.acceptance_tests;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import dtu.mennekser.softwarehuset.backend.schema.AppBackend;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CreateProject {
    /*
    AppBackend database;
    String error;

    public CreateProject() {

        database = new AppBackend();
        database.createEmployee("Hanne");
    }

    @Given("user is logged in")
    public void user_is_logged_in() {
        //log Hanne in
        LoginManager.setLoggedInEmployee(database.findEmployee("Hanne"));
    }
    @When("user creates a project")
    public void user_creates_a_project() {
            try {
                database.createProject("Sommerhus infoside" , LoginManager.getLoggedInEmployee().id);
            } catch (Exception e){
                error = e.getMessage();
            }
    }
    @Then("a project is created")
    public void a_project_is_created() {
        assertTrue(database.projects.contains(database.findProject("Sommerhus infoside")));
    }
    @Then("the project is added to the list of the users projects")
    public void the_project_is_added_to_the_list_of_the_users_projects() {
        assertTrue(database.findProject("Sommerhus infoside").assignedEmployees.contains(database.findEmployee("Hanne").id));
    }

    @Given("user is not logged in")
    public void user_is_not_logged_in() {
        if(LoginManager.getLoggedInEmployee() != null) {LoginManager.logout();}
    }

    @Then("error message {string} is given")
    public void error_message_is_given(String string) {
        assertEquals(string, error);
    }
    @When("a client {string} is added to the project")
    public void a_client_is_added_to_the_project(String string) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
    @Then("that project has a client {string}")
    public void that_project_has_a_client(String string) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }*/
}
