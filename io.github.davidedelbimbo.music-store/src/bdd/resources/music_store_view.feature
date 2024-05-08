Feature: Music Store view
  Specifications of the behavior of the Music Store View

  Background:
    Given The database contains a few songs
    And The Music Store view is shown

  Scenario: Initialize the Music Store view
    Then No playlist is selected from the drop-down list of playlists
    And The "Create Playlist" button is enabled
    And The "Delete Playlist" button is disabled
    And The list of available songs in the store is shown
    And The list of songs in the playlist is empty
    And The "Add To Playlist" button is disabled
    And The "Remove From Playlist" button is disabled
    And No error message is shown

  Scenario: Create a new playlist
    When The user clicks on the "Create Playlist" button
    Then The Create Playlist popup view is shown
    
  Scenario: Select an existing playlist
    When The user selects "My Playlist" playlist from the drop-down list of playlists
    Then Songs in the playlist list are shown

  Scenario: Remove an existing playlist
    When The user selects "My Playlist" playlist from the drop-down list of playlists
    And The user clicks on the "Delete Playlist" button
    Then The "My Playlist" playlist is removed from the drop-down list of playlists
    And Songs in the playlist list are removed

  Scenario: Remove a non-existing playlist
    When The user selects "My Playlist" playlist from the drop-down list of playlists
    And The user clicks on the "Delete Playlist" button
    But The "My Playlist" playlist is meanwhile removed from the database
    Then An error message is shown
    And The "My Playlist" playlist is removed from the drop-down list of playlists

  Scenario: Select a song from the store list
    When The user selects "My Playlist" playlist from the drop-down list of playlists
    And The user selects "Artist1 - Song1" song from the store list
    Then The "Add To Playlist" button is enabled

  Scenario: Select a song from the playlist list
    When The user selects "My Playlist" playlist from the drop-down list of playlists
    And The user selects "Artist1 - Song1" song from the playlist list
    Then The "Remove From Playlist" button is enabled

  Scenario: Add a song to the playlist
    When The user selects "Artist1 - Song1" song from the playlist list
    And The user clicks on the "Add To Playlist" button
    Then The "Artist1 - Song1" song is shown in the playlist list

  Scenario: Add an already existing song to the playlist
    When The user selects "Artist1 - Song1" song from the playlist list
    And The "Artist1 - Song1" song is already in the playlist list
    And The user clicks on the "Add To Playlist" button
    Then An error message is shown

  Scenario: Remove a song from the playlist
    When The user selects "Artist1 - Song1" song from the playlist list
    And The user clicks on the "Remove From Playlist" button
    Then The "Artist1 - Song1" song is removed from the playlist list

  Scenario: Remove a non-existing song from the playlist
    When The user selects "Artist1 - Song1" song from the playlist list
    And The user clicks on the "Remove From Playlist" button
    But The "Artist1 - Song1" playlist is meanwhile removed from the database
    Then An error message is shown
    And The "Artist1 - Song1" song is removed from the playlist list