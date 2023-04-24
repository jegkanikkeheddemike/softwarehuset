Feature: Assign Employee to Project
  Description: The user creates an activity
  Actors: User

  Scenario: Assign Employee to Project
    Given user is logged in
    And user creates a project
    And another Employee "Karsten Ung" exists
    When another Employee "kaun" is added to the project
    Then the Employee "kaun" is part of the project

  Scenario: Assign Employee that does not exist to Project
    Given user is logged in
    And user creates a project
    When another Employee "I don't exist" is added to the project
    Then error message "Employee not found" is given
