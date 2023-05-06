package dtu.mennekser.softwarehuset.whiteBoxTests;

import dtu.mennekser.softwarehuset.backend.schema.AppBackend;
import dtu.mennekser.softwarehuset.backend.schema.Employee;
import dtu.mennekser.softwarehuset.backend.schema.Session;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WB_addEmployeeToActivity {

    static AppBackend appBackend;
    Session session;
    String error;
    int employeeID;
    int otherEmployeeID;
    String otherEmployeeName;
    int projectID;
    int activityID;

    // katinka
    public WB_addEmployeeToActivity() {
        appBackend = new AppBackend();
        employeeID = appBackend.createEmployee("User");
        session = appBackend.attemptLogin("user");

        otherEmployeeID = appBackend.createEmployee("Jens Karsten");
        otherEmployeeName = appBackend.getEmployees().get(1).name;
    }

    @Given("a project {string} exists with projectID {int}")
    public void aProjectExistsWithProjectID(String string, Integer int1) {
        projectID = appBackend.createProject(string,"",session,"1");
    }
    @Given("an activity {string} exists with activityID {int}")
    public void anActivityExistsWithActivityID(String string, Integer int1) {
        activityID = appBackend.createActivity(projectID,string,10,session);
        appBackend.getActivity(projectID,activityID,session).setStartWeek(5);
        appBackend.getActivity(projectID,activityID,session).setEndWeek(7);
    }
    @Given("the session is null")
    public void sessionIsNull() {
        session = null;
    }


    @Given("employeeName is {string}")
    public void employeeNameIs(String string) {
        otherEmployeeName = string;
    }

    @Given("employee with employeeID {int} is on vacation in week {string} to {string}")
    public void employeeWithEmployeeIDIsOnVacationInWeekTo(Integer int1, String start, String end) {
        session = appBackend.attemptLogin(appBackend.getEmployees().get(1).name);
        appBackend.createVacation(start,end,session);
        session = appBackend.attemptLogin("user");
    }

    @Given("user is not in project")
    public void userIsNotInProject() {
        appBackend.createEmployee("Im not in the project");
        session = appBackend.attemptLogin("imno");
    }

    @When("the method addEmployeeToActivity is called")
    public void theMethodAddEmployeeToActivityIsCalled() {
        try{
            appBackend.addEmployeeToActivity(projectID,activityID,otherEmployeeName,session);
        }catch(Exception e){
            error = e.getMessage();
        }
    }
    @Then("error message {string} is given")
    public void errorMessageIsGiven(String string) {
        assertEquals(string,error);
    }

    @Then("the activity with activityID {int} in the project with projectID {int} has an assigned employee with name {string}")
    public void theActivityWithActivityIDInTheProjectWithProjectIDHasAnAssignedEmployeeWithName(Integer int1, Integer int2, String string) {
        assertEquals(appBackend.getEmployees().get(appBackend.getActivity(int2, int1, session).assignedEmployees.get(0)).name, string);
    }

}
