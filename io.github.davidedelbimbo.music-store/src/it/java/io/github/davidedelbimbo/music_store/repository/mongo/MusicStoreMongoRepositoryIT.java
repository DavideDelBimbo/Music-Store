package io.github.davidedelbimbo.music_store.repository.mongo;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import static io.github.davidedelbimbo.music_store.repository.mongo.MusicStoreMongoRepository.*;

import io.github.davidedelbimbo.music_store.model.Playlist;
import io.github.davidedelbimbo.music_store.model.Song;

/*
 * Run docker run -p 27017:27017 --rm mongo:6.0.14 to start a MongoDB container.
*/
public class MusicStoreMongoRepositoryIT {
	private static final Integer mongoPort = Integer.parseInt(System.getProperty("mongo.port", "27017"));

	private static Integer SONG_1_ID = 1;
	private static String SONG_1_TITLE = "Song1";
	private static String SONG_1_ARTIST = "Artist1";
	private static Integer SONG_2_ID = 2;
	private static String SONG_2_TITLE = "Song2";
	private static String SONG_2_ARTIST = "Artist2";
	private static String PLAYLIST_1_NAME = "Playlist1";
	private static String PLAYLIST_2_NAME = "Playlist2";

	MongoClient client;
	private MusicStoreMongoRepository musicStoreRepository;
	private MongoCollection<Document> songCollection;
	private MongoCollection<Document> playlistCollection;

	@Before
	public void setUp() {
		client = new MongoClient(new ServerAddress("localhost", mongoPort));
		musicStoreRepository = new MusicStoreMongoRepository(client, STORE_DB_NAME, SONG_COLLECTION_NAME, PLAYLIST_COLLECTION_NAME);

		// Start with a clean database.
		MongoDatabase database = client.getDatabase(STORE_DB_NAME);
		database.drop();

		// Get the song collection.
		songCollection = database.getCollection(SONG_COLLECTION_NAME);

		// Get the playlist collection.
		playlistCollection = database.getCollection(PLAYLIST_COLLECTION_NAME);
	}

	@After
	public void tearDown() {
		client.close();
	}

	@Test
	public void testFindAllSongsWhenDatabaseIsEmpty() {
		assertThat(musicStoreRepository.findAllSongs())
			.isEmpty();
	}

	@Test
	public void testFindAllSongs() {
		Song song1 = new Song(SONG_1_ID, SONG_1_TITLE, SONG_1_ARTIST);
		addTestSongToDatabase(song1);

		Song song2 = new Song(SONG_2_ID, SONG_2_TITLE, SONG_2_ARTIST);
		addTestSongToDatabase(song2);

		assertThat(musicStoreRepository.findAllSongs())
			.containsExactly(song1, song2);
	}

	@Test
	public void testFindSongByIdWhenSongDoesNotExist() {
		assertThat(musicStoreRepository.findSongById(SONG_1_ID))
			.isNull();
	}

	@Test
	public void testFindSongByIdWhenSongExists() {
		Song song = new Song(SONG_1_ID, SONG_1_TITLE, SONG_1_ARTIST);
		addTestSongToDatabase(song);

		Song songToFind = new Song(SONG_2_ID, SONG_2_TITLE, SONG_2_ARTIST);
		addTestSongToDatabase(songToFind);

		assertThat(musicStoreRepository.findSongById(SONG_2_ID))
			.isEqualTo(songToFind);
	}

	@Test
	public void testAddSong() {
		Song songToAdd = new Song(SONG_1_ID, SONG_1_TITLE, SONG_1_ARTIST);

		musicStoreRepository.addSong(songToAdd);

		assertThat(readAllSongsFromDatabase())
			.containsExactly(songToAdd);
	}

	@Test
	public void testRemoveSong() {
		Song songToRemove = new Song(SONG_1_ID, SONG_1_TITLE, SONG_1_ARTIST);
		addTestSongToDatabase(songToRemove);

		musicStoreRepository.removeSong(songToRemove);

		assertThat(readAllSongsFromDatabase())
			.isEmpty();
	}

	@Test
	public void testFindAllPlaylistsWhenDatabaseIsEmpty() {
		assertThat(musicStoreRepository.findAllPlaylists())
			.isEmpty();
	}

	@Test
	public void testFindAllPlaylists() {
		Playlist playlist1 = new Playlist(PLAYLIST_1_NAME, Arrays.asList(new Song(SONG_1_ID, SONG_1_TITLE, SONG_1_ARTIST)));
		addTestPlaylistToDatabase(playlist1);

		Playlist playlist2 = new Playlist(PLAYLIST_2_NAME);
		addTestPlaylistToDatabase(playlist2);

		assertThat(musicStoreRepository.findAllPlaylists())
			.containsExactly(playlist1, playlist2);
		assertThat(musicStoreRepository.findAllPlaylists().get(0).getSongs())
			.containsExactly(new Song(SONG_1_ID, SONG_1_TITLE, SONG_1_ARTIST));
		assertThat(musicStoreRepository.findAllPlaylists().get(1).getSongs())
			.isEmpty();
	}

	@Test
	public void testFindPlaylistByNameWhenPlaylistDoesNotExist() {
		assertThat(musicStoreRepository.findPlaylistByName(PLAYLIST_1_NAME))
			.isNull();
	}

	@Test
	public void testFindPlaylistByNameWhenPlaylistExists() {
		Playlist playlist = new Playlist(PLAYLIST_1_NAME);
		addTestPlaylistToDatabase(playlist);

		Playlist playlistToFind = new Playlist(PLAYLIST_2_NAME, Arrays.asList(new Song(SONG_1_ID, SONG_1_TITLE, SONG_1_ARTIST)));
		addTestPlaylistToDatabase(playlistToFind);

		assertThat(musicStoreRepository.findPlaylistByName(PLAYLIST_2_NAME))
			.isEqualTo(playlistToFind);
		assertThat(musicStoreRepository.findPlaylistByName(PLAYLIST_2_NAME).getSongs())
			.containsExactly(new Song(SONG_1_ID, SONG_1_TITLE, SONG_1_ARTIST));
	}

	@Test
	public void testCreatePlaylist() {
		Playlist playlistToCreate = new Playlist(PLAYLIST_1_NAME, Arrays.asList(new Song(SONG_1_ID, SONG_1_TITLE, SONG_1_ARTIST)));

		musicStoreRepository.createPlaylist(playlistToCreate);

		assertThat(readAllPlaylistsFromDatabase())
			.containsExactly(playlistToCreate);
		assertThat(readAllPlaylistsFromDatabase().get(0).getSongs())
			.containsExactly(new Song(SONG_1_ID, SONG_1_TITLE, SONG_1_ARTIST));
	}

	@Test
	public void testUpdatePlaylist() {
		Playlist playlistToUpdate = new Playlist(PLAYLIST_1_NAME, Arrays.asList(new Song(SONG_1_ID, SONG_1_TITLE, SONG_1_ARTIST)));
		addTestPlaylistToDatabase(playlistToUpdate);

		Playlist playlistUpdated = new Playlist(PLAYLIST_1_NAME, Arrays.asList(new Song(SONG_2_ID, SONG_2_TITLE, SONG_2_ARTIST)));

		musicStoreRepository.updatePlaylist(playlistUpdated);

		assertThat(readAllPlaylistsFromDatabase())
			.containsExactly(playlistUpdated);
		assertThat(readAllPlaylistsFromDatabase().get(0).getSongs())
			.containsExactly(new Song(SONG_2_ID, SONG_2_TITLE, SONG_2_ARTIST));
	}

	@Test
	public void testDeletePlaylist() {
		Playlist playlistToDelete = new Playlist(PLAYLIST_1_NAME);

		addTestPlaylistToDatabase(playlistToDelete);

		musicStoreRepository.deletePlaylist(playlistToDelete);

		assertThat(readAllPlaylistsFromDatabase())
			.isEmpty();
	}


	// Helper methods.
	private void addTestSongToDatabase(Song song) {
		songCollection.insertOne(
			new Document()
				.append(ID_FIELD, song.getId())
				.append(TITLE_FIELD, song.getTitle())
				.append(ARTIST_FIELD, song.getArtist()));
	}

	private void addTestPlaylistToDatabase(Playlist playlist) {
		playlistCollection.insertOne(
				new Document()
				.append(TITLE_FIELD, playlist.getName())
				.append(SONGS_FIELD, playlist.getSongs().stream()
					.map(song -> new Document()
						.append(ID_FIELD, song.getId())
						.append(TITLE_FIELD, song.getTitle())
						.append(ARTIST_FIELD, song.getArtist()))
					.collect(Collectors.toList())));
	}

	private List<Song> readAllSongsFromDatabase() {
		return StreamSupport
			.stream(songCollection.find().spliterator(), false)
			.map(songDocument -> new Song(
				songDocument.getInteger(ID_FIELD),
				songDocument.getString(TITLE_FIELD),
				songDocument.getString(ARTIST_FIELD)))
			.collect(Collectors.toList());
	}

	private List<Playlist> readAllPlaylistsFromDatabase() {
		return StreamSupport
			.stream(playlistCollection.find().spliterator(), false)
			.map(playlistDocument -> new Playlist(
				playlistDocument.getString(TITLE_FIELD),
				playlistDocument.getList(SONGS_FIELD, Document.class).stream()
					.map(songDocument -> new Song(
						songDocument.getInteger(ID_FIELD),
						songDocument.getString(TITLE_FIELD),
						songDocument.getString(ARTIST_FIELD)))
					.collect(Collectors.toList())))
				.collect(Collectors.toList());
	}
}
