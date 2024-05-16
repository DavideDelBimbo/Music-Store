package io.github.davidedelbimbo.music_store.model;

import java.util.Objects;

public class Song {
	private String title;
	private String artist;

	public Song(String title, String artist) {
		this.title = title;
		this.artist = artist;
	}

	public String getTitle() {
		return title;
	}

	public String getArtist() {
		return artist;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.title, this.artist);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof Song) {
			final Song other = (Song) obj;
			return Objects.equals(this.title, other.title)
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
