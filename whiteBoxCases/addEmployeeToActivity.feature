#Skrevet af Katinka
Feature: White Box tests for addEmployeeToActivity method

  Scenario: session is null
    Given a project "Make a cake" exists with projectID 0
    And an activity "Buy ingredients" exists with activityID 0
    And the session is null
    When the method addEmployeeToActivity is called
    Then error message "Employee not logged in" is given

  Scenario: employeeName is not the name of an employee
    Given a project "Make a cake" exists with projectID 0
    And an activity "Buy ingredients" exists with activityID 0
    And employeeName is "Karsten"
    When the method addEmployeeToActivity is called
    Then error message "Employee not found" is given

  Scenario: employee is on vacation
    Given a project "Make a cake" exists with projectID 0
    And an activity "Buy ingredients" exists with activityID 0
    And employee with employeeID 1 is on vacation in week "1" to "52"
    When the method addEmployeeToActivity is called
    Then error message "Employee on vacation" is given

  Scenario: user not in project
    Given a project "Make a cake" exists with projectID 0
    And an activity "Buy ingredients" exists with activityID 0
    And user is not in project
    When the method addEmployeeToActivity is called
    Then error message "Employee not in project" is given

  Scenario: employee is added to activity
    Given a project "Make a cake" exists with projectID 0
    And an activity "Buy ingredients" exists with activityID 0
    When the method addEmployeeToActivity is called
    Then the activity with activityID 0 in the project with projectID 0 has an assigned employee with name "jeka"

