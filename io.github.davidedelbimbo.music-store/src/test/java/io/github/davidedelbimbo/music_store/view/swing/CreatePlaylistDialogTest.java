package io.github.davidedelbimbo.music_store.view.swing;

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

@RunWith(GUITestRunner.class)
public class CreatePlaylistDialogTest extends AssertJSwingJUnitTestCase {
	private static final String LBL_NAME = "lblName";
	private static final String TXT_NAME = "txtName";	
	private static final String BTN_CREATE_PLAYLIST = "btnCreatePlaylist";
	private static final String BTN_CANCEL = "btnCancel";
	private static final String LBL_ERROR_MESSAGE = "lblErrorMessage";

	private static final String PLAYLIST_NAME = "My Playlist";

	private DialogFixture dialog;

	private CreatePlaylistDialog createPlaylistDialog;
	private MusicStoreController musicStoreController;

	@Override
	protected void onSetUp() {
		this.musicStoreController = mock(MusicStoreController.class);

		GuiActionRunner.execute(() -> {
			// Create the MusicStoreSwingView frame.
			this.createPlaylistDialog = new CreatePlaylistDialog();
			this.createPlaylistDialog.setMusicStoreController(this.musicStoreController);
			return this.createPlaylistDialog;
		});

		// Shows the frame to test.
		dialog = new DialogFixture(robot(), this.createPlaylistDialog);
		dialog.show();
	}


	// Tests to verify GUI controls.
	@Test @GUITest
	public void testControlsInitialStates() {
		dialog.label(LBL_NAME);
		dialog.textBox(TXT_NAME).requireEnabled();
		dialog.button(BTN_CREATE_PLAYLIST).requireDisabled();
		dialog.button(BTN_CANCEL).requireEnabled();
		dialog.label(LBL_ERROR_MESSAGE).requireText(" ");
	}

	@Test @GUITest
	public void testCreatePlaylistButtonShouldBeEnabledWhenNameTextBoxIsNotBlank() {
		dialog.textBox(TXT_NAME).enterText(PLAYLIST_NAME);
		dialog.button(BTN_CREATE_PLAYLIST).requireEnabled();
	}

	@Test @GUITest
	public void testCreatePlaylistButtonShouldBeDisabledWhenNameIsBlank() {
		dialog.textBox(TXT_NAME).enterText(" ");
		dialog.button(BTN_CREATE_PLAYLIST).requireDisabled();
	}


	// Tests to verify GUI actions.
	@Test @GUITest
	public void testDisplayErrorShouldShowTheMessageInTheErrorLabel() {
		GuiActionRunner.execute(() ->
			createPlaylistDialog.displayError("Error message"));

		dialog.label(LBL_ERROR_MESSAGE).requireText("Error message");
	}


	// Tests to verify GUI events.
	@Test @GUITest
	public void testCreatePlaylistButtonShouldDelegateToMusicStoreControllerCreatePlaylistAndCloseTheDialog() {
		dialog.textBox(TXT_NAME).enterText(PLAYLIST_NAME);
		dialog.button(BTN_CREATE_PLAYLIST).click();

		verify(musicStoreController).createPlaylist(new Playlist(PLAYLIST_NAME));
		dialog.requireNotVisible();
	}

	@Test @GUITest
	public void testCancelButtonShouldCloseTheDialog() {
		dialog.button(BTN_CANCEL).click();
		dialog.requireNotVisible();
	}
}
