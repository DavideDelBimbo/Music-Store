package io.github.davidedelbimbo.music_store.model;

import java.util.Objects;

public class Song {
	private Integer id;
	private String title;
	private String artist;

	public Song(Integer id, String title, String artist) {
		this.id = id;
		this.title = title;
		this.artist = artist;
	}

	public Integer getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getArtist() {
		return artist;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.id, this.title, this.artist);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof Song) {
			final Song other = (Song) obj;
			return Objects.equals(this.id, other.id)
					&& Objects.equals(this.title, other.title)
					&& Objects.equals(this.artist, other.artist);
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return this.artist + " - " + this.title;
	}
}
