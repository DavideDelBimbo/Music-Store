package io.github.davidedelbimbo.music_store.controller;

import java.util.List;

import io.github.davidedelbimbo.music_store.repository.MusicStoreRepository;
import io.github.davidedelbimbo.music_store.view.MusicStoreView;
import io.github.davidedelbimbo.music_store.model.Playlist;
import io.github.davidedelbimbo.music_store.model.Song;

public class MusicStoreController {
	public static final String PLAYLIST_NOT_FOUND_MSG = "Playlist not found: ";
	public static final String PLAYLIST_ALREADY_EXISTS_MSG = "Playlist already exists: ";
	public static final String SONG_NOT_FOUND_IN_PLAYLIST_MSG = "Song not found in playlist: ";
	public static final String SONG_ALREADY_IN_PLAYLIST_MSG = "Song already in playlist: ";

	private MusicStoreView musicStoreView;
	private MusicStoreRepository musicStoreRepository;

	public MusicStoreController(MusicStoreView musicStoreView, MusicStoreRepository musicStoreRepository) {
		this.musicStoreView = musicStoreView;
		this.musicStoreRepository = musicStoreRepository;
	}

	public void allSongs() {
		List<Song> songs = musicStoreRepository.findAllSongs();
		musicStoreView.displayAllSongsInStore(songs);
	}

	public void allPlaylists() {
		List<Playlist> playlists = musicStoreRepository.findAllPlaylists();
		musicStoreView.displayAllPlaylists(playlists);
	}

	public void createPlaylist(Playlist playlist) {
		// Check if playlist already exists.
		Playlist existingPlaylist = musicStoreRepository.findPlaylistByName(playlist.getName());
		if (existingPlaylist != null) {
			musicStoreView.displayErrorAndAddPlaylist(PLAYLIST_ALREADY_EXISTS_MSG, existingPlaylist);
			return;
		}

		// Create playlist.
		musicStoreRepository.createPlaylist(playlist);
		musicStoreView.displayPlaylist(playlist);
	}

	public void deletePlaylist(Playlist playlist) {
		// Check if playlist exists.
		if (musicStoreRepository.findPlaylistByName(playlist.getName()) == null) {
			musicStoreView.displayErrorAndRemovePlaylist(PLAYLIST_NOT_FOUND_MSG, playlist);
			return;
		}

		// Delete playlist.
		musicStoreRepository.deletePlaylist(playlist);
		musicStoreView.hidePlaylist(playlist);
	}

	public void allSongsInPlaylist(Playlist playlist) {
		Playlist playlistToUpdate = musicStoreRepository.findPlaylistByName(playlist.getName());
		if (playlistToUpdate == null) {
			musicStoreView.displayErrorAndRemovePlaylist(PLAYLIST_NOT_FOUND_MSG, playlist);
			return;
		}

		// Get all songs in playlist.
		List<Song> songs = playlistToUpdate.getSongs();
		musicStoreView.displayAllSongsInPlaylist(songs);
	}

	public void addSongToPlaylist(Playlist playlist, Song song) {
		// Check if playlist exists.
		Playlist playlistToUpdate = musicStoreRepository.findPlaylistByName(playlist.getName());
		if (playlistToUpdate == null) {
			musicStoreView.displayErrorAndRemovePlaylist(PLAYLIST_NOT_FOUND_MSG, playlist);
			return;
		}

		// Check if song is already in playlist.
		if (playlistToUpdate.getSongs().contains(song)) {
			musicStoreView.displayErrorAndUpdatePlaylist(SONG_ALREADY_IN_PLAYLIST_MSG, song, playlist);
			return;
		}

		// Add song to playlist.
		playlistToUpdate.addSong(song);
		musicStoreRepository.updatePlaylist(playlistToUpdate);
		musicStoreView.displayAllSongsInPlaylist(playlistToUpdate.getSongs());
	}

	public void removeSongFromPlaylist(Playlist playlist, Song song) {
		// Check if playlist exists.
		Playlist playlistToUpdate = musicStoreRepository.findPlaylistByName(playlist.getName());
		if (playlistToUpdate == null) {
			musicStoreView.displayErrorAndRemovePlaylist(PLAYLIST_NOT_FOUND_MSG, playlist);
			return;
		}

		// Check if song is in playlist.
		if (!playlistToUpdate.getSongs().contains(song)) {
			musicStoreView.displayErrorAndUpdatePlaylist(SONG_NOT_FOUND_IN_PLAYLIST_MSG,  song, playlist);
			return;
		}

		// Remove song from playlist.
		playlistToUpdate.removeSong(song);
		musicStoreRepository.updatePlaylist(playlistToUpdate);
		musicStoreView.displayAllSongsInPlaylist(playlistToUpdate.getSongs());
	}
}
