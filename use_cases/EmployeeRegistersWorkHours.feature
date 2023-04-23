Feature: Employee registers work hours on project
  
  Scenario: Employee registers work hours
    Given user is logged in
    And a project "Hold 80's års fødselsdag for Karsten" exists
    And an activity "Køb ind til lagkage" exists
    When the user registers 10 work hours to "Køb ind til lagkage"
    Then the 10 work hours are registered to "Køb ind til lagkage"
    
    
    
  Scenario: Employee registers work hours without being logged in
    Given user is not logged in
    And a project "Hold 80's års fødselsdag for Karsten" exists
    And an activity "Køb ind til lagkage" exists
    When the user registers 10 work hours to "Køb ind til lagkage"
    Then error message "Employee not logged in" is given