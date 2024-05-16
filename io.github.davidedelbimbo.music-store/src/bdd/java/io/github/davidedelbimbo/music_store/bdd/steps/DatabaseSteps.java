package io.github.davidedelbimbo.music_store.bdd.steps;

import java.util.Arrays;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;

import static io.github.davidedelbimbo.music_store.bdd.MusicStoreSwingAppBDD.*;

public class DatabaseSteps {
	private MongoClient client;

	@Before
	public void setUp() {
		client = new MongoClient(new ServerAddress("localhost", MONGO_PORT));

		// Always start with an empty database.
		client.getDatabase(STORE_DB_NAME).drop();
	}

	@After
	public void tearDown() {
		client.close();
	}

	@Given("the database contains a few songs")
	public void the_database_contains_a_few_songs() {
		this.client.getDatabase(STORE_DB_NAME)
			.getCollection(SONG_COLLECTION_NAME)
			.insertMany(Arrays.asList(
				new Document().append("title", SONG_1_TITLE).append("artist", SONG_1_ARTIST),
				new Document().append("title", SONG_2_TITLE).append("artist", SONG_2_ARTIST),
				new Document().append("title", SONG_3_TITLE).append("artist", SONG_3_ARTIST)));
	}

	@Given("the database contains a playlist with a few songs in it")
	public void the_database_contains_a_playlist_with_a_few_songs_in_it() {
		this.client.getDatabase(STORE_DB_NAME)
			.getCollection(PLAYLIST_COLLECTION_NAME)
			.insertOne(new Document()
				.append("name", EXISTING_PLAYLIST_NAME)
				.append("songs", Arrays.asList(
					new Document().append("title", SONG_2_TITLE).append("artist", SONG_2_ARTIST),
					new Document().append("title", SONG_3_TITLE).append("artist", SONG_3_ARTIST))));
	}

	@Given("the playlist has meanwhile been added to the database")
	public void the_playlist_has_meanwhile_been_added_to_the_database() {
		this.client.getDatabase(STORE_DB_NAME)
		.getCollection(PLAYLIST_COLLECTION_NAME)
		.insertOne(new Document()
				.append("name", NEW_PLAYLIST_NAME)
				.append("songs", Arrays.asList()));
	}

	@Given("the playlist has meanwhile been removed from the database")
	public void the_playlist_has_meanwhile_been_removed_from_the_database() {
		this.client.getDatabase(STORE_DB_NAME)
			.getCollection(PLAYLIST_COLLECTION_NAME)
			.deleteOne(Filters.eq("name", EXISTING_PLAYLIST_NAME));
	}

	@Given("the song has meanwhile been added to the playlist database")
	public void the_song_has_meanwhile_been_added_to_the_playlist_database() {
		Document songDocument = new Document()
				.append("title", SONG_1_TITLE)
				.append("artist", SONG_1_ARTIST);

		this.client.getDatabase(STORE_DB_NAME)
			.getCollection(PLAYLIST_COLLECTION_NAME)
			.updateOne(Filters.eq("name", EXISTING_PLAYLIST_NAME), Updates.addToSet("songs", songDocument));
	}

	@Given("the song has meanwhile been removed from the playlist database")
	public void the_song_has_meanwhile_been_removed_from_the_playlist_database() {
		Document songDocument = new Document()
				.append("title", SONG_2_TITLE)
				.append("artist", SONG_2_ARTIST);

		this.client.getDatabase(STORE_DB_NAME)
			.getCollection(PLAYLIST_COLLECTION_NAME)
			.updateOne(Filters.eq("name", EXISTING_PLAYLIST_NAME), Updates.pull("songs", songDocument));
	}
}
