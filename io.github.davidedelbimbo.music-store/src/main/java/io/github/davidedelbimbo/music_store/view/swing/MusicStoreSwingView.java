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
import javax.swing.ScrollPaneConstants;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import java.util.List;
import java.util.stream.IntStream;

import io.github.davidedelbimbo.music_store.controller.MusicStoreController;
import io.github.davidedelbimbo.music_store.view.MusicStoreView;
import io.github.davidedelbimbo.music_store.model.Playlist;
import io.github.davidedelbimbo.music_store.model.Song;

public class MusicStoreSwingView extends JFrame implements MusicStoreView {
	public static final String LBL_SELECT_PLAYLIST = "lblSelectPlaylist";
	public static final String COMBO_BOX_PLAYLISTS = "comboBoxPlaylists";
	public static final String BTN_CREATE_PLAYLIST_VIEW = "btnCreatePlaylist";
	public static final String BTN_DELETE_PLAYLIST = "btnDeletePlaylist";
	public static final String LIST_SONGS_IN_STORE = "listSongsInStore";
	public static final String LIST_SONGS_IN_PLAYLIST = "listSongsInPlaylist";	
	public static final String BTN_ADD_TO_PLAYLIST = "btnAddToPlaylist";
	public static final String BTN_REMOVE_FROM_PLAYLIST = "btnRemoveFromPlaylist";
	public static final String LBL_ERROR_MESSAGE_VIEW = "lblErrorMessage";

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
	private CreatePlaylistDialog createPlaylistDialog;

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
		this.createPlaylistDialog.setMusicStoreController(musicStoreController);
	}

	/**
	 * Create the frame.
	 */
	public MusicStoreSwingView(CreatePlaylistDialog createPlaylistDialog) {
		setMinimumSize(new Dimension(530, 0));
		setResizable(false);
		setTitle("Music Store View");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		// Create playlist dialog.
		this.createPlaylistDialog = createPlaylistDialog;

		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{110, 150, 30, 30, 200, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);

		lblSelectPlaylist = new JLabel("Select a playlist");
		lblSelectPlaylist.setName(LBL_SELECT_PLAYLIST);
		GridBagConstraints gbc_lblSelectPlaylist = new GridBagConstraints();
		gbc_lblSelectPlaylist.anchor = GridBagConstraints.EAST;
		gbc_lblSelectPlaylist.insets = new Insets(0, 0, 5, 5);
		gbc_lblSelectPlaylist.gridx = 0;
		gbc_lblSelectPlaylist.gridy = 0;
		contentPane.add(lblSelectPlaylist, gbc_lblSelectPlaylist);

		comboBoxPlaylistsModel = new DefaultComboBoxModel<>();
		comboBoxPlaylists = new JComboBox<>(comboBoxPlaylistsModel);
		comboBoxPlaylists.setName(COMBO_BOX_PLAYLISTS);
		GridBagConstraints gbc_comboBoxPlaylists = new GridBagConstraints();
		gbc_comboBoxPlaylists.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxPlaylists.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxPlaylists.gridwidth = 4;
		gbc_comboBoxPlaylists.gridx = 1;
		gbc_comboBoxPlaylists.gridy = 0;
		contentPane.add(comboBoxPlaylists, gbc_comboBoxPlaylists);

		// Start with no selection in combo box.
		comboBoxPlaylistsModel.setSelectedItem("");

		// Playlist combo box listener.
		comboBoxPlaylists.addActionListener(e -> {
			// Toggle buttons based on selection.
			toggleButtons();

			// Reset error label.
			resetErrorLabel();

			// Display songs in selected playlist.
			listSongsInPlaylistModel.clear();
			Playlist playlist = (Playlist) comboBoxPlaylists.getSelectedItem();
			if (playlist != null) {
				musicStoreController.allSongsInPlaylist(playlist);
			}
		});

		btnCreatePlaylist = new JButton("Create Playlist");
		btnCreatePlaylist.setName(BTN_CREATE_PLAYLIST_VIEW);
		GridBagConstraints gbc_btnCreatePlaylist = new GridBagConstraints();
		gbc_btnCreatePlaylist.anchor = GridBagConstraints.WEST;
		gbc_btnCreatePlaylist.insets = new Insets(0, 0, 5, 5);
		gbc_btnCreatePlaylist.gridx = 1;
		gbc_btnCreatePlaylist.gridy = 1;
		contentPane.add(btnCreatePlaylist, gbc_btnCreatePlaylist);

		// Create playlist button listener.
		btnCreatePlaylist.addActionListener(e -> createPlaylistDialog.setVisible(true));

		btnDeletePlaylist = new JButton("Delete Playlist");
		btnDeletePlaylist.setEnabled(false);
		btnDeletePlaylist.setName(BTN_DELETE_PLAYLIST);
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
		scrollPaneSongsInStore.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.gridwidth = 2;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 2;
		contentPane.add(scrollPaneSongsInStore, gbc_scrollPane);

		listSongsInStoreModel = new DefaultListModel<>();
		listSongsInStore = new JList<>(listSongsInStoreModel);
		listSongsInStore.setAlignmentX(Component.LEFT_ALIGNMENT);
		listSongsInStore.setName(LIST_SONGS_IN_STORE);
		listSongsInStore.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPaneSongsInStore.setViewportView(listSongsInStore);

		// List of songs in store listener.
		listSongsInStore.addListSelectionListener(e -> {
			// Toggle buttons based on selection.
			toggleButtons();

			// Selecting a song in store list should clear the selection in playlist list.
			if (listSongsInStore.getSelectedIndex() != -1) {
				listSongsInPlaylist.clearSelection();
			}
		});

		scrollPaneSongsInPlaylist = new JScrollPane();
		scrollPaneSongsInPlaylist.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane_1.gridwidth = 3;
		gbc_scrollPane_1.gridx = 2;
		gbc_scrollPane_1.gridy = 2;
		contentPane.add(scrollPaneSongsInPlaylist, gbc_scrollPane_1);

		listSongsInPlaylistModel = new DefaultListModel<>();
		listSongsInPlaylist = new JList<>(listSongsInPlaylistModel);
		listSongsInPlaylist.setAlignmentX(Component.RIGHT_ALIGNMENT);
		listSongsInPlaylist.setFixedCellWidth(250);
		listSongsInPlaylist.setName(LIST_SONGS_IN_PLAYLIST);
		listSongsInPlaylist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPaneSongsInPlaylist.setViewportView(listSongsInPlaylist);

		// List of songs in store listener.
		listSongsInPlaylist.addListSelectionListener(e -> {
			// Toggle buttons based on selection.
			toggleButtons();

			// Selecting a song in playlist list should clear the selection in store list.
			if (listSongsInPlaylist.getSelectedIndex() != -1) {
				listSongsInStore.clearSelection();
			}
		});

		btnAddToPlaylist = new JButton("Add To Playlist");
		btnAddToPlaylist.setEnabled(false);
		btnAddToPlaylist.setName(BTN_ADD_TO_PLAYLIST);
		GridBagConstraints gbc_btnAddToPlaylist = new GridBagConstraints();
		gbc_btnAddToPlaylist.anchor = GridBagConstraints.WEST;
		gbc_btnAddToPlaylist.insets = new Insets(0, 0, 5, 5);
		gbc_btnAddToPlaylist.gridx = 0;
		gbc_btnAddToPlaylist.gridy = 3;
		contentPane.add(btnAddToPlaylist, gbc_btnAddToPlaylist);

		// Add song to playlist button listener.
		btnAddToPlaylist.addActionListener(e -> {
			Playlist playlist = (Playlist) comboBoxPlaylists.getSelectedItem();
			Song song = listSongsInStore.getSelectedValue();

			// Add selected song to selected playlist.
			musicStoreController.addSongToPlaylist(playlist, song);
		});

		btnRemoveFromPlaylist = new JButton("Remove From Playlist");
		btnRemoveFromPlaylist.setEnabled(false);
		btnRemoveFromPlaylist.setName(BTN_REMOVE_FROM_PLAYLIST);
		GridBagConstraints gbc_btnRemoveFromPlaylist = new GridBagConstraints();
		gbc_btnRemoveFromPlaylist.anchor = GridBagConstraints.EAST;
		gbc_btnRemoveFromPlaylist.insets = new Insets(0, 0, 5, 0);
		gbc_btnRemoveFromPlaylist.gridx = 4;
		gbc_btnRemoveFromPlaylist.gridy = 3;
		contentPane.add(btnRemoveFromPlaylist, gbc_btnRemoveFromPlaylist);

		// Remove song from playlist button listener.
		btnRemoveFromPlaylist.addActionListener(e -> {
			Playlist playlist = (Playlist) comboBoxPlaylists.getSelectedItem();
			Song song = listSongsInPlaylist.getSelectedValue();

			// Remove selected song from selected playlist.
			musicStoreController.removeSongFromPlaylist(playlist, song);
		});

		lblErrorMessage = new JLabel(" ");
		lblErrorMessage.setName(LBL_ERROR_MESSAGE_VIEW);
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
	public void displayAllPlaylists(List<Playlist> playlists) {
		playlists.stream().forEach(comboBoxPlaylistsModel::addElement);
	}

	@Override
	public void displayPlaylist(Playlist playlist) {
		// Close dialog.
		createPlaylistDialog.setVisible(false);

		// Find existing playlist or add new one.
		Playlist existingPlaylist = containsPlaylistIgnoreCase(playlist);
		if (existingPlaylist != null) {
			// Select the existing playlist.
			comboBoxPlaylists.setSelectedItem(existingPlaylist);
		} else {
			// Add the playlist to the combo box and select it.
			comboBoxPlaylistsModel.addElement(playlist);
			comboBoxPlaylists.setSelectedItem(playlist);
		}
		resetErrorLabel();
	}

	@Override
	public void hidePlaylist(Playlist playlist) {
		// Remove the playlist from the combo box.
		comboBoxPlaylistsModel.removeElement(playlist);
		comboBoxPlaylists.setSelectedIndex(-1);
		resetErrorLabel();
	}

	@Override
	public void displayAllSongsInPlaylist(List<Song> songs) {
		// Add songs to playlist list.
		listSongsInPlaylistModel.clear();
		songs.stream().forEach(listSongsInPlaylistModel::addElement);
		resetErrorLabel();
	}

	@Override
	public void displayErrorAndAddPlaylist(String message, Playlist playlist) {
		if (containsPlaylistIgnoreCase(playlist) != null) {
			// Display error in dialog.
			createPlaylistDialog.setErrorMessage(message, playlist);
		} else {
			// Add the playlist to the combo box.
			comboBoxPlaylistsModel.addElement(playlist);
			comboBoxPlaylists.setSelectedItem(playlist);
			createPlaylistDialog.setVisible(false);

			// Display error in view.
			lblErrorMessage.setText(message + playlist);
		}
	}

	@Override
	public void displayErrorAndRemovePlaylist(String message, Playlist playlist) {
		// Remove the playlist from the combo box.
		comboBoxPlaylistsModel.removeElement(playlist);
		comboBoxPlaylists.setSelectedIndex(-1);
		toggleButtons();

		// Display error in view.
		lblErrorMessage.setText(message + playlist);
	}

	@Override
	public void displayErrorAndUpdatePlaylist(String message, Song song, Playlist playlist) {
		// Update the playlist list.
		musicStoreController.allSongsInPlaylist(playlist);

		// Display error in view.
		lblErrorMessage.setText(message + song);
	}


	// Helper methods.
	private void resetErrorLabel() {
		lblErrorMessage.setText(" ");
	}

	private void toggleButtons() {
		boolean isPlaylistSelected = comboBoxPlaylists.getSelectedIndex() != -1;
		boolean isSongInStoreSelected = listSongsInStore.getSelectedIndex() != -1;
		boolean isSongInPlaylistSelected = listSongsInPlaylist.getSelectedIndex() != -1;

		btnDeletePlaylist.setEnabled(isPlaylistSelected);
		btnAddToPlaylist.setEnabled(isPlaylistSelected && isSongInStoreSelected);
		btnRemoveFromPlaylist.setEnabled(isPlaylistSelected && isSongInPlaylistSelected);
	}

	private Playlist containsPlaylistIgnoreCase(Playlist playlist) {
		return IntStream.range(0, comboBoxPlaylistsModel.getSize())
			.mapToObj(comboBoxPlaylistsModel::getElementAt)
			.filter(existingPlaylist -> existingPlaylist.getName().equalsIgnoreCase(playlist.getName()))
			.findFirst()
			.orElse(null);
	}
}