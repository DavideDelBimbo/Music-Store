package io.github.davidedelbimbo.music_store.repository;

import java.util.List;

import io.github.davidedelbimbo.music_store.model.Playlist;
import io.github.davidedelbimbo.music_store.model.Song;

public interface MusicStoreRepository {
	List<Song> findAllSongs();

	Song findSongById(Integer songId);

	void addSong(Song song);

	void removeSong(Song song);

	List<Playlist> findAllPlaylists();

	Playlist findPlaylistByName(String playlistName);

	void createPlaylist(Playlist playlist);

	void updatePlaylist(Playlist playlist);

	void deletePlaylist(Playlist playlist);
}
