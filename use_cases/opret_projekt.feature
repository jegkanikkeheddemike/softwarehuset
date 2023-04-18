Feature: Create Project
    Description: The user creates a project
    Actors: user

  Scenario: A logged in user creates a project
    Given user is logged in
    When user creates a project
    Then a project is created
    And the project is added to the list of projects


  Scenario: A user that isn't logged in creates a project
    Given user is not logged in
    When user creates a project
    Then error message "Developer not logged in" is given

  Scenario: A user creates a project connected to a client
    Given user is logged in
    When user creates a project
    And a client "AP Moeller Maersk" is added to the project
    Then a project is created
    And that project has a client "AP Moeller Maersk"
    And the project is added to the list of projects