Feature: Find time used on activity
  Description: The user can see time used
  Actors: user

  Scenario: a project leader checks time used on activity
    Given ProjectLeader is logged in
    And an activity "new activity" exists
    And the activity "new activity" has budgeted time of "60" hour
    When the user registers "0:5" work hours to "new activity"
    Then time used on project is "5"
    And there is "55" hours remaining

  Scenario: a user checks time used on activity
    Given user is logged in
    And a project "new project" exists
    And an activity "new activity" exists
    And the user registers "0:5" work hours to "new activity"
    When user checks time used on activity
    Then error message "Employee not project leader" is given