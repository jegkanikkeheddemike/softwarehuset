Feature: Assign Project Leader
  
  
  Scenario: Employee becomes project leader
    Given a project "Planlæg en rejse" exists
    When the user is assigned as project leader
    Then the user is the project leader of "Planlæg en rejse"
    
  Scenario: Employee becomes project leader when another is already assigned
    Given a project "Planlæg en rejse" exists
    And another Employee "Karsten" exists
    And "Karsten" is project leader
    When the user is assigned as project leader
    Then error message "Project leader already assigned" is given
    
  Scenario: Employee becomes project leader without being logged in
    Given a project "Planlæg en rejse" exists
    And user is not logged in
    When the user is assigned as project leader
    Then error message "Employee not logged in" is given