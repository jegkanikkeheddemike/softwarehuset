#Skrevet af Thor
Feature: Query data
  Description: tests if it can query data
  Scenario: Query data
    Given the remote_location is set as localhost
    And a server is running
    When a query is made
    Then nothing is returned
    Then relevant data is inserted into the server
    When a query is made
    Then the data is returned