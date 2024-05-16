package io.github.davidedelbimbo.music_store.view;

import java.util.List;

import io.github.davidedelbimbo.music_store.model.Playlist;
import io.github.davidedelbimbo.music_store.model.Song;

public interface MusicStoreView {
	void displayAllSongsInStore(List<Song> songs);

	void displayAllPlaylists(List<Playlist> playlists);

	void displayPlaylist(Playlist playlist);

	void hidePlaylist(Playlist playlist);

	void displayAllSongsInPlaylist(List<Song> songs);

	void displaySongInPlaylist(Song song);

	void hideSongFromPlaylist(Song song);

	void displayErrorPlaylistAlreadyExists(String message, Playlist playlist);

	void displayErrorPlaylistNotFound(String message, Playlist playlist);

	void displayErrorSongAlreadyInPlaylist(String message, Song song, Playlist playlist);

	void displayErrorSongNotFoundInPlaylist(String message, Song song, Playlist playlist);
}
