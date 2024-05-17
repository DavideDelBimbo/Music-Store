package io.github.davidedelbimbo.music_store.bdd.steps;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.swing.launcher.ApplicationLauncher.application;

import java.util.regex.Pattern;

import javax.swing.JFrame;

import org.assertj.swing.core.BasicRobot;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

import static io.github.davidedelbimbo.music_store.bdd.MusicStoreSwingAppBDD.*;

public class MusicStoreSwingViewSteps {
	private FrameFixture window;

	@After
	public void tearDown() {
		// Window might be null if the step for showing the view fails or it's not executed.
		if (window != null)
			window.cleanUp();
	}

	@Given("the Music Store view is shown")
	public void the_music_store_view_is_shown() {
		// Start the Swing application.
		application("io.github.davidedelbimbo.music_store.app.swing.MusicStoreSwingApp")
			.withArgs(
				"--mongo-port=" + MONGO_PORT,
				"--db-name=" + STORE_DB_NAME,
				"--song-collection=" + SONG_COLLECTION_NAME,
				"--playlist-collection=" + PLAYLIST_COLLECTION_NAME)
			.start();

		// Show the main window.
		window = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class) {
			@Override
			protected boolean isMatching(JFrame frame) {
				return "Music Store View".equals(frame.getTitle()) && frame.isShowing();
			}
		}).using(BasicRobot.robotWithCurrentAwtHierarchy());
	}

	@Given("the user wants to select a playlist from the drop-down list")
	public void the_user_wants_to_select_a_playlist_from_the_drop_down_list() {
		assertThat(window.comboBox().contents()).anySatisfy(e -> assertThat(e).contains(EXISTING_PLAYLIST_NAME));
	}

	@Given("the user clicks the {string} button")
	public void the_user_clicks_the_button(String buttonName) {
		window.button(JButtonMatcher.withText(buttonName)).click();
	}

	@Given("the dialog is open")
	public void the_dialog_is_open() {
		assertThat(window.dialog().requireVisible()).isNotNull();
	}

	@Given("the user provides a name in the text field of the dialog")
	public void the_user_provides_name_in_the_text_field_of_the_dialog() {
		window.dialog().textBox().enterText(NEW_PLAYLIST_NAME);
	}

	@Given("the user selects a playlist from the drop-down list")
	public void the_user_selects_a_playlist_from_the_drop_down_list() {
		window.comboBox().selectItem(Pattern.compile(".*" + EXISTING_PLAYLIST_NAME + ".*"));
	}

	@Given("the user selects a song from the store")
	public void the_user_selects_a_song_from_the_store() {
		window.list("listSongsInStore").selectItem(Pattern.compile(".*" + SONG_1_TITLE + ".*"));
	}

	@Given("the user selects a song from the playlist")
	public void the_user_selects_a_song_from_the_playlist() {
		window.list("listSongsInPlaylist").selectItem(Pattern.compile(".*" + SONG_2_TITLE + ".*"));
	}

	@When("the playlist is selected")
	public void the_playlist_is_selected() {
		window.comboBox().selectItem(Pattern.compile(".*" + EXISTING_PLAYLIST_NAME + ".*"));
	}
	
	@When("the user clicks the {string} button of the dialog")
	public void the_user_clicks_the_button_of_the_dialog(String buttonName) {
		window.dialog().button(JButtonMatcher.withText(buttonName)).click();
	}


	@Then("all songs in the playlist are shown")
	public void all_songs_in_the_playlist_are_shown() {
		assertThat(window.list("listSongsInPlaylist").contents()).anySatisfy(e -> assertThat(e).contains(SONG_2_TITLE));
	}

	@Then("the playlist is added to the drop-down list")
	public void the_playlist_is_added_to_the_drop_down_list() {
		assertThat(window.comboBox().contents()).anySatisfy(e -> assertThat(e).contains(NEW_PLAYLIST_NAME));
	}

	@Then("the playlist is removed from the drop-down list")
	public void the_playlist_is_removed_from_the_drop_down_list() {
		assertThat(window.comboBox().contents()).noneMatch(e -> e.contains(EXISTING_PLAYLIST_NAME));
	}

	@Then("all songs in the playlist are removed")
	public void all_songs_in_the_playlist_are_removed() {
		assertThat(window.list("listSongsInPlaylist").contents()).isEmpty();
	}

	@Then("an error is shown containing the name of the playlist")
	public void an_error_is_shown_containing_the_name_of_the_playlist() {
		assertThat(window.label("lblErrorMessage").text()).contains(EXISTING_PLAYLIST_NAME);
	}

	@Then("the song is added to the playlist")
	public void the_song_is_added_to_the_playlist() {
		assertThat(window.list("listSongsInPlaylist").contents()).anySatisfy(e -> assertThat(e).contains(SONG_1_TITLE));
	}

	@Then("an error is shown containing the name of the added song")
	public void an_error_is_shown_containing_the_name_of_the_added_song() {
		assertThat(window.label("lblErrorMessage").text()).contains(SONG_1_TITLE);
	}

	@Then("the song is removed from the playlist")
	public void the_song_is_removed_from_the_playlist() {
		assertThat(window.list("listSongsInPlaylist").contents()).noneMatch(e -> e.contains(SONG_2_TITLE));
	}

	@Then("an error is shown containing the name of the removed song")
	public void an_error_is_shown_containing_the_name_of_the_removed_song() {
		assertThat(window.label("lblErrorMessage").text()).contains(SONG_2_TITLE);
	}
}
