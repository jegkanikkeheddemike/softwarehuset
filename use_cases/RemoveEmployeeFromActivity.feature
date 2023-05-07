#Skrevet af Tobias
Feature: Remove Employee from activity or project
  Description: a previously assigned employee can be unassigned
  Actors: user

  Scenario: an employee is unassigned
    Given a project "Lav projekt planlægnings program" exists
    And an activity "skriv employee klasse" exists
    And another Employee "Gitte" exists
    And "gitt" is assigned to project "Lav projekt planlægnings program"
    And "gitt" is assigned to "skriv employee klasse"
    When "Gitte" is unassigned from the activity
    Then "Gitte" is no longer part of that activity
    When "Gitte" is unassigned from the activity
    And error message "Employee not assigned to activity" is given

  Scenario: user wants to know who aren't yet assign to project
    Given user is logged in
    And another Employee "Gitte" exists
    And another Employee "Karsten" exists
    And another Employee "Jens" exists
    And a project "Lav projekt planlægnings program" exists
    When the user requests a list of unassigned employees
    Then a list containing "gitt", "jens" and "kars" is returned