package io.github.davidedelbimbo.music_store.view.swing;

import static org.assertj.core.api.Assertions.*;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;

import java.net.InetSocketAddress;

import java.util.Arrays;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.*;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;

import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import static io.github.davidedelbimbo.music_store.controller.MusicStoreController.*;
import static io.github.davidedelbimbo.music_store.repository.mongo.MusicStoreMongoRepository.*;
import static io.github.davidedelbimbo.music_store.view.swing.MusicStoreSwingView.*;
import static io.github.davidedelbimbo.music_store.view.swing.CreatePlaylistDialog.*;

import io.github.davidedelbimbo.music_store.controller.MusicStoreController;
import io.github.davidedelbimbo.music_store.repository.MusicStoreRepository;
import io.github.davidedelbimbo.music_store.repository.mongo.MusicStoreMongoRepository;
import io.github.davidedelbimbo.music_store.model.Playlist;
import io.github.davidedelbimbo.music_store.model.Song;

@RunWith(GUITestRunner.class)
public class MusicStoreViewIT extends AssertJSwingJUnitTestCase {
	private static final String SONG_1_TITLE = "Song1";
	private static final String SONG_1_ARTIST = "Artist1";
	private static final String SONG_2_TITLE = "Song2";
	private static final String SONG_2_ARTIST = "Artist2";
	private static final String PLAYLIST_1_NAME = "Playlist1";
	private static final String PLAYLIST_2_NAME = "Playlist2";

	private static MongoServer server;
	private static InetSocketAddress serverAddress;

	private MongoClient client;

	private FrameFixture window;
	private DialogFixture dialog;

	private MusicStoreSwingView musicStoreSwingView;
	private CreatePlaylistDialog createPlaylistDialog;
	private MusicStoreController musicStoreController;
	private MusicStoreRepository musicStoreRepository;

	@BeforeClass
	public static void setupServer() {
		server = new MongoServer(new MemoryBackend());

		// Bind on a random local port.
		serverAddress = server.bind();
	}

	@AfterClass
	public static void shutdownServer() {
		server.shutdown();
	}

	@Override
	protected void onSetUp() {
		client = new MongoClient(new ServerAddress(serverAddress));
		musicStoreRepository = new MusicStoreMongoRepository(client, STORE_DB_NAME, SONG_COLLECTION_NAME, PLAYLIST_COLLECTION_NAME);

		// Explicit initialization of database through the repository.
		musicStoreRepository.initializeSongs(Arrays.asList(new Song(SONG_1_TITLE, SONG_1_ARTIST), new Song(SONG_2_TITLE, SONG_2_ARTIST)));
		for (Playlist playlist : musicStoreRepository.findAllPlaylists()) {
			musicStoreRepository.deletePlaylist(playlist);
		}

		GuiActionRunner.execute(() -> {
			createPlaylistDialog = new CreatePlaylistDialog();
			musicStoreSwingView = new MusicStoreSwingView(createPlaylistDialog);
			musicStoreController = new MusicStoreController(musicStoreSwingView, musicStoreRepository);
			musicStoreSwingView.setMusicStoreController(musicStoreController);
			return musicStoreSwingView;
		});

		window = new FrameFixture(robot(), musicStoreSwingView);
		dialog = new DialogFixture(robot(), createPlaylistDialog);

		// Show the frame to test.
		window.show();
	}

	@Override
	public void onTearDown() {
		client.close();
	}

	@Test @GUITest
	public void testAllStudents() {
		Song song1 = new Song(SONG_1_TITLE, SONG_1_ARTIST);
		Song song2 = new Song(SONG_2_TITLE, SONG_2_ARTIST);

		GuiActionRunner.execute(() ->
			musicStoreController.allSongs());

		// Verify that store list is populated.
		assertThat(window.list(LIST_SONGS_IN_STORE).contents())
			.containsExactly(song1.toString(), song2.toString());
	}

	@Test @GUITest
	public void testAllPlaylists() {
		Playlist playlist1 = new Playlist(PLAYLIST_1_NAME);
		Playlist playlist2 = new Playlist(PLAYLIST_2_NAME);
		musicStoreRepository.createPlaylist(playlist1);
		musicStoreRepository.createPlaylist(playlist2);

		GuiActionRunner.execute(() ->
			musicStoreController.allPlaylists());

		// Verify that playlist combo box is populated.
		assertThat(window.comboBox(COMBO_BOX_PLAYLISTS).contents())
			.containsExactly(playlist1.toString(), playlist2.toString());
	}

	@Test @GUITest
	public void testCreateButtonSuccess() {
		// Simulate opening the Create Playlist dialog.
		dialog.show();

		dialog.textBox(TXT_PLAYLIST_NAME).enterText(PLAYLIST_1_NAME);
		dialog.button(BTN_CREATE_PLAYLIST_DIALOG).click();

		// Verify that the playlist is created.
		assertThat(window.comboBox(COMBO_BOX_PLAYLISTS).contents())
			.containsExactly(PLAYLIST_1_NAME);
	}

	@Test @GUITest
	public void testCreatePlaylistButtonFails() {
		// Simulate opening the Create Playlist dialog.
		dialog.show();

		Playlist existingPlaylist = new Playlist(PLAYLIST_1_NAME);
		musicStoreRepository.createPlaylist(existingPlaylist);

		dialog.textBox(TXT_PLAYLIST_NAME).enterText(PLAYLIST_1_NAME);
		dialog.button(BTN_CREATE_PLAYLIST_DIALOG).click();

		// Verify that playlist is not created.
		assertThat(window.comboBox(COMBO_BOX_PLAYLISTS).contents())
			.isEmpty();
		dialog.label(LBL_ERROR_MESSAGE_DIALOG)
			.requireText(PLAYLIST_ALREADY_EXISTS_MSG + existingPlaylist);
	}

	@Test @GUITest
	public void testDeleteButtonSuccess() {
		// Create a playlist and show it in the the view.
		Playlist playlistToDelete = new Playlist(PLAYLIST_1_NAME);
		musicStoreRepository.createPlaylist(playlistToDelete);
		GuiActionRunner.execute(() ->
			musicStoreSwingView.getComboBoxPlaylistsModel().addElement(playlistToDelete));

		window.comboBox(COMBO_BOX_PLAYLISTS).selectItem(0);
		window.button(BTN_DELETE_PLAYLIST).click();

		// Verify that playlist is deleted.
		assertThat(window.comboBox(COMBO_BOX_PLAYLISTS).contents())
			.isEmpty();
	}

	@Test @GUITest
	public void testDeleteButtonFails() {
		// Add a playlist to the view but not to the repository.
		Playlist playlistToDelete = new Playlist(PLAYLIST_1_NAME);
		GuiActionRunner.execute(() ->
			musicStoreSwingView.getComboBoxPlaylistsModel().addElement(playlistToDelete));

		window.comboBox(COMBO_BOX_PLAYLISTS).selectItem(0);
		window.button(BTN_DELETE_PLAYLIST).click();

		// Verify that playlist is not deleted.
		assertThat(window.comboBox(COMBO_BOX_PLAYLISTS).contents())
			.containsExactly(playlistToDelete.toString());
		window.label(LBL_ERROR_MESSAGE_VIEW).requireText(PLAYLIST_NOT_FOUND_MSG + playlistToDelete);
	}

	@Test @GUITest
	public void testAddSongToPlaylistButtonSuccess() {
		// Show the song in the view.
		Song songToAdd = new Song(SONG_1_TITLE, SONG_1_ARTIST);
		GuiActionRunner.execute(() ->
			musicStoreSwingView.getListSongsInStoreModel().addElement(songToAdd));

		// Create a playlist and show it in the view.
		Playlist playlistToUpdate = new Playlist(PLAYLIST_1_NAME);
		musicStoreRepository.createPlaylist(playlistToUpdate);
		GuiActionRunner.execute(() ->
			musicStoreSwingView.getComboBoxPlaylistsModel().addElement(playlistToUpdate));

		window.comboBox(COMBO_BOX_PLAYLISTS).selectItem(PLAYLIST_1_NAME);
		window.list(LIST_SONGS_IN_STORE).selectItem(0);
		window.button(BTN_ADD_TO_PLAYLIST).click();

		// Verify that song is added to playlist.
		assertThat(window.list(LIST_SONGS_IN_PLAYLIST).contents())
			.containsExactly(songToAdd.toString());
	}

	@Test @GUITest
	public void testAddSongToPlaylistButtonFails() {
		// Show the song in the view.
		Song songToAdd = new Song(SONG_1_TITLE, SONG_1_ARTIST);
		GuiActionRunner.execute(() ->
			musicStoreSwingView.getListSongsInStoreModel().addElement(songToAdd));

		// Add a playlist to the repository and show it in the view.
		Playlist playlistToUpdate = new Playlist(PLAYLIST_1_NAME, Arrays.asList(songToAdd));
		musicStoreRepository.createPlaylist(playlistToUpdate);
		GuiActionRunner.execute(() ->
			musicStoreSwingView.getComboBoxPlaylistsModel().addElement(playlistToUpdate));

		window.comboBox(COMBO_BOX_PLAYLISTS).selectItem(PLAYLIST_1_NAME);
		window.list(LIST_SONGS_IN_STORE).selectItem(0);
		window.button(BTN_ADD_TO_PLAYLIST).click();

		// Verify that song is not added to playlist.
		assertThat(window.list(LIST_SONGS_IN_PLAYLIST).contents())
			.containsExactly(songToAdd.toString());
		window.label(LBL_ERROR_MESSAGE_VIEW).requireText(SONG_ALREADY_IN_PLAYLIST_MSG + songToAdd);
	}

	@Test @GUITest
	public void testRemoveSongFromPlaylistButtonSuccess() {
		// Show the song in the view.
		Song songToRemove = new Song(SONG_1_TITLE, SONG_1_ARTIST);
		GuiActionRunner.execute(() -> 
			musicStoreSwingView.getListSongsInStoreModel().addElement(songToRemove));

		// Create a playlist and show it in the view.
		Playlist playlistToUpdate = new Playlist(PLAYLIST_1_NAME, Arrays.asList(songToRemove));
		musicStoreRepository.createPlaylist(playlistToUpdate);
		GuiActionRunner.execute(() ->
			musicStoreSwingView.getComboBoxPlaylistsModel().addElement(playlistToUpdate));

		window.comboBox(COMBO_BOX_PLAYLISTS).selectItem(PLAYLIST_1_NAME);
		window.list(LIST_SONGS_IN_PLAYLIST).selectItem(0);
		window.button(BTN_REMOVE_FROM_PLAYLIST).click();

		// Verify that song is removed from playlist.
		assertThat(window.list(LIST_SONGS_IN_PLAYLIST).contents()).isEmpty();
	}

	@Test @GUITest
	public void testRemoveSongFromPlaylistButtonFails() {
		// Create a playlist and show it in the view.
		Playlist playlistToUpdate = new Playlist(PLAYLIST_1_NAME);
		musicStoreRepository.createPlaylist(playlistToUpdate);
		GuiActionRunner.execute(() -> 
			musicStoreSwingView.getComboBoxPlaylistsModel().addElement(playlistToUpdate));

		window.comboBox(COMBO_BOX_PLAYLISTS).selectItem(PLAYLIST_1_NAME);

		// Show the song in the view.
		Song songToRemove = new Song(SONG_1_TITLE, SONG_1_ARTIST);
		GuiActionRunner.execute(() ->
			musicStoreSwingView.getListSongsInPlaylistModel().addElement(songToRemove));

		window.list(LIST_SONGS_IN_PLAYLIST).selectItem(0);
		window.button(BTN_REMOVE_FROM_PLAYLIST).click();

		// Verify that song is not removed from playlist.
		assertThat(window.list(LIST_SONGS_IN_PLAYLIST).contents())
			.containsExactly(songToRemove.toString());
		window.label(LBL_ERROR_MESSAGE_VIEW).requireText(SONG_NOT_FOUND_IN_PLAYLIST_MSG + songToRemove);
	}
}
