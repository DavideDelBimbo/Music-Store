package io.github.davidedelbimbo.music_store.app.swing;

import java.awt.EventQueue;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import io.github.davidedelbimbo.music_store.controller.MusicStoreController;
import io.github.davidedelbimbo.music_store.repository.mongo.MusicStoreMongoRepository;
import io.github.davidedelbimbo.music_store.view.swing.MusicStoreSwingView;
import io.github.davidedelbimbo.music_store.view.swing.CreatePlaylistDialog;

public class MusicStoreSwingApp {
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				String mongoHost = "localhost";
				int mongoPort = 27017;
				if (args.length > 0)
					mongoHost = args[0];
				if (args.length > 1)
					mongoPort = Integer.parseInt(args[1]);
				MusicStoreMongoRepository musicStoreRepository = new MusicStoreMongoRepository(new MongoClient(new ServerAddress(mongoHost, mongoPort)), "music_store", "songs", "playlists");
				MusicStoreSwingView musicStoreSwingView = new MusicStoreSwingView(new CreatePlaylistDialog());
				MusicStoreController musicStoreController = new MusicStoreController(musicStoreSwingView, musicStoreRepository);
				musicStoreSwingView.setMusicStoreController(musicStoreController);
				musicStoreSwingView.setVisible(true);
				musicStoreController.allSongs();
				musicStoreController.allPlaylists();
				} catch (Exception e) {
					e.printStackTrace();
					}
			});
		}
}
