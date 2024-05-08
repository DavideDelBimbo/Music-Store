package io.github.davidedelbimbo.music_store.repository;

import java.util.List;

import io.github.davidedelbimbo.music_store.model.Playlist;
import io.github.davidedelbimbo.music_store.model.Song;

public interface MusicStoreRepository {

	List<Song> findAllSongs();

	List<Song> findAllSongsInPlaylist(Playlist playlist);

	List<Playlist> findAllPlaylists();

	void addPlaylist(Playlist playlist);

	void removePlaylist(Playlist playlist);

	Boolean isPlaylistExists(Playlist playlist);

	void addSongInPlaylist(Playlist playlist, Song song);

	void removeSongFromPlaylist(Playlist playlist, Song song);

	Boolean isSongInPlaylist(Playlist playlist, Song song);
}
