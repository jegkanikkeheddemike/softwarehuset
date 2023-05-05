Feature: White Box tests for createEmployee method

  Scenario: realName has length of less than 2
    Given the variable realName has value "Karsten"
    When the method createEmployee is called
    Then employees has an element with the name "kars"
    
  Scenario: realName has length of 2 or more
    Given the variable realName has value "Jens Karsten"
    When the method createEmployee is called
    Then employees has an element with the name "jeka"


