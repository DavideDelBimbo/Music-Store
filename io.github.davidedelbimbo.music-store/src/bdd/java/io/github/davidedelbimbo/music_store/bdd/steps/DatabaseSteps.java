package io.github.davidedelbimbo.music_store.bdd.steps;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import io.cucumber.java.Before;
import io.cucumber.java.After;

import static io.github.davidedelbimbo.music_store.bdd.MusicStoreSwingAppBDD.*;

public class DatabaseSteps {
	private MongoClient client;

	@Before
	public void setUp() {
		client = new MongoClient(new ServerAddress("localhost", MONGO_PORT));

		// Always start with an empty database.
		client.getDatabase(DB_NAME).drop();
	}

	@After
	public void tearDown() {
		client.close();
	}
}
