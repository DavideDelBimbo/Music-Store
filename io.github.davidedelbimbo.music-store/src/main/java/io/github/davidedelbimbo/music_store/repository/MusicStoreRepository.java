package io.github.davidedelbimbo.music_store.repository;

import java.util.List;

import io.github.davidedelbimbo.music_store.model.Playlist;
import io.github.davidedelbimbo.music_store.model.Song;

public interface MusicStoreRepository {
	List<Song> findAllSongs();

	void initializeSongs(List<Song> songs);

	List<Playlist> findAllPlaylists();

	Playlist findPlaylistByName(String playlistName);

	void createPlaylist(Playlist playlist);

	void updatePlaylist(Playlist playlist);

	void deletePlaylist(Playlist playlist);
}
