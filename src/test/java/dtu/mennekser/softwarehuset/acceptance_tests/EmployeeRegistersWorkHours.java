package dtu.mennekser.softwarehuset.acceptance_tests;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.assertTrue;

public class EmployeeRegistersWorkHours {


    public EmployeeRegistersWorkHours() {
    }


    @When("the user registers {string} work hours to {string}")
    public void the_user_registers_work_hours_to(String hours, String string) {
        try {
            BusinessTests.appBackend.registerTime(BusinessTests.projectID, BusinessTests.activityID, hours, BusinessTests.session);
        } catch (Exception e){
            BusinessTests.error = e.getMessage();
        }
    }
    @Then("the {string} work hours are registered to {string}")
    public void the_work_hours_are_registered_to(String hours, String string) {
        String[] hoursMinutes = hours.split(":");
        int time = Integer.parseInt(hoursMinutes[0])*60 +  Integer.parseInt(hoursMinutes[1]);
        assertTrue(BusinessTests.appBackend.getTimeRegistrationsOfActivity(BusinessTests.projectID,BusinessTests.activityID,BusinessTests.session).get(BusinessTests.appBackend.getTimeRegistrationsOfActivity(BusinessTests.projectID,BusinessTests.activityID,BusinessTests.session).size()-1).timeRegistration().usedTime == time);
    }

}
