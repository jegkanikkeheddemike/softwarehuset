Feature: Set Start time and End time on activity

  Scenario: Set Start and End time on activity
    Given user is logged in
    And a project "Lagerhus system" exists
    And an activity "Opret klasse" exists
    When start and end time is set from week "14" to week "16"
    Then start time for activity is set to week 14
    And end time for activity is set to week 16

