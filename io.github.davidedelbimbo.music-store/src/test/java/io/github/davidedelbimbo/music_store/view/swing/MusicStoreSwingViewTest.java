package io.github.davidedelbimbo.music_store.view.swing;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.*;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;

import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.davidedelbimbo.music_store.model.Playlist;
import io.github.davidedelbimbo.music_store.model.Song;

@RunWith(GUITestRunner.class)
public class MusicStoreSwingViewTest extends AssertJSwingJUnitTestCase {
	private FrameFixture window;

	private MusicStoreSwingView musicStoreSwingView;

	@Override
	protected void onSetUp() {
		GuiActionRunner.execute(() -> {
			// Create the MusicStoreSwingView frame.
			this.musicStoreSwingView = new MusicStoreSwingView();
			return this.musicStoreSwingView;
		});

		// Shows the frame to test.
		window = new FrameFixture(robot(), this.musicStoreSwingView);
		window.show();
	}

	@Test @GUITest
	public void testControlsInitialStates() {
		window.label("lblSelectPlaylist").requireText("Select a playlist");
		window.comboBox("comboBoxPlaylists").requireEnabled().requireNoSelection();
		window.button("btnCreatePlaylist").requireEnabled();
		window.button("btnDeletePlaylist").requireDisabled();
		window.list("listSongsInStore").requireItemCount(0).requireEnabled();
		window.list("listSongsInPlaylist").requireItemCount(0).requireEnabled();
		window.button("btnAddToPlaylist").requireDisabled();
		window.button("btnRemoveFromPlaylist").requireDisabled();
		window.label("lblErrorMessage").requireText(" ");
	}

	@Test @GUITest
	public void testDeletePlaylistButtonShouldBeEnableOnlyWhenAPlaylistIsSelected() {
		GuiActionRunner.execute(() -> 
			musicStoreSwingView.getComboBoxPlaylistsModel().addElement(new Playlist("Playlist1")));

		// Verify that the delete button is enabled when a playlist is selected.
		window.comboBox("comboBoxPlaylists").selectItem("Playlist1");
		window.button("btnDeletePlaylist").requireEnabled();

		// Verify that the delete button is disabled when no playlist is selected.
		window.comboBox("comboBoxPlaylists").clearSelection();
		window.button("btnDeletePlaylist").requireDisabled();
	}

	@Test @GUITest
	public void testAddToPlaylistButtonShouldBeEnableOnlyWhenAPlaylistAndASongInStoreAreSelected() {
		GuiActionRunner.execute(() -> {
			musicStoreSwingView.getComboBoxPlaylistsModel().addElement(new Playlist("Playlist1"));
			musicStoreSwingView.getListSongsInStoreModel().addElement(new Song(1, "Song1", "Artist1"));
		});

		// Verify that the add button is enabled when a song and a playlist are selected.
		window.comboBox("comboBoxPlaylists").selectItem("Playlist1");
		window.list("listSongsInStore").selectItem(0);
		window.button("btnAddToPlaylist").requireEnabled();

		// Verify that the add button is disabled when no playlist is selected.
		window.comboBox("comboBoxPlaylists").clearSelection();
		window.list("listSongsInStore").selectItem(0);
		window.button("btnAddToPlaylist").requireDisabled();

		// Verify that the add button is disabled when no song is selected.
		window.comboBox("comboBoxPlaylists").selectItem("Playlist1");
		window.list("listSongsInStore").clearSelection();
		window.button("btnAddToPlaylist").requireDisabled();

		// Verify that the add button is disabled when no song and no playlist are selected.
		window.comboBox("comboBoxPlaylists").clearSelection();
		window.list("listSongsInStore").clearSelection();
		window.button("btnAddToPlaylist").requireDisabled();
	}

	@Test @GUITest
	public void testRemoveFromPlaylistButtonShouldBeEnableOnlyWhenAPlaylistAndASongInPlaylistAreSelected() {
		GuiActionRunner.execute(() -> {
			musicStoreSwingView.getComboBoxPlaylistsModel().addElement(new Playlist("Playlist1"));
			musicStoreSwingView.getListSongsInPlaylistModel().addElement(new Song(1, "Song1", "Artist1"));
		});

		// Verify that the remove button is enabled when a song and a playlist are selected.
		window.comboBox("comboBoxPlaylists").selectItem("Playlist1");
		window.list("listSongsInPlaylist").selectItem(0);
		window.button("btnRemoveFromPlaylist").requireEnabled();

		// Verify that the remove button is disabled when no playlist is selected.
		window.comboBox("comboBoxPlaylists").clearSelection();
		window.list("listSongsInPlaylist").selectItem(0);
		window.button("btnRemoveFromPlaylist").requireDisabled();

		// Verify that the remove button is disabled when no song is selected.
		window.comboBox("comboBoxPlaylists").selectItem("Playlist1");
		window.list("listSongsInPlaylist").clearSelection();
		window.button("btnRemoveFromPlaylist").requireDisabled();

		// Verify that the remove button is disabled when no song and no playlist are selected.
		window.comboBox("comboBoxPlaylists").clearSelection();
		window.list("listSongsInPlaylist").clearSelection();
		window.button("btnRemoveFromPlaylist").requireDisabled();
	}

	@Test @GUITest
	public void testVerifyNoSimultaneousSelectionInStoreAndPlaylistLists() {
		GuiActionRunner.execute(() -> {
			musicStoreSwingView.getListSongsInStoreModel().addElement(new Song(1, "Song1", "Artist1"));
			musicStoreSwingView.getListSongsInPlaylistModel().addElement(new Song(1, "Song1", "Artist1"));
		});

		// Verify that no song is selected in the store list.
		window.list("listSongsInStore").selectItem(0);
		window.list("listSongsInPlaylist").selectItem(0);
		window.list("listSongsInStore").requireNoSelection();

		// Verify that no song is selected in the playlist list.
		window.list("listSongsInPlaylist").selectItem(0);
		window.list("listSongsInStore").selectItem(0);
		window.list("listSongsInPlaylist").requireNoSelection();
	}
}
