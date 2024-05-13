package io.github.davidedelbimbo.music_store.repository.mongo;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import io.github.davidedelbimbo.music_store.model.Playlist;
import io.github.davidedelbimbo.music_store.model.Song;
import io.github.davidedelbimbo.music_store.repository.MusicStoreRepository;

public class MusicStoreMongoRepository implements MusicStoreRepository {
	public static final String STORE_DB_NAME = "music_store";
	public static final String SONG_COLLECTION_NAME = "song";
	public static final String PLAYLIST_COLLECTION_NAME = "playlist";

	public static final String ID_FIELD = "id";
	public static final String TITLE_FIELD = "title";
	public static final String ARTIST_FIELD = "artist";
	public static final String PLAYLIST_FIELD = "playlist";
	public static final String SONGS_FIELD = "songs";

	private MongoCollection<Document> songCollection;
	private MongoCollection<Document> playlistCollection;

	public MusicStoreMongoRepository(MongoClient client) {
		this.songCollection = client.getDatabase(STORE_DB_NAME).getCollection(SONG_COLLECTION_NAME);
		this.playlistCollection = client.getDatabase(STORE_DB_NAME).getCollection(PLAYLIST_COLLECTION_NAME);
	}

	@Override
	public void initilizeSongCollection(List<Song> songs) {
		this.songCollection.insertMany(
			songs.stream()
			.map(this::fromSongToDocument)
			.collect(Collectors.toList()));
	}

	@Override
	public List<Song> findAllSongs() {
		return StreamSupport
			.stream(songCollection.find().spliterator(), false)
			.map(this::fromDocumentToSong)
			.collect(Collectors.toList());
	}

	@Override
	public Song findSongById(Integer songId) {
		Document songDocument = this.songCollection.find(Filters.eq(ID_FIELD, songId)).first();
		if (songDocument != null) {
			return fromDocumentToSong(songDocument);
		}
		return null;
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
		Document playlistDocument = this.playlistCollection.find(Filters.eq(TITLE_FIELD, playlistName)).first();
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
		this.playlistCollection.replaceOne(Filters.eq(TITLE_FIELD, playlist.getName()), fromPlaylistToDocument(playlist));
	}

	@Override
	public void deletePlaylist(Playlist playlist) {
		this.playlistCollection.deleteOne(Filters.eq(TITLE_FIELD, playlist.getName()));
	}


	// Helper methods.
	private Song fromDocumentToSong(Document document) {
		return new Song(document.getInteger(ID_FIELD), document.getString(TITLE_FIELD), document.getString(ARTIST_FIELD));
	}

	private Playlist fromDocumentToPlaylist(Document document) {
		return new Playlist(
			document.getString(TITLE_FIELD),
			document.getList(SONGS_FIELD, Document.class).stream()
				.map(this::fromDocumentToSong)
				.collect(Collectors.toList()));
	}

	private Document fromSongToDocument(Song song) {
		return new Document()
			.append(ID_FIELD, song.getId())
			.append(TITLE_FIELD, song.getTitle())
			.append(ARTIST_FIELD, song.getArtist());
	}

	private Document fromPlaylistToDocument(Playlist playlist) {
		return new Document()
			.append(TITLE_FIELD, playlist.getName())
			.append(SONGS_FIELD, playlist.getSongs().stream()
				.map(this::fromSongToDocument)
				.collect(Collectors.toList()));
	}
}
