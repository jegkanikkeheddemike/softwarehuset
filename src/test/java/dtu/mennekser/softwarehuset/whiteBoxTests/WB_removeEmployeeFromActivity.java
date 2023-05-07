package dtu.mennekser.softwarehuset.whiteBoxTests;

import dtu.mennekser.softwarehuset.app.LoginManager;
import dtu.mennekser.softwarehuset.backend.schema.AppBackend;
import dtu.mennekser.softwarehuset.backend.schema.Employee;
import dtu.mennekser.softwarehuset.backend.schema.Session;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

public class WB_removeEmployeeFromActivity {
    static AppBackend appBackend;
    static int projectID;
    static int activityID;
    static int employeeID;
    static String error;
    static Session session;

    public WB_removeEmployeeFromActivity() {
        appBackend = new AppBackend();
        //Fyld med data
        int tmpEmployeeId = appBackend.createEmployee("Barack Obama");
        Employee tmpEmployee= appBackend.getEmployees().get(tmpEmployeeId);

        int tmpProjectId = appBackend.createProject("junkdata","",appBackend.attemptLogin(tmpEmployee.name),"0");
        appBackend.createActivity(tmpProjectId,"Byg en by", 1, appBackend.attemptLogin(tmpEmployee.name));

    }


    @Given("session is null")
    public void sessionIsNull() {
        session = null;
    }

    @When("the user removes {string} from an activity")
    public void theUserRemovesFromAnActivity(String employeeName) {
        try {
            appBackend.removeEmployeeFromActivity(projectID,activityID,employeeName,session);
        } catch (RuntimeException e){
            error = e.getMessage();
        }

    }

    @Then("an exception with the message {string} is thrown")
    public void anExceptionWithTheMessageIsThrown(String message) {
        Assertions.assertEquals(error,message);
    }

    @Given("session is not null")
    public void sessionIsNotNull() {
        appBackend.createEmployee("thor");
        session = appBackend.attemptLogin("thor");
    }

    @And("a project and activity without employee {string} exists")
    public void aProjectAndActivityWithoutEmployeeExists(String unused) {
        projectID = appBackend.createProject("testproject","",session,"1");
        activityID = appBackend.createActivity(projectID,"testactivity",1,session);

    }

    @And("an employee with name {string} exists")
    public void anEmployeeWithNameExists(String employeeName) {
        appBackend.createEmployee(employeeName);
    }

    @And("a project and activity with the employee {string} exists")
    public void aProjectAndActivityWithTheEmployeeExists(String employeeName) {
        projectID = appBackend.createProject("testproject","",session,"1");
        activityID = appBackend.createActivity(projectID,"testactivity",1,session);
        appBackend.addEmployeeToActivity(projectID,activityID,employeeName,session);
    }

    @Then("the user is no longer assigned to the activity")
    public void theUserIsNoLongerAssignedToTheActivity() {
        Assertions.assertFalse(appBackend.getActivity(projectID,activityID,session).assignedEmployees.contains(employeeID));
    }
}
