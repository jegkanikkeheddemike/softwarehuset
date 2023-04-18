Feature: Create Activity
  Description: The user creates an activity
  Actors: User

  Scenario: a user creates an activity
    Given user is logged in
    And user creates a project
    When  a user creates an activity "Design GUI" with 100 hours
    Then an activity "Design GUI" is crated
    And the activity "Design GUI" is added to the list of activities

  Scenario: a user creates an activity without being logged in
    Given user creates a project
    And user is not logged in
    When  a user creates an activity "Design GUI" with 100 hours
    Then error message "Employee not logged in" is given

