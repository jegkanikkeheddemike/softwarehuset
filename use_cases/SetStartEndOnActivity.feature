#Skrevet af Thor
Feature: Set Start time and End time on activity

  Scenario: Set Start and End time on activity
    Given user is logged in
    And a project "Lagerhus system" exists
    And an activity "Lagertal" exists
    When a user sets the start time to week 14 and the end time to week 16
    Then start time for activity is set to week 14
    And end time for activity is set to week 16

