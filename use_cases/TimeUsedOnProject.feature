Feature: Find time usage on project
  Description: The project leader can check the amount of hours
  that are left on a given project, and also see how many hours have been worked
  Actors: User

  Scenario: A project leader checks time usage on a project
    Given user is logged in
    And a project "Byg et hus" exists with a budgeted time of 10 hours
    And the user is the project leader of "Byg et hus"
    And an activity "Læg fundament" exists
    And there is 4 hours registered to the activity
    When the user checks the time usage of the project
    Then the program outputs "4 hours worked" and "6 hours remaining"

  Scenario: A non-project leader checks time usage on a project
    Given user is logged in
    And a project "Byg et hus" exists with a budgeted time of 10 hours
    And the user is not the project leader of "Byg et hus"
    When the user checks the time usage of the project
    Then the error message "Can't check time usage when not the project leader" is given