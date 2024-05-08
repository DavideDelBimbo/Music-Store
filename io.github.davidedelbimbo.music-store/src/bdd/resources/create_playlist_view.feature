Feature: Create Playlist view
  Specifications of the behavior of the Create Playlist view

  Background:
    Given The Create Playlist popup view is shown

  Scenario: Initialize the Create Playlist view
    Then The text field of the playlist name is empty
    And The "Create" button is disabled
    And No error message is shown

  Scenario: Give an empty playlist name
    When The text field of the playlist name is empty
    Then The "Create" button is disabled
    And No error message is shown

  Scenario: Give a playlist name
    When The user provides the name "My Playlist" in the text field of the playlist name
    Then The "Create" button is enabled

  Scenario: Create a playlist
    When The user provides the name "My Playlist" in the text field of the playlist name
    And The user clicks on the "Create" button
    Then The Create Playlist view is closed

  Scenario: Create an existing playlist
    When The user provides the name "My Playlist" in the text field of the playlist name
    And The user click on the "Create" button
    Then An error message is shown
    And The Create Playlist view is not closed

  Scenario: Cancel a playlist creation
    When  The user clicks on the "Cancel" button
    Then The Create Playlist view is closed
