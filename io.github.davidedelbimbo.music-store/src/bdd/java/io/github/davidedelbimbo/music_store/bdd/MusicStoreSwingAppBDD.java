package io.github.davidedelbimbo.music_store.bdd;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

/*
 * Run docker run -p 27017:27017 --rm mongo:6.0.14 to start a MongoDB container.
*/

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/bdd/resources", monochrome = true)
public class MusicStoreSwingAppBDD {
	public static final Integer MONGO_PORT = Integer.parseInt(System.getProperty("mongo.port", "27017"));

	public static final String DB_NAME = "test-db";
	public static final String SONG_COLLECTION_NAME = "test-songs-collection";
	public  static final String PLAYLIST_COLLECTION_NAME = "test.playlists-collection";

	public static Integer SONG_1_ID = 1;
	public static String SONG_1_TITLE = "Song1";
	public static String SONG_1_ARTIST = "Artist1";
	public static Integer SONG_2_ID = 2;
	public static String SONG_2_TITLE = "Song2";
	public static String SONG_2_ARTIST = "Artist2";
	public static Integer SONG_3_ID = 3;
	public static String SONG_3_TITLE = "Song3";
	public static String SONG_3_ARTIST = "Artist3";
	public static String EXISTING_PLAYLIST_NAME = "My Playlist";
	public static String NEW_PLAYLIST_NAME = "New Playlist";

	@BeforeClass
	public static void setUpOnce() {

	}
}
