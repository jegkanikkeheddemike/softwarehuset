#Skrevet af Tobias
Feature: Whitebox test for findProject method

  Scenario: projects is empty
    Given projects is empty
    When findProject is called with name "Karsten"
    Then findProject returns null

  Scenario: projects has project "Byg et tårn"
    Given project "Byg et tårn" exist
    When findProject is called with name "Byg en båd"
    Then findProject returns null

  Scenario: projects has project "Byg en båd"
    Given project "Byg en båd" exist
    When findProject is called with name "Byg en båd"
    Then findProject returns project "Byg en båd"