package io.github.davidedelbimbo.music_store.view.swing;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.*;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;

import org.junit.Test;
import org.junit.runner.RunWith;

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
}
