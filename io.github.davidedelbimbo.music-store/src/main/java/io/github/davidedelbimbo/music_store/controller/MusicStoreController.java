package io.github.davidedelbimbo.music_store.controller;

import java.util.List;

import io.github.davidedelbimbo.music_store.model.Playlist;
import io.github.davidedelbimbo.music_store.model.Song;
import io.github.davidedelbimbo.music_store.repository.MusicStoreRepository;
import io.github.davidedelbimbo.music_store.view.MusicStoreView;

public class MusicStoreController {
	private MusicStoreView musicStoreView;
	private MusicStoreRepository musicStoreRepository;

	public MusicStoreController(MusicStoreView musicStoreView, MusicStoreRepository musicStoreRepository) {
		this.musicStoreView = musicStoreView;
		this.musicStoreRepository = musicStoreRepository;
	}

	public void allSongsInStore() {
		List<Song> songs = musicStoreRepository.findAllSongs();
		musicStoreView.displayAllSongsInStore(songs);
	}
	
	public void allSongsInPlaylist(Playlist playlist) {
		List<Song> songs = musicStoreRepository.findAllSongsInPlaylist(playlist);
		musicStoreView.displayAllSongsInPlaylist(songs);
	}

	public void allPlaylists() {
		List<Playlist> playlists = musicStoreRepository.findAllPlaylists();
		musicStoreView.displayAllPlaylists(playlists);
	}

	public void addPlaylist(Playlist playlist) {
		if (Boolean.TRUE.equals(musicStoreRepository.isPlaylistExists(playlist))) {
			musicStoreView.displayError("Playlist already exists: " + playlist);
			return;
		}

		musicStoreRepository.addPlaylist(playlist);
		musicStoreView.displayPlaylist(playlist);
	}

	public void removePlaylist(Playlist playlist) {
		if (Boolean.FALSE.equals(musicStoreRepository.isPlaylistExists(playlist))) {
			musicStoreView.displayError("Playlist not found: " + playlist);
			return;
		}

		musicStoreRepository.removePlaylist(playlist);
		musicStoreView.hidePlaylist(playlist);
	}

	public void addSongInPlaylist(Playlist playlist, Song song) {
		if (Boolean.TRUE.equals(musicStoreRepository.isSongInPlaylist(playlist, song))) {
			musicStoreView.displayError("Song already in " + playlist + ": " + song);
			return;
		}

		musicStoreRepository.addSongInPlaylist(playlist, song);
		musicStoreView.displaySongInPlaylist(playlist, song);
	}

	public void removeSongFromPlaylist(Playlist playlist, Song song) {
		musicStoreRepository.removeSongFromPlaylist(playlist, song);
		musicStoreView.hideSongFromPlaylist(playlist, song);
	}
}
