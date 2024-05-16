package io.github.davidedelbimbo.music_store.view.swing;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.*;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import static io.github.davidedelbimbo.music_store.repository.mongo.MusicStoreMongoRepository.*;
import static io.github.davidedelbimbo.music_store.view.swing.MusicStoreSwingView.*;
import static io.github.davidedelbimbo.music_store.view.swing.CreatePlaylistDialog.*;

import io.github.davidedelbimbo.music_store.controller.MusicStoreController;
import io.github.davidedelbimbo.music_store.repository.mongo.MusicStoreMongoRepository;
import io.github.davidedelbimbo.music_store.model.Playlist;
import io.github.davidedelbimbo.music_store.model.Song;

/*
 * Run docker run -p 27017:27017 --rm mongo:6.0.14 to start a MongoDB container.
*/
@RunWith(GUITestRunner.class)
public class ModelViewControllerIT extends AssertJSwingJUnitTestCase {
	private static final Integer mongoPort = Integer.parseInt(System.getProperty("mongo.port", "27017"));

	private static final String SONG_TITLE = "Song";
	private static final String SONG_ARTIST = "Artist";
	private static final String PLAYLIST_NAME = "My Playlist";

	MongoClient client;
	private FrameFixture window;
	private MusicStoreController musicStoreController;
	private MusicStoreMongoRepository musicStoreRepository;

	@Override
	protected void onSetUp() {
		client = new MongoClient(new ServerAddress("localhost", mongoPort));
		musicStoreRepository = new MusicStoreMongoRepository(client, STORE_DB_NAME, SONG_COLLECTION_NAME, PLAYLIST_COLLECTION_NAME);

		// Explicit initialization of database through the repository.
		musicStoreRepository.initializeSongs(Arrays.asList(new Song(SONG_TITLE, SONG_ARTIST)));
		for (Playlist playlist : musicStoreRepository.findAllPlaylists()) {
			musicStoreRepository.deletePlaylist(playlist);
		}

		window = new FrameFixture(robot(), GuiActionRunner.execute(() -> {
			CreatePlaylistDialog createPlaylistDialog = new CreatePlaylistDialog();
			MusicStoreSwingView musicStoreSwingView = new MusicStoreSwingView(createPlaylistDialog);
			musicStoreController = new MusicStoreController(musicStoreSwingView, musicStoreRepository);
			musicStoreSwingView.setMusicStoreController(musicStoreController);
			return musicStoreSwingView;
		}));

		// Show the frame to test.
		window.show();
	}

	@Override
	protected void onTearDown() {
		client.close();
	}

	@Test @GUITest
	public void testCreatePlaylist() {
		// Create a playlist using the UI.
		window.button(BTN_CREATE_PLAYLIST_VIEW).click();
		window.dialog().textBox(TXT_PLAYLIST_NAME).enterText(PLAYLIST_NAME);
		window.dialog().button(BTN_CREATE_PLAYLIST_DIALOG).click();

		// Verify that playlist is added to the database.
		assertThat(musicStoreRepository.findPlaylistByName(PLAYLIST_NAME))
			.isEqualTo(new Playlist(PLAYLIST_NAME));
	}

	@Test @GUITest
	public void testDeletePlaylist() {
		// Create a playlist needed for the test.
		musicStoreRepository.createPlaylist(new Playlist(PLAYLIST_NAME));

		// Use the controller to make appear playlists in the UI.
		GuiActionRunner.execute(() ->
			musicStoreController.allPlaylists());

		// Delete the playlist using the UI.
		window.comboBox(COMBO_BOX_PLAYLISTS).selectItem(PLAYLIST_NAME);
		window.button(BTN_DELETE_PLAYLIST).click();

		// Verify that playlist is deleted from the database.
		assertThat(musicStoreRepository.findPlaylistByName(PLAYLIST_NAME)).isNull();
	}

	@Test @GUITest
	public void testAddSongToPlaylist() {
		// Create a playlist needed for the test.
		musicStoreRepository.createPlaylist(new Playlist(PLAYLIST_NAME));

		// Use the controller to make appear songs and playlists in the UI.
		GuiActionRunner.execute(() -> {
			musicStoreController.allSongs();
			musicStoreController.allPlaylists();
		});

		// Add a song to the playlist using the UI.
		window.comboBox(COMBO_BOX_PLAYLISTS).selectItem(PLAYLIST_NAME);
		window.list(LIST_SONGS_IN_STORE).selectItem(0);
		window.button(BTN_ADD_TO_PLAYLIST).click();

		// Verify that song is added to the database.
		assertThat(musicStoreRepository.findPlaylistByName(PLAYLIST_NAME).getSongs())
				.containsExactly(new Song(SONG_TITLE, SONG_ARTIST));
	}

	@Test @GUITest
	public void testRemoveSongFromPlaylist() {
		// Create a playlist needed for the test.
		musicStoreRepository.createPlaylist(new Playlist(PLAYLIST_NAME, Arrays.asList(new Song(SONG_TITLE, SONG_ARTIST))));

		// Use the controller to make appear songs and playlists in the UI.
		GuiActionRunner.execute(() -> {
			musicStoreController.allSongs();
			musicStoreController.allPlaylists();
		});

		// Remove a song from the playlist using the UI.
		window.comboBox(COMBO_BOX_PLAYLISTS).selectItem(PLAYLIST_NAME);
		window.list(LIST_SONGS_IN_PLAYLIST).selectItem(0);
		window.button(BTN_REMOVE_FROM_PLAYLIST).click();

		// Verify that song is removed from the database.
		assertThat(musicStoreRepository.findPlaylistByName(PLAYLIST_NAME).getSongs())
			.isEmpty();
	}
}
