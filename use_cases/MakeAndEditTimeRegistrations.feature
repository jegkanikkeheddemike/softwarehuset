Feature: Employee makes and edits time registrations

  Scenario: Employee has registed work hours and then edits the registration
    Given user is logged in
    And a project "Hold 80's års fødselsdag for Karsten" exists
    And an activity "Køb ind til lagkage" exists
    And a time registration of "3:06" work hours is registered to the activity
    When the user changes the registration from "3:06" to "4:00"
    Then the time registration is changed to "4:00"

