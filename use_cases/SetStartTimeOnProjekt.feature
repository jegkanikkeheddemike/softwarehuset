Feature: Set Start time for project
  Description: The user sets a starttime for the project
  Actors: user

  Scenario: Start time is set
    Given user is logged in
    And a project is created
    When start time is set
    Then the projekts start time exist
  
  Scenario: Start time is set while not logged in
    Given user is not logged in
    And a project is created
    When start time is set
    Then error message "Employee not logged in" is given


    