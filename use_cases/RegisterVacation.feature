#Skrevet af Katinka
Feature: Register Vacation
  Description: the user can registers a vacation, with start and end date
  Actors: user

  Scenario: the User registers an upcoming Vacation
    Given user is logged in
    When the user register a vacation from week "42" to week "43"
    Then a vacation from week "42" to week "43" is registered for that user

  Scenario: the User is assigned to activity while on vacation
    Given user is logged in
    And the user has a vacation from week "16" to week "19"
    And a project "Farfars fødselsdag" exists
    And an activity "Sig hej til farfar" with start week "17" and end week "18" exists
    When the user is assigned to "Sig hej til farfar"
    Then error message "Employee on vacation" is given

  Scenario: the User registers an upcoming Vacation while not being logged in
    Given user is not logged in
    When the user register a vacation from week "42" to week "43"
    Then error message "Employee not logged in" is given


