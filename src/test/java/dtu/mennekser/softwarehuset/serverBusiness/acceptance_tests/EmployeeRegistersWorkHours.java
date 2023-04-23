package dtu.mennekser.softwarehuset.serverBusiness.acceptance_tests;

import dtu.mennekser.softwarehuset.backend.schema.AppBackend;
import dtu.mennekser.softwarehuset.backend.schema.Session;
import dtu.mennekser.softwarehuset.backend.schema.TimeRegistration;
import io.cucumber.java.an.E;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.assertTrue;

public class EmployeeRegistersWorkHours {

    TimeRegistration timeReg;

    public EmployeeRegistersWorkHours() {
    }


    @When("the user registers {string} work hours to {string}")
    public void the_user_registers_work_hours_to(String hours, String string) {
        try {
            timeReg = BusinessTests.appBackend.registerTime(BusinessTests.projectID, BusinessTests.activityID, hours, BusinessTests.session);
        } catch (Exception e){
            BusinessTests.error = e.getMessage();
        }
    }
    @Then("the {string} work hours are registered to {string}")
    public void the_work_hours_are_registered_to(String hours, String string) {
        assertTrue(BusinessTests.appBackend.getTimeRegistrationsOfActivity(BusinessTests.projectID,BusinessTests.activityID,BusinessTests.session).contains(timeReg));
    }

}
