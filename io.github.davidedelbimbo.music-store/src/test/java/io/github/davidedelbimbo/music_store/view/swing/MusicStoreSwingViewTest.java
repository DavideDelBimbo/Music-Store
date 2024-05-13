package io.github.davidedelbimbo.music_store.view.swing;

import static org.assertj.core.api.Assertions.*;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.DefaultComboBoxModel;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.*;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;

import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.davidedelbimbo.music_store.controller.MusicStoreController;
import io.github.davidedelbimbo.music_store.model.Playlist;
import io.github.davidedelbimbo.music_store.model.Song;

@RunWith(GUITestRunner.class)
public class MusicStoreSwingViewTest extends AssertJSwingJUnitTestCase {
	private static final String LBL_SELECT_PLAYLIST = "lblSelectPlaylist";
	private static final String COMBO_BOX_PLAYLISTS = "comboBoxPlaylists";
	private static final String BTN_CREATE_PLAYLIST = "btnCreatePlaylist";
	private static final String BTN_DELETE_PLAYLIST = "btnDeletePlaylist";
	private static final String LIST_SONGS_IN_STORE = "listSongsInStore";
	private static final String LIST_SONGS_IN_PLAYLIST = "listSongsInPlaylist";	
	private static final String BTN_ADD_TO_PLAYLIST = "btnAddToPlaylist";
	private static final String BTN_REMOVE_FROM_PLAYLIST = "btnRemoveFromPlaylist";
	private static final String LBL_ERROR_MESSAGE = "lblErrorMessage";

	private static final int SONG_1_ID = 1;
	private static final String SONG_1_TITLE = "Song1";
	private static final String SONG_1_ARTIST = "Artist1";
	private static final int SONG_2_ID = 2;
	private static final String SONG_2_NAME = "Song2";
	private static final String SONG_2_ARTIST = "Artist2";
	private static final String PLAYLIST_1_NAME = "Playlist1";
	private static final String PLAYLIST_2_NAME = "Playlist2";

	private FrameFixture window;

	private MusicStoreSwingView musicStoreSwingView;
	private MusicStoreController musicStoreController;

	@Override
	protected void onSetUp() {
		musicStoreController = mock(MusicStoreController.class);

		GuiActionRunner.execute(() -> {
			// Create the MusicStoreSwingView frame.
			this.musicStoreSwingView = new MusicStoreSwingView();
			this.musicStoreSwingView.setMusicStoreController(this.musicStoreController);
			return this.musicStoreSwingView;
		});

		// Shows the frame to test.
		window = new FrameFixture(robot(), this.musicStoreSwingView);
		window.show();
	}


	// Tests to verify GUI controls.
	@Test @GUITest
	public void testControlsInitialStates() {
		window.label(LBL_SELECT_PLAYLIST);
		window.comboBox(COMBO_BOX_PLAYLISTS).requireNoSelection();
		window.button(BTN_CREATE_PLAYLIST).requireEnabled();
		window.button(BTN_DELETE_PLAYLIST).requireDisabled();
		window.list(LIST_SONGS_IN_STORE);
		window.list(LIST_SONGS_IN_PLAYLIST).requireItemCount(0);
		window.button(BTN_ADD_TO_PLAYLIST).requireDisabled();
		window.button(BTN_REMOVE_FROM_PLAYLIST).requireDisabled();
		window.label(LBL_ERROR_MESSAGE).requireText(" ");
	}

	@Test @GUITest
	public void testUnselectingAPlaylistShouldClearThePlaylistList() {
		GuiActionRunner.execute(() -> 
			musicStoreSwingView.getListSongsInPlaylistModel().addElement(new Song(SONG_1_ID, SONG_1_TITLE, SONG_1_ARTIST)));

		window.comboBox(COMBO_BOX_PLAYLISTS).clearSelection();
		window.list(LIST_SONGS_IN_PLAYLIST).requireItemCount(0);
	}

	@Test @GUITest
	public void testDeletePlaylistButtonShouldBeEnabledWhenAPlaylistIsSelected() {
		GuiActionRunner.execute(() ->
			musicStoreSwingView.getComboBoxPlaylistsModel().addElement(new Playlist(PLAYLIST_1_NAME)));

		window.comboBox(COMBO_BOX_PLAYLISTS).selectItem(PLAYLIST_1_NAME);
		window.button(BTN_DELETE_PLAYLIST).requireEnabled();
	}

	@Test @GUITest
	public void testDeletePlaylistButtonShouldBeDisabledWhenNoPlaylistIsSelected() {
		GuiActionRunner.execute(() ->
			musicStoreSwingView.getComboBoxPlaylistsModel().addElement(new Playlist(PLAYLIST_1_NAME)));

		window.comboBox(COMBO_BOX_PLAYLISTS).clearSelection();
		window.button(BTN_DELETE_PLAYLIST).requireDisabled();
	}

	@Test @GUITest
	public void testAddToPlaylistButtonShouldBeEnabledOnlyWhenAPlaylistAndASongInStoreAreSelected() {
		GuiActionRunner.execute(() -> {
			musicStoreSwingView.getComboBoxPlaylistsModel().addElement(new Playlist(PLAYLIST_1_NAME));
			musicStoreSwingView.getListSongsInStoreModel().addElement(new Song(SONG_1_ID, SONG_1_TITLE, SONG_1_ARTIST));
		});

		window.comboBox(COMBO_BOX_PLAYLISTS).selectItem(PLAYLIST_1_NAME);
		window.list(LIST_SONGS_IN_STORE).selectItem(0);
		window.button(BTN_ADD_TO_PLAYLIST).requireEnabled();
	}

	@Test @GUITest
	public void testAddToPlaylistButtonShouldBeDisabledWhenNoPlaylistIsSelected() {
		GuiActionRunner.execute(() ->
			musicStoreSwingView.getComboBoxPlaylistsModel().addElement(new Playlist(PLAYLIST_1_NAME)));

		window.comboBox(COMBO_BOX_PLAYLISTS).clearSelection();
		window.button(BTN_ADD_TO_PLAYLIST).requireDisabled();
	}

	@Test @GUITest
	public void testAddToPlaylistButtonShouldBeDisabledWhenNoSongIsSelected() {
		GuiActionRunner.execute(() ->
			musicStoreSwingView.getListSongsInStoreModel().addElement(new Song(SONG_1_ID, SONG_1_TITLE, SONG_1_ARTIST)));

		window.list(LIST_SONGS_IN_STORE).clearSelection();
		window.button(BTN_ADD_TO_PLAYLIST).requireDisabled();
	}

	@Test @GUITest
	public void testAddToPlaylistButtonShouldBeDisabledWhenNoPlaylistAndNoSongAreSelected() {
		GuiActionRunner.execute(() -> {
			musicStoreSwingView.getComboBoxPlaylistsModel().addElement(new Playlist(PLAYLIST_1_NAME));
			musicStoreSwingView.getListSongsInStoreModel().addElement(new Song(SONG_1_ID, SONG_1_TITLE, SONG_1_ARTIST));
		});

		window.comboBox(COMBO_BOX_PLAYLISTS).clearSelection();
		window.list(LIST_SONGS_IN_STORE).clearSelection();
		window.button(BTN_ADD_TO_PLAYLIST).requireDisabled();
	}

	@Test @GUITest
	public void testRemoveFromPlaylistButtonShouldBeEnableOnlyWhenAPlaylistAndASongInPlaylistAreSelected() {
		GuiActionRunner.execute(() -> {
			musicStoreSwingView.getComboBoxPlaylistsModel().addElement(new Playlist(PLAYLIST_1_NAME));
			musicStoreSwingView.getListSongsInPlaylistModel().addElement(new Song(SONG_1_ID, SONG_1_TITLE, SONG_1_ARTIST));
		});

		window.comboBox(COMBO_BOX_PLAYLISTS).selectItem(PLAYLIST_1_NAME);
		window.list(LIST_SONGS_IN_PLAYLIST).selectItem(0);
		window.button(BTN_REMOVE_FROM_PLAYLIST).requireEnabled();
	}

	@Test @GUITest
	public void testRemoveFromPlaylistButtonShouldBeDisabledWhenNoPlaylistIsSelected() {
		GuiActionRunner.execute(() ->
			musicStoreSwingView.getComboBoxPlaylistsModel().addElement(new Playlist(PLAYLIST_1_NAME)));

		window.comboBox(COMBO_BOX_PLAYLISTS).clearSelection();
		window.button(BTN_REMOVE_FROM_PLAYLIST).requireDisabled();
	}

	@Test @GUITest
	public void testRemoveFromPlaylistButtonShouldBeDisabledWhenNoSongIsSelected() {
		GuiActionRunner.execute(() ->
			musicStoreSwingView.getListSongsInPlaylistModel().addElement(new Song(SONG_1_ID, SONG_1_TITLE, SONG_1_ARTIST)));

		window.list(LIST_SONGS_IN_PLAYLIST).clearSelection();
		window.button(BTN_REMOVE_FROM_PLAYLIST).requireDisabled();
	}

	@Test @GUITest
	public void testRemoveFromPlaylistButtonShouldBeDisabledWhenNoPlaylistAndNoSongAreSelected() {
		GuiActionRunner.execute(() -> {
			musicStoreSwingView.getComboBoxPlaylistsModel().addElement(new Playlist(PLAYLIST_1_NAME));
			musicStoreSwingView.getListSongsInPlaylistModel().addElement(new Song(SONG_1_ID, SONG_1_TITLE, SONG_1_ARTIST));
		});

		window.comboBox(COMBO_BOX_PLAYLISTS).clearSelection();
		window.list(LIST_SONGS_IN_PLAYLIST).clearSelection();
		window.button(BTN_REMOVE_FROM_PLAYLIST).requireDisabled();
	}

	@Test @GUITest
	public void testSelectingASongInStoreShouldUnselectTheSongInPlaylist() {
		GuiActionRunner.execute(() -> {
			musicStoreSwingView.getListSongsInStoreModel().addElement(new Song(SONG_1_ID, SONG_1_TITLE, SONG_1_ARTIST));
			musicStoreSwingView.getListSongsInPlaylistModel().addElement(new Song(SONG_1_ID, SONG_1_TITLE, SONG_1_ARTIST));
		});

		window.list(LIST_SONGS_IN_STORE).selectItem(0);
		window.list(LIST_SONGS_IN_PLAYLIST).selectItem(0);
		window.list(LIST_SONGS_IN_STORE).requireNoSelection();
	}

	@Test @GUITest
	public void testSelectingASongInPlaylistShouldUnselectTheSongInStore() {
		GuiActionRunner.execute(() -> {
			musicStoreSwingView.getListSongsInStoreModel().addElement(new Song(SONG_1_ID, SONG_1_TITLE, SONG_1_ARTIST));
			musicStoreSwingView.getListSongsInPlaylistModel().addElement(new Song(SONG_1_ID, SONG_1_TITLE, SONG_1_ARTIST));
		});

		window.list(LIST_SONGS_IN_PLAYLIST).selectItem(0);
		window.list(LIST_SONGS_IN_STORE).selectItem(0);
		window.list(LIST_SONGS_IN_PLAYLIST).requireNoSelection();
	}


	// Tests to verify GUI actions.
	@Test @GUITest
	public void testDisplayAllSongsInStoreShouldAddAllSongsToSongStoreList() {
		Song song1 = new Song(SONG_1_ID, SONG_1_TITLE, SONG_1_ARTIST);
		Song song2 = new Song(SONG_2_ID, SONG_2_NAME, SONG_2_ARTIST);

		GuiActionRunner.execute(() ->
			musicStoreSwingView.displayAllSongsInStore(Arrays.asList(song1, song2)));

		// Verify that songs are added to the store list.
		String[] listContents = window.list(LIST_SONGS_IN_STORE).contents();
		assertThat(listContents).containsExactly(song1.toString(), song2.toString());
	}

	@Test @GUITest
	public void testDisplayAllSongsInPlaylistShouldAddAllSongsToPlaylistList() {
		Song song1 = new Song(SONG_1_ID, SONG_1_TITLE, SONG_1_ARTIST);
		Song song2 = new Song(SONG_2_ID, SONG_2_NAME, SONG_2_ARTIST);

		GuiActionRunner.execute(() ->
			musicStoreSwingView.displayAllSongsInPlaylist(Arrays.asList(song1, song2)));

		// Verify that songs are added to the playlist list.
		String[] listContents = window.list(LIST_SONGS_IN_PLAYLIST).contents();
		assertThat(listContents).containsExactly(song1.toString(), song2.toString());
	}

	@Test @GUITest
	public void testDisplayAllPlaylistsShouldAddAllPlaylistsToComboBoxPlaylists() {
		Playlist playlist1 = new Playlist(PLAYLIST_1_NAME);
		Playlist playlist2 = new Playlist(PLAYLIST_2_NAME);

		GuiActionRunner.execute(() ->
			musicStoreSwingView.displayAllPlaylists(Arrays.asList(playlist1, playlist2)));

		// Verify that playlists are added to the combo box.
		String[] comboBoxContents = window.comboBox(COMBO_BOX_PLAYLISTS).contents();
		assertThat(comboBoxContents).containsExactly(playlist1.toString(), playlist2.toString());
	}

	@Test @GUITest
	public void testDisplayPlaylistShouldAddThePlaylistToComboBoxAndSelectThePlaylistAndResetTheErrorLabel() {
		Playlist existingPlaylist = new Playlist(PLAYLIST_1_NAME);
		Playlist playlistToAdd = new Playlist(PLAYLIST_2_NAME);
		GuiActionRunner.execute(() -> 
			musicStoreSwingView.getComboBoxPlaylistsModel().addElement(existingPlaylist));
		
		GuiActionRunner.execute(() ->
			musicStoreSwingView.displayPlaylist(playlistToAdd));

		// Verify that playlist is displayed in the combo box.
		String[] comboBoxContents = window.comboBox(COMBO_BOX_PLAYLISTS).contents();
		assertThat(comboBoxContents).containsExactly(existingPlaylist.toString(), playlistToAdd.toString());

		// Verify that playlist is selected in the combo box.
		window.comboBox(COMBO_BOX_PLAYLISTS).requireSelection(playlistToAdd.toString());

		// Verify that error message is reset.
		window.label(LBL_ERROR_MESSAGE).requireText(" ");
	}

	@Test @GUITest
	public void testHidePlaylistShouldRemoveThePlaylistFromComboBoxAndResetSelectedPlaylistAndResetTheErrorLabel() {
		Playlist playlistToHide = new Playlist(PLAYLIST_1_NAME);
		Playlist existingPlaylist = new Playlist(PLAYLIST_2_NAME);
		GuiActionRunner.execute(() -> {
			DefaultComboBoxModel<Playlist> comboBoxPlaylistsModel = musicStoreSwingView.getComboBoxPlaylistsModel();
			comboBoxPlaylistsModel.addElement(playlistToHide);
			comboBoxPlaylistsModel.addElement(existingPlaylist);
		});

		GuiActionRunner.execute(() ->
			musicStoreSwingView.hidePlaylist(new Playlist(PLAYLIST_1_NAME)));

		// Verify that playlist is removed from the combo box.
		String[] comboBoxContents = window.comboBox(COMBO_BOX_PLAYLISTS).contents();
		assertThat(comboBoxContents).containsExactly(existingPlaylist.toString());

		// Verify that no playlist is selected in the combo box.
		window.comboBox(COMBO_BOX_PLAYLISTS).requireNoSelection();

		// Verify that error message is reset.
		window.label(LBL_ERROR_MESSAGE).requireText(" ");
	}

	@Test @GUITest
	public void testDisplaySongInPlaylistShouldAddTheSongToPlaylistListAndResetTheErrorLabel() {
		Song existingSong = new Song(SONG_1_ID, SONG_1_TITLE, SONG_1_ARTIST);
		Song songToAdd = new Song(SONG_2_ID, SONG_2_NAME, SONG_2_ARTIST);
		GuiActionRunner.execute(() ->
			musicStoreSwingView.getListSongsInPlaylistModel().addElement(existingSong));

		GuiActionRunner.execute(() ->
			musicStoreSwingView.displaySongInPlaylist(songToAdd));

		// Verify that song is added to the playlist list.
		String[] listContents = window.list(LIST_SONGS_IN_PLAYLIST).contents();
		assertThat(listContents).containsExactly(existingSong.toString(), songToAdd.toString());

		// Verify that error message is reset.
		window.label(LBL_ERROR_MESSAGE).requireText(" ");
	}

	@Test @GUITest
	public void testHideSongFromPlaylistShouldRemoveTheSongFromPlaylistListAndResetTheErrorLabel() {
		Song songToHide = new Song(SONG_1_ID, SONG_1_TITLE, SONG_1_ARTIST);
		Song existingSong = new Song(SONG_2_ID, SONG_2_NAME, SONG_2_ARTIST);
		GuiActionRunner.execute(() -> {
			musicStoreSwingView.getListSongsInPlaylistModel().addElement(songToHide);
			musicStoreSwingView.getListSongsInPlaylistModel().addElement(existingSong);
		});

		GuiActionRunner.execute(() ->
			musicStoreSwingView.hideSongFromPlaylist(songToHide));

		// Verify that the song is removed from the playlist list.
		String[] listContents = window.list(LIST_SONGS_IN_PLAYLIST).contents();
		assertThat(listContents).containsExactly(existingSong.toString());

		// Verify that the error message is reset.
		window.label(LBL_ERROR_MESSAGE).requireText(" ");
	}

	@Test @GUITest
	public void testDisplayErrorShouldShowTheMessageInTheErrorLabel() {
		GuiActionRunner.execute(() ->
			musicStoreSwingView.displayError("Error message"));

		// Verify that the error message is displayed.
		window.label(LBL_ERROR_MESSAGE).requireText("Error message");
	}


	// Tests to verify GUI events.
	@Test @GUITest
	public void testSelectPlaylistFromComboBoxShouldDelegateToMusicStoreControllerAllSongsInPlaylist() {
		List<Song> songs = new ArrayList<>(Arrays.asList(new Song(SONG_1_ID, SONG_1_TITLE, SONG_1_ARTIST)));
		GuiActionRunner.execute(() ->
			musicStoreSwingView.getComboBoxPlaylistsModel().addElement(new Playlist(PLAYLIST_1_NAME, songs)));

		window.comboBox(COMBO_BOX_PLAYLISTS).selectItem(PLAYLIST_1_NAME);

		verify(musicStoreController).allSongsInPlaylist(new Playlist(PLAYLIST_1_NAME, songs));
	}

	@Test @GUITest
	public void testDeletePlaylistButtonShouldDelegateToMusicStoreControllerDeletePlaylist() {
		GuiActionRunner.execute(() ->
			musicStoreSwingView.getComboBoxPlaylistsModel().addElement(new Playlist(PLAYLIST_1_NAME)));

		window.comboBox(COMBO_BOX_PLAYLISTS).selectItem(PLAYLIST_1_NAME);
		window.button(BTN_DELETE_PLAYLIST).click();

		verify(musicStoreController).deletePlaylist(new Playlist(PLAYLIST_1_NAME));
	}

	@Test @GUITest
	public void testAddSongToPlaylistButtonShouldDelegateToMusicStoreControllerAddSongToPlaylist() {
		GuiActionRunner.execute(() -> {
			musicStoreSwingView.getComboBoxPlaylistsModel().addElement(new Playlist(PLAYLIST_1_NAME));
			musicStoreSwingView.getListSongsInStoreModel().addElement(new Song(SONG_1_ID, SONG_1_TITLE, SONG_1_ARTIST));
		});

		window.comboBox(COMBO_BOX_PLAYLISTS).selectItem(PLAYLIST_1_NAME);
		window.list(LIST_SONGS_IN_STORE).selectItem(0);
		window.button(BTN_ADD_TO_PLAYLIST).click();

		verify(musicStoreController).addSongToPlaylist(new Playlist(PLAYLIST_1_NAME), new Song(SONG_1_ID, SONG_1_TITLE, SONG_1_ARTIST));
	}

	@Test @GUITest
	public void testRemoveSongFromPlaylistButtonShouldDelegateToMusicStoreControllerRemoveSongFromPlaylist() {
		GuiActionRunner.execute(() -> {
			musicStoreSwingView.getComboBoxPlaylistsModel().addElement(new Playlist(PLAYLIST_1_NAME));
			musicStoreSwingView.getListSongsInPlaylistModel().addElement(new Song(SONG_1_ID, SONG_1_TITLE, SONG_1_ARTIST));
		});

		window.comboBox(COMBO_BOX_PLAYLISTS).selectItem(PLAYLIST_1_NAME);
		window.list(LIST_SONGS_IN_PLAYLIST).selectItem(0);
		window.button(BTN_REMOVE_FROM_PLAYLIST).click();

		verify(musicStoreController).removeSongFromPlaylist(new Playlist(PLAYLIST_1_NAME), new Song(SONG_1_ID, SONG_1_TITLE, SONG_1_ARTIST));
	}
}
