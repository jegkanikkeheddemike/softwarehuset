Feature: White Box tests for removeEmployeeFromActivity method

  Scenario: user not logged in
    Given session is null
    When the user removes "jeka" from an activity
    Then an exception with the message "Employee not logged in" is thrown

  Scenario: no employee with name employeeName exists
    Given session is not null
    When the user removes "jeka" from an activity
    Then an exception with the message "Employee not found" is thrown

  Scenario: the employee with the name employeeName is not in the activity
    Given session is not null
    And an employee with name "jens karsten" exists
    And a project and activity without employee "jeka" exists
    When the user removes "jeka" from an activity
    Then an exception with the message "Employee not assigned to activity" is thrown

  Scenario: the employee with the name employeeName is in the activity
    Given session is not null
    And an employee with name "jens karsten" exists
    And a project and activity with the employee "jeka" exists
    When the user removes "jeka" from an activity
    Then the user is no longer assigned to the activity
