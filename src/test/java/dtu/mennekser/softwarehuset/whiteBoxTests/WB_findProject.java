package dtu.mennekser.softwarehuset.whiteBoxTests;

import dtu.mennekser.softwarehuset.backend.schema.AppBackend;
import dtu.mennekser.softwarehuset.backend.schema.Project;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.*;

public class WB_findProject {
    static AppBackend appBackend;
    static Project project;

    /**
     * @Author Tobias
     */
    public WB_findProject() {appBackend = new AppBackend();}
    @Given("projects is empty")
    public void projectsIsEmpty() {
    }

    @When("findProject is called with name {string}")
    public void findProjectIsCalledWithName(String string) {
        project = appBackend.findProject(string);
    }

    @Then("findProject returns null")
    public void findProjectReturnsNull() {
        assertNull(project);
    }

    @Given("project {string} exist")
    public void projectExist(String string) {
        appBackend.createEmployee("hann");
        appBackend.createProject(string, "No one", appBackend.attemptLogin("hann"), "1");
    }

    @Then("findProject returns project {string}")
    public void findProjectReturnsProject(String string) {
        assertTrue(project.name.equals(string));
    }
}
