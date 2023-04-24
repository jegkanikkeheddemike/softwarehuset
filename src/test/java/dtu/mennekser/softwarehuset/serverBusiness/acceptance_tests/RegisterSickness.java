package dtu.mennekser.softwarehuset.serverBusiness.acceptance_tests;

import dtu.mennekser.softwarehuset.app.LoginManager;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.assertTrue;

public class RegisterSickness {
    public RegisterSickness(){}

    @When("the user registers that they are sick in week {string}")
    public void theUserRegistersThatTheyAreSickInWeek(String week) {
        BusinessTests.appBackend.createSickLeave("Hanne", week ,BusinessTests.session);
    }
    @Then("the user is registered as sick in week {string}")
    public void theUserIsRegisteredAsSickInWeek(String week) {
        //assert the user has a sick leave with start week equal to that of the test
        assertTrue(BusinessTests.appBackend.getEmployees().get(BusinessTests.employeeID).sickLeave.get(0).getStartWeek() == Integer.parseInt(week));
    }
    @Given("{string} is sick in week {string}")
    public void isSickInWeek(String employeeName, String week) {
        BusinessTests.session =  BusinessTests.appBackend.attemptLogin("Hanne");
        BusinessTests.appBackend.createSickLeave(employeeName, week ,BusinessTests.session);
    }


}
