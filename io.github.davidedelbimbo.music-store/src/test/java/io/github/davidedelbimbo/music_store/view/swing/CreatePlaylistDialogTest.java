package io.github.davidedelbimbo.music_store.view.swing;

import static org.mockito.Mockito.*;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.*;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;

import org.junit.Test;
import org.junit.runner.RunWith;

import static io.github.davidedelbimbo.music_store.view.swing.CreatePlaylistDialog.*;
import io.github.davidedelbimbo.music_store.controller.MusicStoreController;
import io.github.davidedelbimbo.music_store.model.Playlist;

@RunWith(GUITestRunner.class)
public class CreatePlaylistDialogTest extends AssertJSwingJUnitTestCase {
	private static final String PLAYLIST_NAME = "My Playlist";

	private DialogFixture dialog;

	private CreatePlaylistDialog createPlaylistDialog;
	private MusicStoreController musicStoreController;

	@Override
	protected void onSetUp() {
		musicStoreController = mock(MusicStoreController.class);

		GuiActionRunner.execute(() -> {
			// Create the MusicStoreSwingView frame.
			createPlaylistDialog = new CreatePlaylistDialog();
			createPlaylistDialog.setMusicStoreController(musicStoreController);
			return createPlaylistDialog;
		});

		// Shows the frame to test.
		dialog = new DialogFixture(robot(), createPlaylistDialog);
		dialog.show();
	}

	@Override
	protected void onTearDown() {
		if (dialog != null){
			dialog.cleanUp();
		}
	}


	// Tests to verify GUI controls.
	@Test @GUITest
	public void testControlsInitialStates() {
		dialog.label(LBL_PLAYLIST_NAME);
		dialog.textBox(TXT_PLAYLIST_NAME).requireEnabled();
		dialog.button(BTN_CREATE_PLAYLIST_DIALOG).requireDisabled();
		dialog.button(BTN_CANCEL).requireEnabled();
		dialog.label(LBL_ERROR_MESSAGE_DIALOG).requireText(" ");
	}

	@Test @GUITest
	public void testResetControlsOnDialogOpen() {
		dialog.textBox(TXT_PLAYLIST_NAME).enterText(PLAYLIST_NAME);
		GuiActionRunner.execute(() ->
			createPlaylistDialog.getLblErrorMessage().setText("Error message"));
		dialog.button(BTN_CREATE_PLAYLIST_DIALOG).requireEnabled();

		// Close and reopen the dialog.
		dialog.close();
		dialog.show();

		// Verify that controls are reset.
		dialog.textBox(TXT_PLAYLIST_NAME).requireText("");
		dialog.button(BTN_CREATE_PLAYLIST_DIALOG).requireDisabled();
		dialog.label(LBL_ERROR_MESSAGE_DIALOG).requireText(" ");
	}

	@Test @GUITest
	public void testCreatePlaylistButtonShouldBeEnabledWhenNameTextBoxIsNotBlank() {
		dialog.textBox(TXT_PLAYLIST_NAME).enterText(PLAYLIST_NAME);
		dialog.button(BTN_CREATE_PLAYLIST_DIALOG).requireEnabled();
	}

	@Test @GUITest
	public void testCreatePlaylistButtonShouldBeDisabledWhenNameIsBlank() {
		dialog.textBox(TXT_PLAYLIST_NAME).enterText(" ");
		dialog.button(BTN_CREATE_PLAYLIST_DIALOG).requireDisabled();
	}


	//Tests to verify GUI actions.
	@Test @GUITest
	public void testDisplayErrorMessageShouldShowErrorMessage() {
		GuiActionRunner.execute(() ->
			createPlaylistDialog.displayErrorMessage("Error message"));

		// Verify that error message is displayed.
		dialog.label(LBL_ERROR_MESSAGE_DIALOG).requireText("Error message");
	}


	// Tests to verify GUI events.
	@Test @GUITest
	public void testCreatePlaylistButtonShouldDelegateToMusicStoreControllerCreatePlaylist() {
		dialog.textBox(TXT_PLAYLIST_NAME).enterText(PLAYLIST_NAME);
		dialog.button(BTN_CREATE_PLAYLIST_DIALOG).click();

		verify(musicStoreController).createPlaylist(new Playlist(PLAYLIST_NAME));
	}

	@Test @GUITest
	public void testCancelButtonShouldCloseTheDialog() {
		dialog.button(BTN_CANCEL).click();

		// Verify that dialog is closed.
		dialog.requireNotVisible();
	}
}
