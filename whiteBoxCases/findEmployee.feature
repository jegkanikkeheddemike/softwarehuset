#Skrevet af Frederik
Feature: White Box tests for findEmployee method

  Scenario: employees is empty
    Given employees is empty
    When the method findEmployee is called with name "Karsten"
    Then error message "Employee not found" is returned

  Scenario: employees does not contain employee
    Given employee in employees with name "Jens" exists
    When the method findEmployee is called with name "Karsten"
    Then error message "Employee not found" is returned

  Scenario: employees contains employee
    Given employee in employees with name "Karsten" exists
    When the method findEmployee is called with name "kars"
    Then findEmployee returns employee with name "kars"
