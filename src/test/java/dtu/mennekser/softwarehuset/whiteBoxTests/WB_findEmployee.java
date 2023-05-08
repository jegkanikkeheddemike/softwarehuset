package dtu.mennekser.softwarehuset.whiteBoxTests;

import dtu.mennekser.softwarehuset.backend.schema.AppBackend;
import dtu.mennekser.softwarehuset.backend.schema.Employee;
import io.cucumber.java.en.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WB_findEmployee {

    AppBackend appBackend = new AppBackend();

    String error;

    Employee employee;

    public WB_findEmployee() {

    }



    @Given("employees is empty")
    public void employeesIsEmpty() {
    }
    @When("the method findEmployee is called with name {string}")
    public void theMethodFindEmployeeIsCalledWithName(String string) {
        try {
            employee = appBackend.findEmployee(string);
        } catch (Exception e) {
            error = e.getMessage();
        }
    }
    @Then("error message {string} is returned")
    public void errorMessageIsReturned(String string) {
       assertEquals(error,string);
    }

    @Given("employee in employees with name {string} exists")
    public void employeeInEmployeesWithNameExists(String string) {
        appBackend.createEmployee(string);
    }

    @Then("findEmployee returns employee with name {string}")
    public void findEmployeeReturnsEmployeeWithName(String string) {
        assertEquals(string,employee.name);
    }
}
