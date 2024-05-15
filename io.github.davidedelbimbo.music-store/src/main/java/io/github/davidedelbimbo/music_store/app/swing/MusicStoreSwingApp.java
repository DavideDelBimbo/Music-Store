package io.github.davidedelbimbo.music_store.app.swing;

import java.awt.EventQueue;

import java.util.concurrent.Callable;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import io.github.davidedelbimbo.music_store.controller.MusicStoreController;
import io.github.davidedelbimbo.music_store.repository.mongo.MusicStoreMongoRepository;
import io.github.davidedelbimbo.music_store.view.swing.MusicStoreSwingView;
import io.github.davidedelbimbo.music_store.view.swing.CreatePlaylistDialog;
import io.github.davidedelbimbo.music_store.model.Song;

@Command(mixinStandardHelpOptions = true)
public class MusicStoreSwingApp implements Callable<Void> {
	private static final Logger LOGGER = LogManager.getLogger(MusicStoreSwingApp.class);

	@Option(names = { "--mongo-host" }, description = "MongoDB host address")
	private String mongoHost = "localhost";

	@Option(names = { "--mongo-port" }, description = "MongoDB host port")
	private Integer mongoPort = 27017;

	@Option(names = { "--db-name" }, description = "Database name")
	private String databaseName = "music_store";

	@Option(names = { "--song-collection" }, description = "Song collection name")
	private String songCollectionName = "songs";

	@Option(names = { "--playlist-collection" }, description = "Playlist collection name")
	private String playlistCollectionName = "playlists";

	public static void main(String[] args) {
		new CommandLine(new MusicStoreSwingApp()).execute(args);
	}

	@Override
	public Void call() throws Exception {
		EventQueue.invokeLater(() -> {
			try {
				MongoClient client = new MongoClient(new ServerAddress(mongoHost, mongoPort));
				client.getDatabase(databaseName).drop();
				MusicStoreMongoRepository musicStoreMongoRepository = new MusicStoreMongoRepository(client,
						databaseName, songCollectionName, playlistCollectionName);
				MusicStoreSwingView musicStoreSwingView = new MusicStoreSwingView(new CreatePlaylistDialog());
				MusicStoreController musicStoreController = new MusicStoreController(musicStoreSwingView,
						musicStoreMongoRepository);
				musicStoreSwingView.setMusicStoreController(musicStoreController);
				musicStoreSwingView.setVisible(true);

				initializeDatabase(musicStoreMongoRepository);
				musicStoreController.allSongs();
				musicStoreController.allPlaylists();
			} catch (Exception e) {
				LOGGER.log(Level.ERROR, "Exception", e);
			}
		});
		return null;
	}

	private void initializeDatabase(MusicStoreMongoRepository musicStoreMongoRepository) {
		musicStoreMongoRepository.addSong(new Song(1, "Song1", "Artist1"));
		musicStoreMongoRepository.addSong(new Song(2, "Song2", "Artist2"));
		musicStoreMongoRepository.addSong(new Song(3, "Song3", "Artist3"));
		
	}
}
