package io.github.davidedelbimbo.music_store.controller;

import static org.mockito.Mockito.*;
import org.mockito.InOrder;

import static java.util.Arrays.*;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import io.github.davidedelbimbo.music_store.model.Playlist;
import io.github.davidedelbimbo.music_store.model.Song;
import io.github.davidedelbimbo.music_store.repository.MusicStoreRepository;
import io.github.davidedelbimbo.music_store.view.MusicStoreView;

public class MusicStoreControllerTest {
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
		List<Song> songs = asList(new Song("Song1", "Artist1"), new Song("Song2", "Artist2"));
		when(musicStoreRepository.findAllSongs()).thenReturn(songs);

		musicStoreController.allSongsInStore();

		verify(musicStoreView).displayAllSongsInStore(songs);
	}

	@Test
	public void testAllPlaylists() {
		List<Playlist> playlists = asList(
				new Playlist("Playlist1", new ArrayList<>()),
				new Playlist("Playlist2", new ArrayList<>())
			);
		when(musicStoreRepository.findAllPlaylists()).thenReturn(playlists);

		musicStoreController.allPlaylists();

		verify(musicStoreView).displayAllPlaylists(playlists);
	}

	@Test
	public void testAddNewPlaylistWhenPlaylistDoesNotAlreadyExists() {
		Playlist playlist = new Playlist("Playlist1", new ArrayList<>());
		when(musicStoreRepository.isPlaylistExists(playlist)).thenReturn(false);

		musicStoreController.addPlaylist(playlist);

		InOrder inOrder = inOrder(musicStoreRepository, musicStoreView);
		inOrder.verify(musicStoreRepository).addPlaylist(playlist);
		inOrder.verify(musicStoreView).displayPlaylist(playlist);
	}

	@Test
	public void testAddNewPlaylistWhenPlaylistAlreadyExists() {
		Playlist playlist = new Playlist("Playlist1", new ArrayList<>());
		when(musicStoreRepository.isPlaylistExists(playlist)).thenReturn(true);

		musicStoreController.addPlaylist(playlist);

		verify(musicStoreView).displayError("Playlist already exists: Playlist1");
		verifyNoMoreInteractions(ignoreStubs(musicStoreRepository));
	}

	@Test
	public void testRemovePlaylistWhenPlaylistExists() {
		Playlist playlist = new Playlist("Playlist1", new ArrayList<>());
		when(musicStoreRepository.isPlaylistExists(playlist)).thenReturn(true);

		musicStoreController.removePlaylist(playlist);

		InOrder inOrder = inOrder(musicStoreRepository, musicStoreView);
		inOrder.verify(musicStoreRepository).removePlaylist(playlist);
		inOrder.verify(musicStoreView).hidePlaylist(playlist);
	}

	@Test
	public void testRemovePlaylistWhenPlaylistDoesNotExist() {
		Playlist playlist = new Playlist("Playlist1", new ArrayList<>());
		when(musicStoreRepository.isPlaylistExists(playlist)).thenReturn(false);

		musicStoreController.removePlaylist(playlist);

		verify(musicStoreView).displayError("Playlist not found: Playlist1");
		verifyNoMoreInteractions(ignoreStubs(musicStoreRepository));
	}

	@Test
	public void testAllSongsInPlaylist() {
		List<Song> songs = asList(new Song("Song1", "Artist1"), new Song("Song2", "Artist2"));
		Playlist playlist = new Playlist("Playlist1", songs);
		when(musicStoreRepository.findAllSongsInPlaylist(playlist)).thenReturn(songs);

		musicStoreController.allSongsInPlaylist(playlist);

		verify(musicStoreView).displayAllSongsInPlaylist(songs);
	}

	@Test
	public void testAddSongInPlaylistWhenSongDoesNotAlreadyInPlaylist() {
		Song song = new Song("Song1", "Artist1");
		Playlist playlist = new Playlist("Playlist1", new ArrayList<>());
		when(musicStoreRepository.isSongInPlaylist(playlist, song)).thenReturn(false);

		musicStoreController.addSongInPlaylist(playlist, song);

		InOrder inOrder = inOrder(musicStoreRepository, musicStoreView);
		inOrder.verify(musicStoreRepository).addSongInPlaylist(playlist, song);
		inOrder.verify(musicStoreView).displaySongInPlaylist(playlist, song);
	}

	@Test
	public void testAddSongInPlaylistWhenSongAlreadyInPlaylist() {
		Song song = new Song("Song1", "Artist1");
		Playlist playlist = new Playlist("Playlist1", asList(song));
		when(musicStoreRepository.isSongInPlaylist(playlist, song)).thenReturn(true);

		musicStoreController.addSongInPlaylist(playlist, song);

		verify(musicStoreView).displayError("Song already in Playlist1: Artist1 - Song1");
		verifyNoMoreInteractions(ignoreStubs(musicStoreRepository));
	}

	@Test
	public void testRemoveSongFromPlaylistWhenSongExistsInPlaylist() {
		Song song = new Song("Song1", "Artist1");
		Playlist playlist = new Playlist("Playlist1", asList(song));
		when(musicStoreRepository.isSongInPlaylist(playlist, song)).thenReturn(true);

		musicStoreController.removeSongFromPlaylist(playlist, song);

		InOrder inOrder = inOrder(musicStoreRepository, musicStoreView);
		inOrder.verify(musicStoreRepository).removeSongFromPlaylist(playlist, song);
		inOrder.verify(musicStoreView).hideSongFromPlaylist(playlist, song);
	}
}
