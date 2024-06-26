package io.github.davidedelbimbo.music_store.controller;

import static org.assertj.core.api.Assertions.*;

import static org.mockito.Mockito.*;
import org.mockito.InOrder;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import static io.github.davidedelbimbo.music_store.controller.MusicStoreController.*;

import io.github.davidedelbimbo.music_store.repository.MusicStoreRepository;
import io.github.davidedelbimbo.music_store.view.MusicStoreView;
import io.github.davidedelbimbo.music_store.model.Playlist;
import io.github.davidedelbimbo.music_store.model.Song;

public class MusicStoreControllerTest {
	private static String SONG_1_TITLE = "Song1";
	private static String SONG_1_ARTIST = "Artist1";
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
		Song song1 = new Song(SONG_1_TITLE, SONG_1_ARTIST);
		Song song2 = new Song(SONG_2_TITLE, SONG_2_ARTIST);
		when(musicStoreRepository.findAllSongs()).thenReturn(Arrays.asList(song1, song2));

		musicStoreController.allSongs();

		verify(musicStoreView).displayAllSongsInStore(Arrays.asList(song1, song2));
	}

	@Test
	public void testAllPlaylists() {
		Playlist playlist1 = new Playlist(PLAYLIST_1_NAME);
		Playlist playlist2 = new Playlist(PLAYLIST_2_NAME);
		when(musicStoreRepository.findAllPlaylists()).thenReturn(Arrays.asList(playlist1, playlist2));

		musicStoreController.allPlaylists();

		verify(musicStoreView).displayAllPlaylists(Arrays.asList(playlist1, playlist2));
	}

	@Test
	public void testCreatePlaylistWhenPlaylistDoesNotAlreadyExist() {
		when(musicStoreRepository.findPlaylistByName(PLAYLIST_1_NAME)).thenReturn(null);

		Playlist playlistToCreate = new Playlist(PLAYLIST_1_NAME);
		musicStoreController.createPlaylist(playlistToCreate);

		InOrder inOrder = inOrder(musicStoreRepository, musicStoreView);
		inOrder.verify(musicStoreRepository).createPlaylist(playlistToCreate);
		inOrder.verify(musicStoreView).displayPlaylist(playlistToCreate);
	}

	@Test
	public void testCreatePlaylistWhenPlaylistAlreadyExists() {
		Playlist existingPlaylist = new Playlist(PLAYLIST_1_NAME);
		when(musicStoreRepository.findPlaylistByName(PLAYLIST_1_NAME)).thenReturn(existingPlaylist);

		Playlist playlistToCreate = new Playlist(PLAYLIST_1_NAME);
		musicStoreController.createPlaylist(playlistToCreate);

		verify(musicStoreView).displayErrorAndAddPlaylist(PLAYLIST_ALREADY_EXISTS_MSG, existingPlaylist);
		verifyNoMoreInteractions(ignoreStubs(musicStoreRepository));
	}

	@Test
	public void testDeletePlaylistWhenPlaylistExists() {
		Playlist existingPlaylist = new Playlist(PLAYLIST_1_NAME);
		when(musicStoreRepository.findPlaylistByName(PLAYLIST_1_NAME)).thenReturn(existingPlaylist);

		Playlist playlistToDelete = new Playlist(PLAYLIST_1_NAME);
		musicStoreController.deletePlaylist(playlistToDelete);

		InOrder inOrder = inOrder(musicStoreRepository, musicStoreView);
		inOrder.verify(musicStoreRepository).deletePlaylist(playlistToDelete);
		inOrder.verify(musicStoreView).hidePlaylist(playlistToDelete);
	}

	@Test
	public void testDeletePlaylistWhenPlaylistDoesNotExist() {
		when(musicStoreRepository.findPlaylistByName(PLAYLIST_1_NAME)).thenReturn(null);

		Playlist playlistToDelete = new Playlist(PLAYLIST_1_NAME);
		musicStoreController.deletePlaylist(playlistToDelete);

		verify(musicStoreView).displayErrorAndRemovePlaylist(PLAYLIST_NOT_FOUND_MSG, playlistToDelete);
		verifyNoMoreInteractions(ignoreStubs(musicStoreRepository));
	}

	@Test
	public void testAllSongsInPlaylistWhenPlaylistExists() {
		Song song1 = new Song(SONG_1_TITLE, SONG_1_ARTIST);
		Song song2 = new Song(SONG_2_TITLE, SONG_2_ARTIST);
		Playlist playlist = new Playlist(PLAYLIST_1_NAME, Arrays.asList(song1, song2));
		when(musicStoreRepository.findPlaylistByName(PLAYLIST_1_NAME)).thenReturn(playlist);

		musicStoreController.allSongsInPlaylist(playlist);

		verify(musicStoreView).displayAllSongsInPlaylist(Arrays.asList(song1, song2));
	}

	@Test
	public void testAllSongsInPlaylistWhenPlaylistDoesNotExist() {
		Playlist playlist = new Playlist(PLAYLIST_1_NAME);
		when(musicStoreRepository.findPlaylistByName(PLAYLIST_1_NAME)).thenReturn(null);

		musicStoreController.allSongsInPlaylist(playlist);

		verify(musicStoreView).displayErrorAndRemovePlaylist(PLAYLIST_NOT_FOUND_MSG, playlist);
	}

	@Test
	public void testAddSongToPlaylistWhenSongDoesNotAlreadyInPlaylist() {
		Song existingSong = new Song(SONG_2_TITLE, SONG_2_ARTIST);
		Playlist playlist = new Playlist(PLAYLIST_1_NAME, Arrays.asList(existingSong));
		when(musicStoreRepository.findPlaylistByName(PLAYLIST_1_NAME)).thenReturn(playlist);

		Song songToAdd = new Song(SONG_1_TITLE, SONG_1_ARTIST);
		musicStoreController.addSongToPlaylist(playlist, songToAdd);

		InOrder inOrder = inOrder(musicStoreRepository, musicStoreView);
		inOrder.verify(musicStoreRepository).updatePlaylist(playlist);
		inOrder.verify(musicStoreView).displayAllSongsInPlaylist(Arrays.asList(existingSong, songToAdd));
		assertThat(playlist.getSongs()).containsExactly(existingSong, songToAdd);
}

	@Test
	public void testAddSongToPlaylistWhenPlaylistDoesNotExist() {
		Playlist playlist = new Playlist(PLAYLIST_1_NAME);
		when(musicStoreRepository.findPlaylistByName(PLAYLIST_1_NAME)).thenReturn(null);

		Song songToAdd = new Song(SONG_1_TITLE, SONG_1_ARTIST);
		musicStoreController.addSongToPlaylist(playlist, songToAdd);

		verify(musicStoreView).displayErrorAndRemovePlaylist(PLAYLIST_NOT_FOUND_MSG, playlist);
		verifyNoMoreInteractions(ignoreStubs(musicStoreRepository));
	}

	@Test
	public void testAddSongToPlaylistWhenSongAlreadyInPlaylist() {
		Song existingSong = new Song(SONG_1_TITLE, SONG_1_ARTIST);
		Playlist playlist = new Playlist(PLAYLIST_1_NAME, Arrays.asList(existingSong));
		when(musicStoreRepository.findPlaylistByName(PLAYLIST_1_NAME)).thenReturn(playlist);

		Song songToAdd = new Song(SONG_1_TITLE, SONG_1_ARTIST);
		musicStoreController.addSongToPlaylist(playlist, songToAdd);

		verify(musicStoreView).displayErrorAndUpdatePlaylist(SONG_ALREADY_IN_PLAYLIST_MSG, songToAdd, playlist);
		verifyNoMoreInteractions(ignoreStubs(musicStoreRepository));
		assertThat(playlist.getSongs()).containsExactly(existingSong);
}

	@Test
	public void testRemoveSongFromPlaylistWhenSongExistsInPlaylist() {
		Song existingSong = new Song(SONG_1_TITLE, SONG_1_ARTIST);
		Playlist playlist = new Playlist(PLAYLIST_1_NAME, Arrays.asList(existingSong));
		when(musicStoreRepository.findPlaylistByName(PLAYLIST_1_NAME)).thenReturn(playlist);

		Song songToRemove = new Song(SONG_1_TITLE, SONG_1_ARTIST);
		musicStoreController.removeSongFromPlaylist(playlist, songToRemove);

		InOrder inOrder = inOrder(musicStoreRepository, musicStoreView);
		inOrder.verify(musicStoreRepository).updatePlaylist(playlist);
		inOrder.verify(musicStoreView).displayAllSongsInPlaylist(Arrays.asList());
		assertThat(playlist.getSongs()).isEmpty();
	}

	@Test
	public void testRemoveSongFromPlaylistWhenPlaylistDoesNotExist() {
		Playlist playlist = new Playlist(PLAYLIST_1_NAME);
		when(musicStoreRepository.findPlaylistByName(PLAYLIST_1_NAME)).thenReturn(null);

		Song songToRemove = new Song(SONG_1_TITLE, SONG_1_ARTIST);
		musicStoreController.removeSongFromPlaylist(playlist, songToRemove);

		verify(musicStoreView).displayErrorAndRemovePlaylist(PLAYLIST_NOT_FOUND_MSG, playlist);
		verifyNoMoreInteractions(ignoreStubs(musicStoreRepository));
	}

	@Test
	public void testRemoveSongFromPlaylistWhenSongDoesNotExistInPlaylist() {
		Song existingSong = new Song(SONG_2_TITLE, SONG_2_ARTIST);
		Playlist playlist = new Playlist(PLAYLIST_1_NAME, Arrays.asList(existingSong));
		when(musicStoreRepository.findPlaylistByName(PLAYLIST_1_NAME)).thenReturn(playlist);

		Song songToRemove = new Song(SONG_1_TITLE, SONG_1_ARTIST);
		musicStoreController.removeSongFromPlaylist(playlist, songToRemove);

		verify(musicStoreView).displayErrorAndUpdatePlaylist(SONG_NOT_FOUND_IN_PLAYLIST_MSG, songToRemove, playlist);
		verifyNoMoreInteractions(ignoreStubs(musicStoreRepository));
		assertThat(playlist.getSongs()).containsExactly(existingSong);
	}
}
