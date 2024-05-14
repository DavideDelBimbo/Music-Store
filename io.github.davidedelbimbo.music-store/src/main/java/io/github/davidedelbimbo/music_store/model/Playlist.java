package io.github.davidedelbimbo.music_store.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Playlist {
	private String name;
	private List<Song> songs;

	public Playlist(String name) {
		this.name = name;
		this.songs = new ArrayList<>();
	}

	public Playlist(String name, List<Song> songs) {
		this.name = name;
		this.songs = new ArrayList<>(songs);
	}

	public String getName() {
		return name;
	}

	public List<Song> getSongs() {
		return songs;
	}

	public void addSong(Song song) {
		this.songs.add(song);
	}

	public void removeSong(Song song) {
		this.songs.remove(song);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.name, this.songs);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof Playlist) {
			final Playlist other = (Playlist) obj;
			return Objects.equals(this.name, other.name)
					&& Objects.equals(this.songs, other.songs);
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return this.name;
	}
}
