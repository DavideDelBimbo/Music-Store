package io.github.davidedelbimbo.music_store.repository.mongo;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
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

import io.github.davidedelbimbo.music_store.model.Song;
import io.github.davidedelbimbo.music_store.model.Playlist;

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
		musicStoreRepository = new MusicStoreMongoRepository(client);
		
		// Start with a clean database.
		MongoDatabase database = client.getDatabase(MusicStoreMongoRepository.STORE_DB_NAME);
		database.drop();
		
		// Get the song collection.
		songCollection = database.getCollection(MusicStoreMongoRepository.SONG_COLLECTION_NAME);
		
		// Get the playlist collection.
		playlistCollection = database.getCollection(MusicStoreMongoRepository.PLAYLIST_COLLECTION_NAME);
	}

	@After
	public void tearDown() {
		client.close();
	}

	@Test
	public void testFindAllSongsWhenDatabaseIsEmpty() {
		assertThat(this.musicStoreRepository.findAllSongs())
			.isEmpty();
	}

	@Test
	public void testFindAllSongs() {
		Song song1 = new Song(SONG_1_ID, SONG_1_TITLE, SONG_1_ARTIST);
		Song song2 = new Song(SONG_2_ID, SONG_2_TITLE, SONG_2_ARTIST);

		addTestSongToDatabase(song1);
		addTestSongToDatabase(song2);

		assertThat(this.musicStoreRepository.findAllSongs())
			.containsExactly(song1, song2);
	}

	@Test
	public void testInitilizeSongCollection() {
		Song song1 = new Song(SONG_1_ID, SONG_1_TITLE, SONG_1_ARTIST);
		Song song2 = new Song(SONG_2_ID, SONG_2_TITLE, SONG_2_ARTIST);

		this.musicStoreRepository.initilizeSongCollection(Arrays.asList(song1, song2));
		
		assertThat(readAllSongsFromDatabase())
			.containsExactly(song1, song2);
	}

	@Test
	public void testFindSongByIdWhenSongDoesNotExist() {
		assertThat(this.musicStoreRepository.findSongById(SONG_1_ID))
			.isNull();
	}

	@Test
	public void testFindSongByIdWhenSongExists() {
		Song song = new Song(SONG_1_ID, SONG_1_TITLE, SONG_1_ARTIST);
		Song songToFind = new Song(SONG_2_ID, SONG_2_TITLE, SONG_2_ARTIST);

		addTestSongToDatabase(song);
		addTestSongToDatabase(songToFind);

		assertThat(this.musicStoreRepository.findSongById(SONG_2_ID))
			.isEqualTo(songToFind);
	}

	@Test
	public void testFindAllPlaylistsWhenDatabaseIsEmpty() {
		assertThat(this.musicStoreRepository.findAllPlaylists())
			.isEmpty();
	}

	@Test
	public void testFindAllPlaylists() {
		ArrayList<Song> songsPlaylist1 = new ArrayList<>(Arrays.asList(new Song(SONG_1_ID, SONG_1_TITLE, SONG_1_ARTIST)));
		Playlist playlist1 = new Playlist(PLAYLIST_1_NAME, songsPlaylist1);

		Playlist playlist2 = new Playlist(PLAYLIST_2_NAME);

		addTestPlaylistToDatabase(playlist1);
		addTestPlaylistToDatabase(playlist2);

		assertThat(this.musicStoreRepository.findAllPlaylists())
			.containsExactly(playlist1, playlist2);
	}

	@Test
	public void testFindPlaylistByNameWhenPlaylistDoesNotExist() {
		assertThat(this.musicStoreRepository.findPlaylistByName(PLAYLIST_1_NAME))
			.isNull();
	}

	@Test
	public void testFindPlaylistByNameWhenPlaylistExists() {
		Playlist playlist = new Playlist(PLAYLIST_1_NAME);

		ArrayList<Song> songsPlaylistToFind = new ArrayList<>(Arrays.asList(new Song(SONG_1_ID, SONG_1_TITLE, SONG_1_ARTIST)));
		Playlist playlistToFind = new Playlist(PLAYLIST_2_NAME, songsPlaylistToFind);

		addTestPlaylistToDatabase(playlist);
		addTestPlaylistToDatabase(playlistToFind);

		assertThat(this.musicStoreRepository.findPlaylistByName(PLAYLIST_2_NAME))
			.isEqualTo(playlistToFind);
		assertThat(this.musicStoreRepository.findPlaylistByName(PLAYLIST_2_NAME).getSongs())
			.isEqualTo(songsPlaylistToFind);
	}

	@Test
	public void testCreatePlaylist() {
		Playlist playlistToCreate = new Playlist(PLAYLIST_1_NAME);

		musicStoreRepository.createPlaylist(playlistToCreate);

		assertThat(readAllPlaylistsFromDatabase()).containsExactly(playlistToCreate);
	}

	@Test
	public void testUpdatePlaylist() {
		List<Song> songsPlaylistToUpdate = new ArrayList<>(Arrays.asList(new Song(SONG_1_ID, SONG_1_TITLE, SONG_1_ARTIST)));
		Playlist playlistToUpdate = new Playlist(PLAYLIST_1_NAME, songsPlaylistToUpdate);

		addTestPlaylistToDatabase(playlistToUpdate);

		List<Song> songsPlaylistUpdated = new ArrayList<>(Arrays.asList(new Song(SONG_2_ID, SONG_2_TITLE, SONG_2_ARTIST)));
		Playlist playlistUpdated = new Playlist(PLAYLIST_1_NAME, songsPlaylistUpdated);

		musicStoreRepository.updatePlaylist(playlistUpdated);

		assertThat(readAllPlaylistsFromDatabase())
			.containsExactly(playlistUpdated);
		assertThat(readAllPlaylistsFromDatabase().get(0).getSongs())
			.isEqualTo(songsPlaylistUpdated);
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
		this.songCollection.insertOne(
			new Document()
				.append(MusicStoreMongoRepository.ID_FIELD, song.getId())
				.append(MusicStoreMongoRepository.TITLE_FIELD, song.getTitle())
				.append(MusicStoreMongoRepository.ARTIST_FIELD, song.getArtist()));
	}

	private void addTestPlaylistToDatabase(Playlist playlist) {
		this.playlistCollection.insertOne(
				new Document()
				.append(MusicStoreMongoRepository.TITLE_FIELD, playlist.getName())
				.append(MusicStoreMongoRepository.SONGS_FIELD, playlist.getSongs().stream()
					.map(song -> new Document()
						.append(MusicStoreMongoRepository.ID_FIELD, song.getId())
						.append(MusicStoreMongoRepository.TITLE_FIELD, song.getTitle())
						.append(MusicStoreMongoRepository.ARTIST_FIELD, song.getArtist()))
					.collect(Collectors.toList())));
	}

	private List<Song> readAllSongsFromDatabase() {
		return StreamSupport
			.stream(this.songCollection.find().spliterator(), false)
			.map(songDocument -> new Song(
				songDocument.getInteger(MusicStoreMongoRepository.ID_FIELD),
				songDocument.getString(MusicStoreMongoRepository.TITLE_FIELD),
				songDocument.getString(MusicStoreMongoRepository.ARTIST_FIELD)))
			.collect(Collectors.toList());
	}

	private List<Playlist> readAllPlaylistsFromDatabase() {
		return StreamSupport
			.stream(this.playlistCollection.find().spliterator(), false)
			.map(playlistDocument -> new Playlist(
				playlistDocument.getString(MusicStoreMongoRepository.TITLE_FIELD),
				playlistDocument.getList(MusicStoreMongoRepository.SONGS_FIELD, Document.class).stream()
					.map(songDocument -> new Song(
						songDocument.getInteger(MusicStoreMongoRepository.ID_FIELD),
						songDocument.getString(MusicStoreMongoRepository.TITLE_FIELD),
						songDocument.getString(MusicStoreMongoRepository.ARTIST_FIELD)))
					.collect(Collectors.toList())))
				.collect(Collectors.toList());
	}
}
