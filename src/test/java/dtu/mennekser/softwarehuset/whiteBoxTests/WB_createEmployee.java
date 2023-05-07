package dtu.mennekser.softwarehuset.whiteBoxTests;

import dtu.mennekser.softwarehuset.backend.schema.AppBackend;
import dtu.mennekser.softwarehuset.backend.schema.Employee;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.CucumberOptions.SnippetType;
import org.junit.runner.RunWith;


import static org.junit.Assert.*;

import java.util.ArrayList;

public class WB_createEmployee {
    static AppBackend appBackend;

    static String realName;

    /**
     * @Author Christian
     */

    public WB_createEmployee() {
        appBackend = new AppBackend();
    }
    @Given("the argument realName has value {string}")
    public void theVariableRealNameHasValue(String name) {
        realName = name;
    }

    @When("the method createEmployee is called")
    public void theMethodCreateEmployeeIsCalled() {
        appBackend.createEmployee(realName);
    }

    @Then("employees has an element with the name {string}")
    public void employeesHasAnElementWithTheName(String name) {
        ArrayList<Employee> employees = appBackend.getEmployees();
        assertTrue(employees.stream().anyMatch(employee -> employee.name.equals(name)));
    }
}
