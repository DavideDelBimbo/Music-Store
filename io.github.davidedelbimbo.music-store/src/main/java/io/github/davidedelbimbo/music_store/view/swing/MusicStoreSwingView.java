package io.github.davidedelbimbo.music_store.view.swing;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Color;

import java.util.List;

import io.github.davidedelbimbo.music_store.model.Song;
import io.github.davidedelbimbo.music_store.controller.MusicStoreController;
import io.github.davidedelbimbo.music_store.model.Playlist;
import io.github.davidedelbimbo.music_store.view.MusicStoreView;

public class MusicStoreSwingView extends JFrame implements MusicStoreView {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JLabel lblSelectPlaylist;
	private JComboBox<Playlist> comboBoxPlaylists;
	private JButton btnCreatePlaylist;
	private JButton btnDeletePlaylist;
	private JScrollPane scrollPaneSongsInStore;
	private JList<Song> listSongsInStore;
	private JScrollPane scrollPaneSongsInPlaylist;
	private JList<Song> listSongsInPlaylist;
	private JButton btnAddToPlaylist;
	private JButton btnRemoveFromPlaylist;
	private JLabel lblErrorMessage;

	private DefaultComboBoxModel<Playlist> comboBoxPlaylistsModel;
	private DefaultListModel<Song> listSongsInStoreModel;
	private DefaultListModel<Song> listSongsInPlaylistModel;

	private transient MusicStoreController musicStoreController;

	DefaultComboBoxModel<Playlist> getComboBoxPlaylistsModel() {
		return comboBoxPlaylistsModel;
	}

	DefaultListModel<Song> getListSongsInStoreModel() {
		return listSongsInStoreModel;
	}

	DefaultListModel<Song> getListSongsInPlaylistModel() {
		return listSongsInPlaylistModel;
	}

	public void setMusicStoreController(MusicStoreController musicStoreController) {
		this.musicStoreController = musicStoreController;
	}

	/**
	 * Create the frame.
	 */
	public MusicStoreSwingView() {
		setTitle("Music Store View");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0, 0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);

		lblSelectPlaylist = new JLabel("Select a playlist");
		lblSelectPlaylist.setName("lblSelectPlaylist");
		GridBagConstraints gbc_lblSelectPlaylist = new GridBagConstraints();
		gbc_lblSelectPlaylist.anchor = GridBagConstraints.EAST;
		gbc_lblSelectPlaylist.insets = new Insets(0, 0, 5, 5);
		gbc_lblSelectPlaylist.gridx = 0;
		gbc_lblSelectPlaylist.gridy = 0;
		contentPane.add(lblSelectPlaylist, gbc_lblSelectPlaylist);

		comboBoxPlaylistsModel = new DefaultComboBoxModel<>();
		comboBoxPlaylists = new JComboBox<>(comboBoxPlaylistsModel);
		comboBoxPlaylists.setName("comboBoxPlaylists");
		GridBagConstraints gbc_comboBoxPlaylists = new GridBagConstraints();
		gbc_comboBoxPlaylists.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxPlaylists.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxPlaylists.gridwidth = 4;
		gbc_comboBoxPlaylists.gridx = 1;
		gbc_comboBoxPlaylists.gridy = 0;
		contentPane.add(comboBoxPlaylists, gbc_comboBoxPlaylists);

		// Playlist combo box listener.
		comboBoxPlaylists.addActionListener(e -> {
			// Toggle buttons based on selection.
			toggleDeletePlaylistButton();
			toggleAddToPlaylistButton();
			toggleRemoveFromPlaylistButton();

			if (comboBoxPlaylists.getSelectedIndex() != -1) {
				// Display all songs in selected playlist.
				Playlist playlist = (Playlist) comboBoxPlaylists.getSelectedItem();
				musicStoreController.allSongsInPlaylist(playlist);
			} else {
				// Clear the list of songs in playlist.
				listSongsInPlaylistModel.clear();
			}
		});

		btnCreatePlaylist = new JButton("Create new playlist");
		btnCreatePlaylist.setName("btnCreatePlaylist");
		GridBagConstraints gbc_btnCreatePlaylist = new GridBagConstraints();
		gbc_btnCreatePlaylist.anchor = GridBagConstraints.WEST;
		gbc_btnCreatePlaylist.insets = new Insets(0, 0, 5, 5);
		gbc_btnCreatePlaylist.gridx = 1;
		gbc_btnCreatePlaylist.gridy = 1;
		contentPane.add(btnCreatePlaylist, gbc_btnCreatePlaylist);

		btnDeletePlaylist = new JButton("Delete selcted playlist");
		btnDeletePlaylist.setEnabled(false);
		btnDeletePlaylist.setName("btnDeletePlaylist");
		GridBagConstraints gbc_btnDeletePlaylist = new GridBagConstraints();
		gbc_btnDeletePlaylist.anchor = GridBagConstraints.EAST;
		gbc_btnDeletePlaylist.insets = new Insets(0, 0, 5, 0);
		gbc_btnDeletePlaylist.gridx = 4;
		gbc_btnDeletePlaylist.gridy = 1;
		contentPane.add(btnDeletePlaylist, gbc_btnDeletePlaylist);

		// Delete button listener.
		btnDeletePlaylist.addActionListener(e -> {
			// Delete selected playlist.
			Playlist playlist = (Playlist) comboBoxPlaylists.getSelectedItem();
			musicStoreController.deletePlaylist(playlist);
		});

		scrollPaneSongsInStore = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.gridwidth = 2;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 2;
		contentPane.add(scrollPaneSongsInStore, gbc_scrollPane);

		listSongsInStoreModel = new DefaultListModel<>();
		listSongsInStore = new JList<>(listSongsInStoreModel);
		listSongsInStore.setName("listSongsInStore");
		listSongsInStore.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPaneSongsInStore.setViewportView(listSongsInStore);

		// List of songs in store listener.
		listSongsInStore.addListSelectionListener(e -> {
			// Toggle buttons based on selection.
			toggleAddToPlaylistButton();

			// Selecting a song in store list should clear the selection in playlist list.
			listSongsInPlaylist.clearSelection();
		});

		scrollPaneSongsInPlaylist = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane_1.gridwidth = 3;
		gbc_scrollPane_1.gridx = 2;
		gbc_scrollPane_1.gridy = 2;
		contentPane.add(scrollPaneSongsInPlaylist, gbc_scrollPane_1);

		listSongsInPlaylistModel = new DefaultListModel<>();
		listSongsInPlaylist = new JList<>(listSongsInPlaylistModel);
		listSongsInPlaylist.setName("listSongsInPlaylist");
		listSongsInPlaylist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPaneSongsInPlaylist.setViewportView(listSongsInPlaylist);

		// List of songs in store listener.
		listSongsInPlaylist.addListSelectionListener(e -> {
			// Toggle buttons based on selection.
			toggleRemoveFromPlaylistButton();

			// Selecting a song in playlist list should clear the selection in store list.
			listSongsInStore.clearSelection();
		});
		

		btnAddToPlaylist = new JButton("Add to playlist");
		btnAddToPlaylist.setEnabled(false);
		btnAddToPlaylist.setName("btnAddToPlaylist");
		GridBagConstraints gbc_btnAddToPlaylist = new GridBagConstraints();
		gbc_btnAddToPlaylist.anchor = GridBagConstraints.WEST;
		gbc_btnAddToPlaylist.insets = new Insets(0, 0, 5, 5);
		gbc_btnAddToPlaylist.gridx = 0;
		gbc_btnAddToPlaylist.gridy = 3;
		contentPane.add(btnAddToPlaylist, gbc_btnAddToPlaylist);

		// Add song to playlist button listener.
		btnAddToPlaylist.addActionListener(e -> {
			// Add selected song to selected playlist.
			Playlist playlist = (Playlist) comboBoxPlaylists.getSelectedItem();
			Song song = listSongsInStore.getSelectedValue();
			musicStoreController.addSongToPlaylist(playlist, song);
		});

		btnRemoveFromPlaylist = new JButton("Remove from playlist");
		btnRemoveFromPlaylist.setEnabled(false);
		btnRemoveFromPlaylist.setName("btnRemoveFromPlaylist");
		GridBagConstraints gbc_btnRemoveFromPlaylist = new GridBagConstraints();
		gbc_btnRemoveFromPlaylist.anchor = GridBagConstraints.EAST;
		gbc_btnRemoveFromPlaylist.insets = new Insets(0, 0, 5, 0);
		gbc_btnRemoveFromPlaylist.gridx = 4;
		gbc_btnRemoveFromPlaylist.gridy = 3;
		contentPane.add(btnRemoveFromPlaylist, gbc_btnRemoveFromPlaylist);

		// Remove song from playlist button listener.
		btnRemoveFromPlaylist.addActionListener(e -> {
			// Remove selected song from selected playlist.
			Playlist playlist = (Playlist) comboBoxPlaylists.getSelectedItem();
			Song song = listSongsInPlaylist.getSelectedValue();
			musicStoreController.removeSongFromPlaylist(playlist, song);
		});

		lblErrorMessage = new JLabel(" ");
		lblErrorMessage.setName("lblErrorMessage");
		lblErrorMessage.setForeground(new Color(255, 0, 0));
		GridBagConstraints gbc_lblErrorMessage = new GridBagConstraints();
		gbc_lblErrorMessage.insets = new Insets(0, 0, 0, 5);
		gbc_lblErrorMessage.gridwidth = 5;
		gbc_lblErrorMessage.gridx = 0;
		gbc_lblErrorMessage.gridy = 4;
		contentPane.add(lblErrorMessage, gbc_lblErrorMessage);
	}


	@Override
	public void displayAllSongsInStore(List<Song> songs) {
		songs.stream().forEach(listSongsInStoreModel::addElement);
	}

	@Override
	public void displayAllSongsInPlaylist(List<Song> songs) {
		songs.stream().forEach(listSongsInPlaylistModel::addElement);
	}

	@Override
	public void displayAllPlaylists(List<Playlist> playlists) {
		playlists.stream().forEach(comboBoxPlaylistsModel::addElement);
	}

	@Override
	public void displayPlaylist(Playlist playlist) {
		comboBoxPlaylistsModel.addElement(playlist);
		comboBoxPlaylists.setSelectedItem(playlist);
		resetErrrorLabel();
	}

	@Override
	public void hidePlaylist(Playlist playlist) {
		comboBoxPlaylistsModel.removeElement(playlist);
		comboBoxPlaylists.setSelectedIndex(-1);
		resetErrrorLabel();
	}

	@Override
	public void displaySongInPlaylist(Song song) {
		listSongsInPlaylistModel.addElement(song);
		resetErrrorLabel();
	}

	@Override
	public void hideSongFromPlaylist(Song song) {
		listSongsInPlaylistModel.removeElement(song);
		resetErrrorLabel();
	}

	@Override
	public void displayError(String message) {
		lblErrorMessage.setText(message);
	}


	// Helper methods.
	private void resetErrrorLabel() {
		lblErrorMessage.setText(" ");
	}

	private void toggleDeletePlaylistButton() {
		boolean isPlaylistSelected = comboBoxPlaylists.getSelectedIndex() != -1;

		btnDeletePlaylist.setEnabled(isPlaylistSelected);
	}

	private void toggleAddToPlaylistButton() {
		boolean isPlaylistSelected = comboBoxPlaylists.getSelectedIndex() != -1;
		boolean isSongInStoreSelected = listSongsInStore.getSelectedIndex() != -1;

		btnDeletePlaylist.setEnabled(isPlaylistSelected);
		btnAddToPlaylist.setEnabled(isPlaylistSelected && isSongInStoreSelected);
	}

	private void toggleRemoveFromPlaylistButton() {
		boolean isPlaylistSelected = comboBoxPlaylists.getSelectedIndex() != -1;
		boolean isSongInPlaylistSelected = listSongsInPlaylist.getSelectedIndex() != -1;

		btnDeletePlaylist.setEnabled(isPlaylistSelected);
		btnRemoveFromPlaylist.setEnabled(isPlaylistSelected && isSongInPlaylistSelected);
	}
}