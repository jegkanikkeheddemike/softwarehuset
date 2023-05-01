Feature: Start local server
  Description: A local server can be started. Which will listen on port 7009
  Actors: Server
  Scenario: a local server starts running
    Given the remote_location is set as localhost
    And a server is running
    Then a connection can be made to the server