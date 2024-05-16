package io.github.davidedelbimbo.music_store.bdd.steps;

import org.assertj.swing.fixture.FrameFixture;

import io.cucumber.java.After;

public class MusicStoreSwingViewSteps {
	private FrameFixture window;

	@After
	public void tearDown() {
		// Window might be null if the step for showing the view fails or it's not executed.
		if (window != null)
			window.cleanUp();
	}
}
