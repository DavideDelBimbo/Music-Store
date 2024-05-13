package io.github.davidedelbimbo.music_store.controller;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import io.github.davidedelbimbo.music_store.model.Playlist;
import io.github.davidedelbimbo.music_store.model.Song;
import io.github.davidedelbimbo.music_store.repository.MusicStoreRepository;
import io.github.davidedelbimbo.music_store.repository.mongo.MusicStoreMongoRepository;
import io.github.davidedelbimbo.music_store.view.MusicStoreView;

/*
 * Run docker run -p 27017:27017 --rm mongo:6.0.14 to start a MongoDB container.
*/
public class MusicStoreControllerIT {
	static final Integer mongoPort = Integer.parseInt(System.getProperty("mongo.port", "27017"));

	private static final int SONG_1_ID = 1;
	private static final String SONG_1_TITLE = "Song1";
	private static final String SONG_1_ARTIST = "Artist1";
	private static final int SONG_2_ID = 2;
	private static final String SONG_2_TITLE = "Song2";
	private static final String SONG_2_ARTIST = "Artist2";
	private static final String PLAYLIST_NAME = "My Playlist";
	
	private MongoClient client;
	private MusicStoreController musicStoreController;
	private MusicStoreView musicStoreView;
	private MusicStoreRepository musicStoreRepository;

	@Before
	public void setUp() {
		musicStoreView = mock(MusicStoreView.class);
		client = new MongoClient(new ServerAddress("localhost", mongoPort));
		musicStoreRepository = new MusicStoreMongoRepository(client);

		// Explicit populate the database through the repository.
		Song song1 = new Song(SONG_1_ID, SONG_1_TITLE, SONG_1_ARTIST);
		Song song2 = new Song(SONG_2_ID, SONG_2_TITLE, SONG_2_ARTIST);
		List<Song> songs = new ArrayList<>(Arrays.asList(song1, song2));
		for (Song song : songs) {
			musicStoreRepository.addSong(song);
		}

		// Explicit empty the database through the repository.
		for (Playlist playlist : musicStoreRepository.findAllPlaylists()) {
			musicStoreRepository.deletePlaylist(playlist);
		}

		musicStoreController = new MusicStoreController(musicStoreView, musicStoreRepository);
	}

	@After
	public void tearDown() {
		// Explicit empty the database through the repository.
		for (Song song : musicStoreRepository.findAllSongs()) {
			musicStoreRepository.removeSong(song);
		}

		client.close();
	}

	@Test
	public void testAllSongs() {
		Song song1 = new Song(SONG_1_ID, SONG_1_TITLE, SONG_1_ARTIST);
		Song song2 = new Song(SONG_2_ID, SONG_2_TITLE, SONG_2_ARTIST);
		List<Song> songs = new ArrayList<>(Arrays.asList(song1, song2));

		musicStoreController.allSongs();

		verify(musicStoreView).displayAllSongsInStore(songs);
	}

	@Test
	public void testAllPlaylists() {
		Playlist playlist = new Playlist(PLAYLIST_NAME);
		musicStoreRepository.createPlaylist(playlist);

		musicStoreController.allPlaylists();

		verify(musicStoreView).displayAllPlaylists(Arrays.asList(playlist));
	}

	@Test
	public void testCreatePlaylist() {
		Playlist playlistToCreate = new Playlist(PLAYLIST_NAME);

		musicStoreController.createPlaylist(playlistToCreate);

		verify(musicStoreView).displayPlaylist(playlistToCreate);
	}

	@Test
	public void testDeletePlaylist() {
		Playlist playlistToDelete = new Playlist(PLAYLIST_NAME);
		musicStoreRepository.createPlaylist(playlistToDelete);

		musicStoreController.deletePlaylist(playlistToDelete);

		verify(musicStoreView).hidePlaylist(playlistToDelete);
	}

	@Test
	public void testAllSongsInPlaylist() {
		Song song1 = new Song(SONG_1_ID, SONG_1_TITLE, SONG_1_ARTIST);
		Song song2 = new Song(SONG_2_ID, SONG_2_TITLE, SONG_2_ARTIST);
		List<Song> songs = new ArrayList<>(Arrays.asList(song1, song2));
		Playlist playlist = new Playlist(PLAYLIST_NAME, songs);
		musicStoreRepository.createPlaylist(playlist);

		musicStoreController.allSongsInPlaylist(playlist);

		verify(musicStoreView).displayAllSongsInPlaylist(songs);
	}

	@Test
	public void testAddSongInPlaylist() {
		Song songToAdd = new Song(SONG_1_ID, SONG_1_TITLE, SONG_1_ARTIST);
		Playlist playlist = new Playlist(PLAYLIST_NAME);
		musicStoreRepository.createPlaylist(playlist);

		musicStoreController.addSongToPlaylist(playlist, songToAdd);

		verify(musicStoreView).displaySongInPlaylist(songToAdd);
	}

	@Test
	public void testRemoveSongFromPlaylist() {
		Song songToRemove = new Song(SONG_1_ID, SONG_1_TITLE, SONG_1_ARTIST);
		List<Song> songs = new ArrayList<>(Arrays.asList(songToRemove));
		Playlist playlist = new Playlist(PLAYLIST_NAME, songs);
		musicStoreRepository.createPlaylist(playlist);

		musicStoreController.removeSongFromPlaylist(playlist, songToRemove);

		verify(musicStoreView).hideSongFromPlaylist(songToRemove);
	}
}