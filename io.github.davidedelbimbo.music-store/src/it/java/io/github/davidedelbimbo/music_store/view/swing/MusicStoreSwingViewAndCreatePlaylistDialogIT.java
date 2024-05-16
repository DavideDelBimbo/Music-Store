package io.github.davidedelbimbo.music_store.view.swing;

import static org.mockito.Mockito.*;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.*;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;

import org.junit.Test;
import org.junit.runner.RunWith;

import static io.github.davidedelbimbo.music_store.view.swing.MusicStoreSwingView.*;
import static io.github.davidedelbimbo.music_store.view.swing.CreatePlaylistDialog.*;

import io.github.davidedelbimbo.music_store.controller.MusicStoreController;
import io.github.davidedelbimbo.music_store.model.Playlist;

@RunWith(GUITestRunner.class)
public class MusicStoreSwingViewAndCreatePlaylistDialogIT extends AssertJSwingJUnitTestCase {
	private static final String PLAYLIST_NAME = "New Playlist";

	private FrameFixture window;
	private DialogFixture dialog;

	private MusicStoreSwingView musicStoreSwingView;
	private CreatePlaylistDialog createPlaylistDialog;
	private MusicStoreController musicStoreController;

	@Override
	public void onSetUp() {
		musicStoreController = mock(MusicStoreController.class);

		GuiActionRunner.execute(() -> {
			// Create the MusicStoreSwingView frame.
			createPlaylistDialog = new CreatePlaylistDialog();
			musicStoreSwingView = new MusicStoreSwingView(createPlaylistDialog);
			musicStoreSwingView.setMusicStoreController(musicStoreController);
			return musicStoreSwingView;
		});

		window = new FrameFixture(robot(), musicStoreSwingView);
		dialog = new DialogFixture(robot(), createPlaylistDialog);

		// Show the frame to test.
		window.show();
	}

	@Override
	protected void onTearDown() {
		if (dialog != null){
			dialog.cleanUp();
		}
	}

	@Test @GUITest
	public void testCreateButtonInViewShouldDisplayCreatePlaylistDialog() {
		window.button(BTN_CREATE_PLAYLIST_VIEW).click();

		// Check that the dialog is displayed.
		window.dialog().requireVisible();
	}

	@Test @GUITest
	public void testCreateButtonInDialogShouldAddPlaylistToPlaylistsComboBoxIfSuccess() {
		// Simulate opening the create playlist dialog.
		dialog.show();

		// Stub the createPlaylist method to add playlist to the playlists combo box.
		doAnswer(invocation -> {
			Playlist playlist = invocation.getArgument(0);
			musicStoreSwingView.getComboBoxPlaylistsModel().addElement(playlist);
			return null;
		}).when(musicStoreController).createPlaylist(any(Playlist.class));

		dialog.textBox(TXT_PLAYLIST_NAME).enterText(PLAYLIST_NAME);
		dialog.button(BTN_CREATE_PLAYLIST_DIALOG).click();

		// Verify that playlist is added to the playlists combo box in the view.
		window.comboBox(COMBO_BOX_PLAYLISTS).requireItemCount(1);
	}

	@Test @GUITest
	public void testCreateButtonShouldNotAddPlaylistToPlaylistsComboBoxIfFailure() {
		// Simulate opening the create playlist dialog.
		dialog.show();

		// Stub the createPlaylist method.
		doNothing().when(musicStoreController).createPlaylist(any(Playlist.class));

		dialog.textBox(TXT_PLAYLIST_NAME).enterText(PLAYLIST_NAME);
		dialog.button(BTN_CREATE_PLAYLIST_DIALOG).click();

		// Verify that playlist is not added to the playlists combo box in the view.
		window.comboBox(COMBO_BOX_PLAYLISTS).requireNoSelection();
	}
}
