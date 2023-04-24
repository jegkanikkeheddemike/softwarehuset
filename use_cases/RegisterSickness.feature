Feature: Register Sickness
  Description: if an employee becomes sick, they will be able to register that they are sick
  Actors: user

  Scenario: the User registers that they are sick
    Given user is logged in
    When the user registers that they are sick in week "17"
    Then the user is registered as sick in week "17"
    