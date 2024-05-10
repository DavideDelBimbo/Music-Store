package io.github.davidedelbimbo.music_store.view.swing;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.*;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;

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
}
