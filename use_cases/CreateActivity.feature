Feature: Create Activity
  Description: The user creates an activity
  Actors: User

  Scenario: a user creates an activity
    Given user is logged in
    And a project is created
    When  a user creates an activity "Design GUI"
    Then an activity "Design GUI" is crated
    And the activity "Design GUI" is added to the list of activities

  Scenario: a user creates an activity without being logged in
    Given a project is created
    And user is not logged in
    When  a user creates an activity "Design GUI"
    Then error message "Employee not logged in" is given

