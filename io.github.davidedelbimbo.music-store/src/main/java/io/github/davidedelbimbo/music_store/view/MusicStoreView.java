package io.github.davidedelbimbo.music_store.view;

import java.util.List;

import io.github.davidedelbimbo.music_store.model.Playlist;
import io.github.davidedelbimbo.music_store.model.Song;

public interface MusicStoreView {

	void displayAllSongsInStore(List<Song> songs);

	void displayAllSongsInPlaylist(List<Song> songs);

	void displayAllPlaylists(List<Playlist> playlists);

	void displayPlaylist(Playlist playlist);

	void hidePlaylist(Playlist playlist);

	void displaySongInPlaylist(Playlist playlist, Song song);

	void hideSongFromPlaylist(Playlist playlist, Song song);

	void displayError(String message);
}