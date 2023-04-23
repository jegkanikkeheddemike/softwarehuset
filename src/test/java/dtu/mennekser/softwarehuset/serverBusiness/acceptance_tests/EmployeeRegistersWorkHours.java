package dtu.mennekser.softwarehuset.serverBusiness.acceptance_tests;

import dtu.mennekser.softwarehuset.backend.schema.AppBackend;
import dtu.mennekser.softwarehuset.backend.schema.Session;
import dtu.mennekser.softwarehuset.backend.schema.TimeRegistration;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.assertTrue;

public class EmployeeRegistersWorkHours {

    AppBackend appBackend;
    String error;
    Session session;
    int projectID;
    int activityID;
    int employeeID;

    public EmployeeRegistersWorkHours() {
        appBackend = new AppBackend();
        employeeID = appBackend.createEmployee("Hanne");
    }


    @When("the user registers {string} work hours to {string}")
    public void the_user_registers_work_hours_to(String hours, String string) {
        appBackend.registerTime(projectID,activityID,hours,session);
    }
    @Then("the {string} work hours are registered to {string}")
    public void the_work_hours_are_registered_to(String hours, String string) {
        //assertTrue(appBackend.getTimeRegistrationsOfActivity(projectID,activityID,session).contains());
    }

}
