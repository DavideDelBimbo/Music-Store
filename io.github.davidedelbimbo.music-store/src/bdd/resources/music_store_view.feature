Feature: Music Store View
  Specifications of the behavior of the Music Store View

  Background:
    Given the database contains a few songs
    And the database contains a playlist with a few songs in it
    And the Music Store view is shown

  Scenario: Create a new playlist
    Given the user clicks the "Create Playlist" button
    And the dialog is open
    And the user provides a name in the text field of the dialog
    When the user clicks the "Create Playlist" button of the dialog
    Then the drop-down list contains the new playlist

  Scenario: Create a new playlist with an existing name
    Given the user clicks the "Create Playlist" button
    And the dialog is open
    And the user provides a name in the text field of the dialog, specifying an existing name
    When the user clicks the "Create Playlist" button of the dialog
    Then an error is shown containing the name of the playlist

  Scenario: Delete an existing playlist
    Given the user selects a playlist from the drop-down list
    When the user clicks the "Delete Playlist" button
    Then the playlist is removed from the drop-down list
    And all songs in the playlist are removed

  Scenario: Delete a non-existing playlist
    Given the user selects a playlist from the drop-down list
    But the playlist has meanwhile been removed from the database
    When the user clicks the "Delete Playlist" button
    Then an error is shown containing the name of the playlist
    And the playlist is removed from the drop-down list

  Scenario: Add a song to the playlist
    Given the user selects a playlist
    And the user selects a song from the store
    When the user clicks the "Add To Playlist" button
    Then the song is shown in the playlist

  Scenario: Add an existing song to the playlist
    Given the user selects a playlist
    And the user selects a song from the store
    And the song is already in the playlist
    When the user clicks the "Add To Playlist" button
    Then an error is shown containing the name of the song

  Scenario: Remove an existing song from the playlist
    Given the user selects a playlist
    And the user selects a song from the playlist
    When the user clicks the "Remove From Playlist" button
    Then the song is removed from the playlist

  Scenario: Remove a non-existing song from the playlist
    Given the user selects a playlist
    And the user selects a song from the playlist
    But the song has meanwhile been removed from the database
    When the user clicks the "Remove From Playlist" button
    Then an error is shown containing the name of the song
    And the song is removed from the playlist