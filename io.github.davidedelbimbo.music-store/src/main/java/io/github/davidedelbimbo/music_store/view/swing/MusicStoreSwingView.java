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

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Color;

import io.github.davidedelbimbo.music_store.model.Playlist;
import io.github.davidedelbimbo.music_store.model.Song;

public class MusicStoreSwingView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public MusicStoreSwingView() {
		setTitle("Music Store View");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		
		JLabel lblSelectPlaylist = new JLabel("Select a playlist");
		lblSelectPlaylist.setName("lblSelectPlaylist");
		GridBagConstraints gbc_lblSelectPlaylist = new GridBagConstraints();
		gbc_lblSelectPlaylist.anchor = GridBagConstraints.EAST;
		gbc_lblSelectPlaylist.insets = new Insets(0, 0, 5, 5);
		gbc_lblSelectPlaylist.gridx = 0;
		gbc_lblSelectPlaylist.gridy = 0;
		contentPane.add(lblSelectPlaylist, gbc_lblSelectPlaylist);
		
		JComboBox<Playlist> comboBoxPlaylists = new JComboBox<>();
		comboBoxPlaylists.setName("comboBoxPlaylists");
		GridBagConstraints gbc_comboBoxPlaylists = new GridBagConstraints();
		gbc_comboBoxPlaylists.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxPlaylists.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxPlaylists.gridwidth = 4;
		gbc_comboBoxPlaylists.gridx = 1;
		gbc_comboBoxPlaylists.gridy = 0;
		contentPane.add(comboBoxPlaylists, gbc_comboBoxPlaylists);
		
		JButton btnCreatePlaylist = new JButton("Create new playlist");
		btnCreatePlaylist.setName("btnCreatePlaylist");
		GridBagConstraints gbc_btnCreatePlaylist = new GridBagConstraints();
		gbc_btnCreatePlaylist.anchor = GridBagConstraints.WEST;
		gbc_btnCreatePlaylist.insets = new Insets(0, 0, 5, 5);
		gbc_btnCreatePlaylist.gridx = 1;
		gbc_btnCreatePlaylist.gridy = 1;
		contentPane.add(btnCreatePlaylist, gbc_btnCreatePlaylist);
		
		JButton btnDeletePlaylist = new JButton("Delete selcted playlist");
		btnDeletePlaylist.setEnabled(false);
		btnDeletePlaylist.setName("btnDeletePlaylist");
		GridBagConstraints gbc_btnDeletePlaylist = new GridBagConstraints();
		gbc_btnDeletePlaylist.anchor = GridBagConstraints.EAST;
		gbc_btnDeletePlaylist.insets = new Insets(0, 0, 5, 0);
		gbc_btnDeletePlaylist.gridx = 4;
		gbc_btnDeletePlaylist.gridy = 1;
		contentPane.add(btnDeletePlaylist, gbc_btnDeletePlaylist);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.gridwidth = 2;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 2;
		contentPane.add(scrollPane, gbc_scrollPane);
		
		JList<Song> listSongsInStore = new JList<>();
		listSongsInStore.setName("listSongsInStore");
		listSongsInStore.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(listSongsInStore);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane_1.gridwidth = 3;
		gbc_scrollPane_1.gridx = 2;
		gbc_scrollPane_1.gridy = 2;
		contentPane.add(scrollPane_1, gbc_scrollPane_1);
		
		JList<Song> listSongsInPlaylist = new JList<>();
		listSongsInPlaylist.setName("listSongsInPlaylist");
		listSongsInPlaylist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane_1.setViewportView(listSongsInPlaylist);
		
		JButton btnAddToPlaylist = new JButton("Add to playlist");
		btnAddToPlaylist.setEnabled(false);
		btnAddToPlaylist.setName("btnAddToPlaylist");
		GridBagConstraints gbc_btnAddToPlaylist = new GridBagConstraints();
		gbc_btnAddToPlaylist.anchor = GridBagConstraints.WEST;
		gbc_btnAddToPlaylist.insets = new Insets(0, 0, 5, 5);
		gbc_btnAddToPlaylist.gridx = 0;
		gbc_btnAddToPlaylist.gridy = 3;
		contentPane.add(btnAddToPlaylist, gbc_btnAddToPlaylist);
		
		JButton btnRemoveFromPlaylist = new JButton("Remove from playlist");
		btnRemoveFromPlaylist.setEnabled(false);
		btnRemoveFromPlaylist.setName("btnRemoveFromPlaylist");
		GridBagConstraints gbc_btnRemoveFromPlaylist = new GridBagConstraints();
		gbc_btnRemoveFromPlaylist.anchor = GridBagConstraints.EAST;
		gbc_btnRemoveFromPlaylist.insets = new Insets(0, 0, 5, 0);
		gbc_btnRemoveFromPlaylist.gridx = 4;
		gbc_btnRemoveFromPlaylist.gridy = 3;
		contentPane.add(btnRemoveFromPlaylist, gbc_btnRemoveFromPlaylist);
		
		JLabel lblErrorMessage = new JLabel(" ");
		lblErrorMessage.setName("lblErrorMessage");
		lblErrorMessage.setForeground(new Color(255, 0, 0));
		GridBagConstraints gbc_lblErrorMessage = new GridBagConstraints();
		gbc_lblErrorMessage.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblErrorMessage.insets = new Insets(0, 0, 0, 5);
		gbc_lblErrorMessage.gridwidth = 5;
		gbc_lblErrorMessage.gridx = 0;
		gbc_lblErrorMessage.gridy = 4;
		contentPane.add(lblErrorMessage, gbc_lblErrorMessage);
	}
}