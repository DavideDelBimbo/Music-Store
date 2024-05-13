package io.github.davidedelbimbo.music_store.view.swing;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import io.github.davidedelbimbo.music_store.controller.MusicStoreController;
import io.github.davidedelbimbo.music_store.model.Playlist;

import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Color;

public class CreatePlaylistDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private JLabel lblName;
	private JPanel contentPane;
	private JTextField txtName;
	private JButton btnCreatePlaylist;
	private JButton btnCancel;
	private JLabel lblErrorMessage;

	private transient MusicStoreController musicStoreController;

	public void setMusicStoreController(MusicStoreController musicStoreController) {
		this.musicStoreController = musicStoreController;
	}

	/**
	 * Create the dialog.
	 */
	public CreatePlaylistDialog() {
		setTitle("Create A Playlist");
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);

		lblName = new JLabel("Name");
		lblName.setName("lblName");
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.anchor = GridBagConstraints.EAST;
		gbc_lblName.insets = new Insets(0, 0, 5, 5);
		gbc_lblName.gridx = 0;
		gbc_lblName.gridy = 0;
		contentPane.add(lblName, gbc_lblName);

		txtName = new JTextField();
		txtName.setName("txtName");
		GridBagConstraints gbc_txtName = new GridBagConstraints();
		gbc_txtName.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtName.insets = new Insets(0, 0, 5, 0);
		gbc_txtName.gridwidth = 3;
		gbc_txtName.gridx = 1;
		gbc_txtName.gridy = 0;
		contentPane.add(txtName, gbc_txtName);
		txtName.setColumns(10);

		// Name text box listener.
		txtName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				btnCreatePlaylist.setEnabled(!txtName.getText().trim().isEmpty());
			}
		});

		btnCreatePlaylist = new JButton("Create playlist");
		btnCreatePlaylist.setEnabled(false);
		btnCreatePlaylist.setName("btnCreatePlaylist");
		GridBagConstraints gbc_btnCreatePlaylist = new GridBagConstraints();
		gbc_btnCreatePlaylist.anchor = GridBagConstraints.WEST;
		gbc_btnCreatePlaylist.insets = new Insets(0, 0, 5, 5);
		gbc_btnCreatePlaylist.gridx = 1;
		gbc_btnCreatePlaylist.gridy = 1;
		contentPane.add(btnCreatePlaylist, gbc_btnCreatePlaylist);

		// Create playlist button listener.
		btnCreatePlaylist.addActionListener(e -> {
			musicStoreController.createPlaylist(new Playlist(txtName.getText()));
			closeDialog();
		});

		btnCancel = new JButton("Cancel");
		btnCancel.setName("btnCancel");
		GridBagConstraints gbc_btnCancel = new GridBagConstraints();
		gbc_btnCancel.anchor = GridBagConstraints.EAST;
		gbc_btnCancel.insets = new Insets(0, 0, 5, 0);
		gbc_btnCancel.gridx = 3;
		gbc_btnCancel.gridy = 1;
		contentPane.add(btnCancel, gbc_btnCancel);

		// Cancel button listener.
		btnCancel.addActionListener(e -> closeDialog());

		lblErrorMessage = new JLabel(" ");
		lblErrorMessage.setName("lblErrorMessage");
		lblErrorMessage.setForeground(new Color(255, 0, 0));
		GridBagConstraints gbc_lblErrorMessage = new GridBagConstraints();
		gbc_lblErrorMessage.insets = new Insets(0, 0, 0, 5);
		gbc_lblErrorMessage.gridwidth = 4;
		gbc_lblErrorMessage.gridx = 0;
		gbc_lblErrorMessage.gridy = 2;
		contentPane.add(lblErrorMessage, gbc_lblErrorMessage);
	}


	public void closeDialog() {
		this.dispose();
	}

	public void displayError(String message) {
		lblErrorMessage.setText(message);
	}
}
