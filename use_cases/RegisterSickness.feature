Feature: Register Sickness
  Description: if an employee becomes sick, they will be able to register that they are sick
  Actors: user

  Scenario: the User registers that they are sick
    Given user is logged in
    When the user registers that they are sick in week 17
    Then the user is registered as sick in week 17

  Scenario: the User assigns a sick Employee to an activity
    Given user is logged in
    And another Employee "Karsten" exists
    And "Karsten" is sick in week 17
    And a project "Farfars fødselsdag" exists
    And an activity "Sig hej til farfar" with start week "17" and end week "18" exists
    When "Karsten" is assigned to "Sig hej til farfar"
    Then error message "Employee is sick" is given
