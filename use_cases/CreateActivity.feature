Feature: Create Activity
  Description: The user creates an activity
  Actors: User

  Scenario: a user creates an activity
    Given user is logged in
    And a project "Projektplanlægningsprogram" exists
    When  a user creates an activity "Design GUI" with 100 hours
    Then an activity "Design GUI" is created
    And the activity "Design GUI" is added to the list of activities

  Scenario: a user creates an activity without being logged in
    Given user creates a project
    And user is not logged in
    When a user creates an activity "Design GUI" with 100 hours
    Then error message "Employee not logged in" is given

  Scenario: a user creates an activity with start and end week
    Given user is logged in
    And a project "Lagerhus system" exists
    When a user creates an activity "Lagertal" with 100 hours, from week 14 to week 16
    Then an activity "Lagertal" is created
    And start time for activity is set to week 14
    And end time for activity is set to week 16

