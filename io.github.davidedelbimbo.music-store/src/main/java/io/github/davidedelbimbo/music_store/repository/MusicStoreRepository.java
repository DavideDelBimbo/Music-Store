package io.github.davidedelbimbo.music_store.repository;

import java.util.List;

import io.github.davidedelbimbo.music_store.model.Playlist;
import io.github.davidedelbimbo.music_store.model.Song;

public interface MusicStoreRepository {
	void initilizeSongCollection(List<Song> songs);

	List<Song> findAllSongs();

	Song findSongById(Integer songId);
	
	List<Playlist> findAllPlaylists();

	Playlist findPlaylistByName(String playlistName);

	void createPlaylist(Playlist playlist);

	void updatePlaylist(Playlist playlist);

	void deletePlaylist(Playlist playlist);
}
