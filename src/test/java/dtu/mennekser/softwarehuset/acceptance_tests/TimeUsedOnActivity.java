package dtu.mennekser.softwarehuset.acceptance_tests;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.assertTrue;

public class TimeUsedOnActivity {
    /**
     * @Author Tobias
     */
    public TimeUsedOnActivity() {
    }
    @Given("the activity {string} has budgeted time of {string} hour")
    public void the_activity_has_budgeted_time_of_hours(String string, String hours) {
        String[] hoursMinutes = hours.split(":");
        int time = Integer.parseInt(hoursMinutes[0])*60 +  Integer.parseInt(hoursMinutes[1]);
        BusinessTests.appBackend.setBudgetedTime(BusinessTests.projectID,BusinessTests.projectID,time,BusinessTests.session);
    }

    @Then("time used on project is {string}")
    public void timeUsedOnProjectIs(String hours) {
        String[] hoursMinutes = hours.split(":");
        int time = Integer.parseInt(hoursMinutes[0])*60 +  Integer.parseInt(hoursMinutes[1]);
        assertTrue(BusinessTests.appBackend.TimeUsedActivity(BusinessTests.projectID,BusinessTests.activityID,BusinessTests.session) == time);
    }

    @And("there is {string} hours remaining")
    public void thereIsHoursRemaining(String hours) {
        String[] hoursMinutes = hours.split(":");
        int time = Integer.parseInt(hoursMinutes[0])*60 +  Integer.parseInt(hoursMinutes[1]);
        assertTrue(BusinessTests.appBackend.TimeRemainingActivity(BusinessTests.projectID,BusinessTests.activityID,BusinessTests.session) == time);
    }

    @When("user checks time used on activity")
    public void userChecksTimeUsedOnActivity() {
        try {
            BusinessTests.appBackend.TimeUsedActivity(BusinessTests.projectID,BusinessTests.activityID,BusinessTests.session);
        } catch (Exception e) {
            BusinessTests.error = e.getMessage();
        }

    }
}
