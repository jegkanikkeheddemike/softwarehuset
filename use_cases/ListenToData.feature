Feature: Listen to data
  Description: When a server is running. Data can be listend to real time

  Scenario: Listen to data real time
    Given the remote_location is set as localhost
    And a server is running
    And a client is listening to data
    When data is inserted into to server
    Then the client receives the data
    And the old data is not the same as the new data
