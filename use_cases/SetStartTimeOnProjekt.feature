Feature: Set Start time for project
  Description: The user sets a starttime for the project
  Actors: user

  Scenario: Start time is set
    Given user is logged in
    And a project "Planlæg en rejse" exists
    When start time is set to week 10
    Then the projects start time exist is in week 10
  
  Scenario: Start time is set while not logged in
    Given user is not logged in
    And a project is created
    When start time is set
    Then error message "Employee not logged in" is given


    