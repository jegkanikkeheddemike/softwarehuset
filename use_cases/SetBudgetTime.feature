#Skrevet af Frederik
Feature: Set budgeted time for activity
  Description: The user sets a time budget for an activity
  Actors: user

  Scenario: Set budgeted time for activity
    Given ProjectLeader is logged in
    And user creates a project
    When a user creates an activity "Sig hej til mormor" with 100 hours
    Then the activity "Sig hej til mormor" has budgeted time of 100 hours
