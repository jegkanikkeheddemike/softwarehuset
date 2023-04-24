package dtu.mennekser.softwarehuset.serverBusiness.acceptance_tests;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Given;

import static org.junit.Assert.assertTrue;

public class RegisterVacation {

    public RegisterVacation(){}

    @When("the user register a vacation from week {string} to week {string}")
    public void theUserRegisterAVacationFromWeekToWeek(String string, String string2) {
        try {
            BusinessTests.appBackend.createVacation(string, string2, BusinessTests.session);
        } catch (Exception e){
            BusinessTests.error = e.getMessage();
        }
    }
    @Then("a vacation from week {string} to week {string} is registered for that user")
    public void aVacationFromWeekToWeekIsRegisteredForThatUser(String string, String string2) {
        assertTrue(BusinessTests.appBackend.getVacations(BusinessTests.appBackend.findEmployee("Hanne").id).get(0).getStartWeek() == Integer.parseInt(string));
        assertTrue(BusinessTests.appBackend.getVacations(BusinessTests.appBackend.findEmployee("Hanne").id).get(0).getEndWeek() == Integer.parseInt(string2));
    }

    @Given("the user has a vacation from week {string} to week {string}")
    public void theUserHasAVacationFromWeekToWeek(String string, String string2) {
        BusinessTests.appBackend.createVacation(string,string2,BusinessTests.session);
    }
    @Given("an activity {string} with start week {string} and end week {string} exists")
    public void anActivityWithStartWeekAndEndWeekExists(String string, String string2, String string3) {
        BusinessTests.appBackend.createActivity(BusinessTests.projectID,string,100,Integer.parseInt(string2),Integer.parseInt(string3),BusinessTests.session);
    }


}
