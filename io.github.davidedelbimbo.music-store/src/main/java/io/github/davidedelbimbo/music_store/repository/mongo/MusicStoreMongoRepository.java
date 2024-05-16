package io.github.davidedelbimbo.music_store.repository.mongo;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import io.github.davidedelbimbo.music_store.repository.MusicStoreRepository;
import io.github.davidedelbimbo.music_store.model.Playlist;
import io.github.davidedelbimbo.music_store.model.Song;

public class MusicStoreMongoRepository implements MusicStoreRepository {
	public static final String STORE_DB_NAME = "music_store";
	public static final String SONG_COLLECTION_NAME = "song";
	public static final String PLAYLIST_COLLECTION_NAME = "playlist";

	public static final String SONG_TITLE_FIELD = "title";
	public static final String SONG_ARTIST_FIELD = "artist";
	public static final String PLAYLIST_NAME_FIELD = "name";
	public static final String PLAYLIST_SONGS_FIELD = "songs";

	private MongoCollection<Document> songCollection;
	private MongoCollection<Document> playlistCollection;

	public MusicStoreMongoRepository(MongoClient client, String databaseName, String songCollectionName, String playlistCollectionName) {
		this.songCollection = client.getDatabase(databaseName).getCollection(songCollectionName);
		this.playlistCollection = client.getDatabase(databaseName).getCollection(playlistCollectionName);
	}

	@Override
	public List<Song> findAllSongs() {
		return StreamSupport
			.stream(songCollection.find().spliterator(), false)
			.map(this::fromDocumentToSong)
			.collect(Collectors.toList());
	}

	@Override
	public void initializeSongs(List<Song> songs) {
		// Clear the existing songs.
		this.songCollection.deleteMany(new Document());

		// Insert the new songs.
		this.songCollection.insertMany(songs.stream()
			.map(this::fromSongToDocument)
			.collect(Collectors.toList()));
	}

	@Override
	public List<Playlist> findAllPlaylists() {
		return StreamSupport
			.stream(playlistCollection.find().spliterator(), false)
			.map(this::fromDocumentToPlaylist)
			.collect(Collectors.toList());
	}

	@Override
	public Playlist findPlaylistByName(String playlistName) {
		Pattern pattern = Pattern.compile("^" + playlistName + "$", Pattern.CASE_INSENSITIVE);
		Document playlistDocument = this.playlistCollection.find(Filters.eq(PLAYLIST_NAME_FIELD, pattern)).first();
		if (playlistDocument != null) {
			return fromDocumentToPlaylist(playlistDocument);
		}
		return null;
	}

	@Override
	public void createPlaylist(Playlist playlist) {
		this.playlistCollection.insertOne(fromPlaylistToDocument(playlist));
	}

	@Override
	public void updatePlaylist(Playlist playlist) {
		this.playlistCollection.replaceOne(Filters.eq(PLAYLIST_NAME_FIELD, playlist.getName()), fromPlaylistToDocument(playlist));
	}

	@Override
	public void deletePlaylist(Playlist playlist) {
		this.playlistCollection.deleteOne(Filters.eq(PLAYLIST_NAME_FIELD, playlist.getName()));
	}


	// Helper methods.
	private Song fromDocumentToSong(Document document) {
		return new Song(document.getString(SONG_TITLE_FIELD), document.getString(SONG_ARTIST_FIELD));
	}

	private Playlist fromDocumentToPlaylist(Document document) {
		return new Playlist(
			document.getString(PLAYLIST_NAME_FIELD),
			document.getList(PLAYLIST_SONGS_FIELD, Document.class).stream()
				.map(this::fromDocumentToSong)
				.collect(Collectors.toList()));
	}

	private Document fromSongToDocument(Song song) {
		return new Document()
			.append(SONG_TITLE_FIELD, song.getTitle())
			.append(SONG_ARTIST_FIELD, song.getArtist());
	}

	private Document fromPlaylistToDocument(Playlist playlist) {
		return new Document()
			.append(PLAYLIST_NAME_FIELD, playlist.getName())
			.append(PLAYLIST_SONGS_FIELD, playlist.getSongs().stream()
				.map(this::fromSongToDocument)
				.collect(Collectors.toList()));
	}
}
