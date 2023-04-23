Feature: Find time used on activity
  Description: The user can see time used
  Actors: user

  Scenario: a project leader checks time used on activity
    Given user is logged in
    And a project "new project" exists
    And the user is assigned as project leader
    And an activity "new activity" exists
    And activity has registered time
    When user checks time used on activity
    Then user gets time used on activity

  Scenario: a user checks time used on activity
    Given user is logged in
    And a project "new project" exists
    And an activity "new activity" exists
    And activity has registered time
    When user checks time used on activity
    Then error message "user not project leader" is given