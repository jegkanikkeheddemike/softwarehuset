package dtu.mennekser.softwarehuset.serverBusiness.acceptance_tests;

import dtu.mennekser.softwarehuset.backend.schema.AppBackend;
import dtu.mennekser.softwarehuset.backend.schema.Session;
import dtu.mennekser.softwarehuset.backend.schema.TimeRegistration;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.assertTrue;

public class EmployeeRegistersWorkHours {


    public EmployeeRegistersWorkHours() {
    }


    @When("the user registers {string} work hours to {string}")
    public void the_user_registers_work_hours_to(String hours, String string) {
        BusinessTests.appBackend.registerTime(BusinessTests.projectID,BusinessTests.activityID,hours,BusinessTests.session);
    }
    @Then("the {string} work hours are registered to {string}")
    public void the_work_hours_are_registered_to(String hours, String string) {
        //assertTrue(appBackend.getTimeRegistrationsOfActivity(projectID,activityID,session).contains());
    }

}
