#Skrevet af Christian
Feature: Find time usage on project
  Description: The project leader can check the amount of hours
  that are left on a given project, and also see how many hours have been worked
  Actors: User

  Scenario: A project leader checks time usage on a project
    Given user is logged in
    Given a project "Byg et hus" exists with start week 10
    Given there is a project leader of "Byg et hus"
    Given project leader of "Byg et hus" is logged in
    And an activity "Læg fundament" with 10 hours budgeted time exists
    And there is 4 hours and 53 minutes registered to the activity
    When the user checks the time usage of the project
    Then the program outputs 4 hours 53 minutes as worked and 5 hours 7 minutes as remaining

  Scenario: A non-project leader checks time usage on a project
    Given user is logged in
    Given a project "Byg et hus" exists with start week 10
    Given there is a project leader of "Byg et hus"
    Given project leader of "Byg et hus" is not logged in
    When the user checks the time usage of the project
    Then error message "Employee is not project leader" is given