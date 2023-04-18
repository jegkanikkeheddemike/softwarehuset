package dtu.mennekser.softwarehuset.serverBusiness.acceptance_tests;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import dtu.mennekser.softwarehuset.backend.schema.Database;
import dtu.mennekser.softwarehuset.backend.Business.LoginManager;

import static org.junit.Assert.assertTrue;

public class CreateProject {

    Database database;
    LoginManager loginManager;

    public CreateProject() {

        database = new Database();
        database.createEmployee("Hanne");
        loginManager = new LoginManager();

        //System.out.println(database.employees.toString());
    }

    @Given("user is logged in")
    public void user_is_logged_in() {
        //log Hanne in
        loginManager.setLoggedInEmployee(database.findEmployee("Hanne"));
    }
    @When("user creates a project")
    public void user_creates_a_project() {
        database.createProject("Sommerhus infoside" , database.findEmployee("Hanne").id);
    }
    @Then("a project is created")
    public void a_project_is_created() {
        //assertTrue(//her skal det teste om projektet er oprettet);
    }
    @Then("the project is added to the list of projects")
    public void the_project_is_added_to_the_list_of_projects() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Given("user is not logged in")
    public void user_is_not_logged_in() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("error message {string} is given")
    public void error_message_is_given(String string) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
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
    }
}
