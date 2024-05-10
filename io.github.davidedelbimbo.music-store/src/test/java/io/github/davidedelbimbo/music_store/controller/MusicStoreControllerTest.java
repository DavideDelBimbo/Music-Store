package io.github.davidedelbimbo.music_store.controller;

import static org.assertj.core.api.Assertions.*;

import static org.mockito.Mockito.*;
import org.mockito.InOrder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import io.github.davidedelbimbo.music_store.model.Playlist;
import io.github.davidedelbimbo.music_store.model.Song;
import io.github.davidedelbimbo.music_store.repository.MusicStoreRepository;
import io.github.davidedelbimbo.music_store.view.MusicStoreView;

public class MusicStoreControllerTest {
	private static Integer SONG_1_ID = 1;
	private static String SONG_1_TITLE = "Song1";
	private static String SONG_1_ARTIST = "Artist1";
	private static Integer SONG_2_ID = 2;
	private static String SONG_2_TITLE = "Song2";
	private static String SONG_2_ARTIST = "Artist2";
	private static String PLAYLIST_1_NAME = "Playlist1";
	private static String PLAYLIST_2_NAME = "Playlist2";

	private MusicStoreView musicStoreView;
	private MusicStoreRepository musicStoreRepository;
	private MusicStoreController musicStoreController;

	@Before
	public void setUp() {
		musicStoreView = mock(MusicStoreView.class);
		musicStoreRepository = mock(MusicStoreRepository.class);

		musicStoreController = new MusicStoreController(musicStoreView, musicStoreRepository);
	}

	@Test
	public void testAllSongs() {
		Song song1 = new Song(SONG_1_ID, SONG_1_TITLE, SONG_1_ARTIST);
		Song song2 = new Song(SONG_2_ID, SONG_2_TITLE, SONG_2_ARTIST);
		List<Song> songs = new ArrayList<Song>(Arrays.asList(song1, song2));
		when(musicStoreRepository.findAllSongs()).thenReturn(songs);

		musicStoreController.allSongs();

		verify(musicStoreView).displayAllSongsInStore(songs);
	}

	@Test
	public void testAllPlaylists() {
		List<Playlist> playlists = Arrays.asList(new Playlist(PLAYLIST_1_NAME), new Playlist(PLAYLIST_2_NAME));
		when(musicStoreRepository.findAllPlaylists()).thenReturn(playlists);

		musicStoreController.allPlaylists();

		verify(musicStoreView).displayAllPlaylists(playlists);
	}

	@Test
	public void testCreatePlaylistWhenPlaylistDoesNotAlreadyExist() {
		Playlist playlistToCreate = new Playlist(PLAYLIST_1_NAME);
		when(musicStoreRepository.findPlaylistByName(PLAYLIST_1_NAME)).thenReturn(null);

		musicStoreController.createPlaylist(playlistToCreate);

		InOrder inOrder = inOrder(musicStoreRepository, musicStoreView);
		inOrder.verify(musicStoreRepository).createPlaylist(playlistToCreate);
		inOrder.verify(musicStoreView).displayPlaylist(playlistToCreate);
	}

	@Test
	public void testCreatePlaylistWhenPlaylistAlreadyExists() {
		Playlist existingPlaylist = new Playlist(PLAYLIST_1_NAME);
		Playlist playlistToCreate = new Playlist(PLAYLIST_1_NAME);
		when(musicStoreRepository.findPlaylistByName(PLAYLIST_1_NAME)).thenReturn(existingPlaylist);

		musicStoreController.createPlaylist(playlistToCreate);

		verify(musicStoreView).displayError(MusicStoreController.PLAYLIST_ALREADY_EXISTS_MSG + PLAYLIST_1_NAME);
		verifyNoMoreInteractions(ignoreStubs(musicStoreRepository));
	}

	@Test
	public void testDeletePlaylistWhenPlaylistExists() {
		Playlist existingPlaylist = new Playlist(PLAYLIST_1_NAME);
		Playlist playlistToDelete = new Playlist(PLAYLIST_1_NAME);
		when(musicStoreRepository.findPlaylistByName(PLAYLIST_1_NAME)).thenReturn(existingPlaylist);

		musicStoreController.deletePlaylist(playlistToDelete);

		InOrder inOrder = inOrder(musicStoreRepository, musicStoreView);
		inOrder.verify(musicStoreRepository).deletePlaylist(playlistToDelete);
		inOrder.verify(musicStoreView).hidePlaylist(playlistToDelete);
	}

	@Test
	public void testDeletePlaylistWhenPlaylistDoesNotExist() {
		Playlist playlistToDelete = new Playlist(PLAYLIST_1_NAME);
		when(musicStoreRepository.findPlaylistByName(PLAYLIST_1_NAME)).thenReturn(null);

		musicStoreController.deletePlaylist(playlistToDelete);

		verify(musicStoreView).displayError(MusicStoreController.PLAYLIST_NOT_FOUND_MSG + PLAYLIST_1_NAME);
		verifyNoMoreInteractions(ignoreStubs(musicStoreRepository));
	}

	@Test
	public void testAllSongsInPlaylist() {
		Song song1 = new Song(SONG_1_ID, SONG_1_TITLE, SONG_1_ARTIST);
		Song song2 = new Song(SONG_2_ID, SONG_2_TITLE, SONG_2_ARTIST);
		List<Song> songs = new ArrayList<Song>(Arrays.asList(song1, song2));
		Playlist playlist = new Playlist(PLAYLIST_1_NAME, songs);
		when(musicStoreRepository.findPlaylistByName(PLAYLIST_1_NAME)).thenReturn(playlist);

		musicStoreController.allSongsInPlaylist(playlist);

		verify(musicStoreView).displayAllSongsInPlaylist(songs);
	}

	@Test
	public void testAddSongInPlaylistWhenPlaylistDoesNotExist() {
		Playlist playlist = new Playlist(PLAYLIST_1_NAME);
		Song songToAdd = new Song(SONG_1_ID, SONG_1_TITLE, SONG_1_ARTIST);
		when(musicStoreRepository.findPlaylistByName(PLAYLIST_1_NAME)).thenReturn(null);
		when(musicStoreRepository.findSongById(SONG_1_ID)).thenReturn(songToAdd);

		musicStoreController.addSongInPlaylist(playlist, songToAdd);

		verify(musicStoreView).displayError(MusicStoreController.PLAYLIST_NOT_FOUND_MSG + PLAYLIST_1_NAME);
		verifyNoMoreInteractions(ignoreStubs(musicStoreRepository));
	}

	@Test
	public void testAddSongInPlaylistWhenSongDoesNotExist() {
		Playlist playlist = new Playlist(PLAYLIST_1_NAME);
		Song songToAdd = new Song(SONG_1_ID, SONG_1_TITLE, SONG_1_ARTIST);
		when(musicStoreRepository.findPlaylistByName(PLAYLIST_1_NAME)).thenReturn(playlist);
		when(musicStoreRepository.findSongById(SONG_1_ID)).thenReturn(null);

		musicStoreController.addSongInPlaylist(playlist, songToAdd);

		verify(musicStoreView)
			.displayError(MusicStoreController.SONG_NOT_FOUND_MSG + SONG_1_ARTIST + " - " + SONG_1_TITLE);
		verifyNoMoreInteractions(ignoreStubs(musicStoreRepository));
	}

	@Test
	public void testAddSongInPlaylistWhenSongAlreadyInPlaylist() {
		Song existingSong = new Song(SONG_1_ID, SONG_1_TITLE, SONG_1_ARTIST);
		List<Song> songs = new ArrayList<Song>(Arrays.asList(existingSong));
		Playlist playlist = new Playlist(PLAYLIST_1_NAME, songs);
		Song songToAdd = new Song(SONG_1_ID, SONG_1_TITLE, SONG_1_ARTIST);
		when(musicStoreRepository.findPlaylistByName(PLAYLIST_1_NAME)).thenReturn(playlist);
		when(musicStoreRepository.findSongById(SONG_1_ID)).thenReturn(songToAdd);

		musicStoreController.addSongInPlaylist(playlist, songToAdd);

		verify(musicStoreView)
			.displayError(MusicStoreController.SONG_ALREADY_IN_PLAYLIST_MSG + SONG_1_ARTIST + " - " + SONG_1_TITLE);
		verifyNoMoreInteractions(ignoreStubs(musicStoreRepository));
	}

	@Test
	public void testAddSongInPlaylistWhenSongDoesNotAlreadyInPlaylist() {
		Song existingSong = new Song(SONG_2_ID, SONG_2_TITLE, SONG_2_ARTIST);
		List<Song> songs = new ArrayList<Song>(Arrays.asList(existingSong));
		Playlist playlist = new Playlist(PLAYLIST_1_NAME, songs);
		Song songToAdd = new Song(SONG_1_ID, SONG_1_TITLE, SONG_1_ARTIST);
		when(musicStoreRepository.findPlaylistByName(PLAYLIST_1_NAME)).thenReturn(playlist);
		when(musicStoreRepository.findSongById(SONG_1_ID)).thenReturn(songToAdd);

		musicStoreController.addSongInPlaylist(playlist, songToAdd);

		assertThat(playlist.getSongs())
			.containsExactly(existingSong, songToAdd);
		InOrder inOrder = inOrder(musicStoreRepository, musicStoreView);
		inOrder.verify(musicStoreRepository).updatePlaylist(playlist);
		inOrder.verify(musicStoreView).displaySongInPlaylist(playlist, songToAdd);
	}

	@Test
	public void testRemoveSongFromPlaylistWhenPlaylistDoesNotExist() {
		Playlist playlist = new Playlist(PLAYLIST_1_NAME);
		Song songToRemove = new Song(SONG_1_ID, SONG_1_TITLE, SONG_1_ARTIST);
		when(musicStoreRepository.findPlaylistByName(PLAYLIST_1_NAME)).thenReturn(null);
		when(musicStoreRepository.findSongById(SONG_1_ID)).thenReturn(songToRemove);

		musicStoreController.removeSongFromPlaylist(playlist, songToRemove);

		verify(musicStoreView).displayError(MusicStoreController.PLAYLIST_NOT_FOUND_MSG + PLAYLIST_1_NAME);
		verifyNoMoreInteractions(ignoreStubs(musicStoreRepository));
	}

	@Test
	public void testRemoveSongFromPlaylistWhenSongDoesNotExist() {
		Playlist playlist = new Playlist(PLAYLIST_1_NAME);
		Song songToRemove = new Song(SONG_1_ID, SONG_1_TITLE, SONG_1_ARTIST);
		when(musicStoreRepository.findPlaylistByName(PLAYLIST_1_NAME)).thenReturn(playlist);
		when(musicStoreRepository.findSongById(SONG_1_ID)).thenReturn(null);

		musicStoreController.removeSongFromPlaylist(playlist, songToRemove);

		verify(musicStoreView)
			.displayError(MusicStoreController.SONG_NOT_FOUND_MSG + SONG_1_ARTIST + " - " + SONG_1_TITLE);
		verifyNoMoreInteractions(ignoreStubs(musicStoreRepository));
	}

	@Test
	public void testRemoveSongFromPlaylistWhenSongDoesNotExistInPlaylist() {
		Song existingSong = new Song(SONG_2_ID, SONG_2_TITLE, SONG_2_ARTIST);
		List<Song> songs = new ArrayList<Song>(Arrays.asList(existingSong));
		Playlist playlist = new Playlist(PLAYLIST_1_NAME, songs);
		Song songToRemove = new Song(SONG_1_ID, SONG_1_TITLE, SONG_1_ARTIST);
		when(musicStoreRepository.findPlaylistByName(PLAYLIST_1_NAME)).thenReturn(playlist);
		when(musicStoreRepository.findSongById(SONG_1_ID)).thenReturn(songToRemove);

		musicStoreController.removeSongFromPlaylist(playlist, songToRemove);

		verify(musicStoreView)
				.displayError(MusicStoreController.SONG_NOT_FOUND_IN_PLAYLIST_MSG + SONG_1_ARTIST + " - " + SONG_1_TITLE);
		verifyNoMoreInteractions(ignoreStubs(musicStoreRepository));
	}

	@Test
	public void testRemoveSongFromPlaylistWhenSongExistsInPlaylist() {
		Song existingSong = new Song(SONG_1_ID, SONG_1_TITLE, SONG_1_ARTIST);
		List<Song> songs = new ArrayList<Song>(Arrays.asList(existingSong));
		Playlist playlist = new Playlist(PLAYLIST_1_NAME, songs);
		Song songToRemove = new Song(SONG_1_ID, SONG_1_TITLE, SONG_1_ARTIST);
		when(musicStoreRepository.findPlaylistByName(PLAYLIST_1_NAME)).thenReturn(playlist);
		when(musicStoreRepository.findSongById(SONG_1_ID)).thenReturn(songToRemove);

		musicStoreController.removeSongFromPlaylist(playlist, songToRemove);

		assertThat(playlist.getSongs())
			.isEmpty();
		InOrder inOrder = inOrder(musicStoreRepository, musicStoreView);
		inOrder.verify(musicStoreRepository).updatePlaylist(playlist);
		inOrder.verify(musicStoreView).hideSongFromPlaylist(playlist, songToRemove);
	}
}
