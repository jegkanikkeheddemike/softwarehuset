Feature: Assign Employee to activity
  
  actors: user
  
  Scenario: Assign employee to activity
    Given user is logged in
    And a project "Planlæg en rejse" exists
    And an activity "Køb kuffert" exists
    When the user is assigned to "Køb kuffert"
    Then  the user is assigned
    
    
  Scenario: Assign employee to already assigned activity
    Given user is logged in
    And a project "Planlæg en rejse" exists
    And an activity "Køb kuffert" exists
    When the user is assigned to "Køb kuffert"
    When the user is assigned to "Køb kuffert"
    Then  error message "Employee already assigned" is given