Feature: White Box tests for createEmployee method

  Scenario: realName has only one word
    Given the argument realName has value "Karsten"
    When the method createEmployee is called
    Then employees has an element with the name "kars"
    
  Scenario: realName has multiple words
    Given the argument realName has value "Jens Karsten"
    When the method createEmployee is called
    Then employees has an element with the name "jeka"


