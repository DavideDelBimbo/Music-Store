package io.github.davidedelbimbo.music_store.model;

import java.util.List;

public class Playlist {
	private String name;
	private List<Song> songs;

	public Playlist(String name, List<Song> songs) {
		this.name = name;
		this.songs = songs;
	}

	public String getName() {
		return name;
	}

	public List<Song> getSongs() {
		return songs;
	}

	@Override
	public String toString() {
		return this.getName();
	}
}
