package io.github.davidedelbimbo.view.swing;

import static org.mockito.Mockito.*;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.*;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;

import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.davidedelbimbo.music_store.controller.MusicStoreController;
import io.github.davidedelbimbo.music_store.model.Playlist;
import io.github.davidedelbimbo.music_store.view.swing.CreatePlaylistDialog;
import io.github.davidedelbimbo.music_store.view.swing.MusicStoreSwingView;

@RunWith(GUITestRunner.class)
public class MusicStoreViewAndCreatePlaylistDialogIT extends AssertJSwingJUnitTestCase {
	private FrameFixture window;

	private MusicStoreSwingView musicStoreSwingView;
	private CreatePlaylistDialog createPlaylistDialog;
	private MusicStoreController musicStoreController;

	@Override
	public void onSetUp() {
		musicStoreController = mock(MusicStoreController.class);

		GuiActionRunner.execute(() -> {
			// Create the MusicStoreSwingView frame.
			this.musicStoreSwingView = new MusicStoreSwingView();
			this.musicStoreSwingView.setMusicStoreController(this.musicStoreController);
			return this.musicStoreSwingView;
		});

		window = new FrameFixture(robot(), this.musicStoreSwingView);
		window.show();
	}

	@Test @GUITest
	public void testCreatePlaylistDialogShouldBeDisplayedWhenCreatePlaylistButtonIsClicked() {
		window.button("btnCreatePlaylist").click();

		// Check that the Create Playlist dialog is displayed.
		window.dialog().requireVisible();
	}

	@Test
	public void testAddPlaylistFromDialogToComboBox() {
		// Simulate opening the create playlist dialog.
		GuiActionRunner.execute(() -> {
			this.createPlaylistDialog = new CreatePlaylistDialog();
			this.createPlaylistDialog.setMusicStoreController(this.musicStoreController);
			return this.createPlaylistDialog;
		});

		DialogFixture dialog = new DialogFixture(robot(), this.createPlaylistDialog);
		dialog.show();

		// Stub the createPlaylist method to add playlist to the playlists combo box.
		doAnswer(invocation -> {
			Playlist playlist = invocation.getArgument(0);
			musicStoreSwingView.getComboBoxPlaylistsModel().addElement(playlist);
			return null;
		}).when(musicStoreController).createPlaylist(any(Playlist.class));

		dialog.textBox("txtPlaylistName").enterText("New Playlist");
		dialog.button("btnCreatePlaylist").click();

		// Verify that playlist is added to the playlists combo box in the view.
		window.comboBox("comboBoxPlaylists").requireSelection("New Playlist");
	}
}
