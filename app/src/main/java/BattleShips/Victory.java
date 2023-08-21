package BattleShips;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.Color;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;

public class Victory extends JFrame {

	private JPanel contentPane;
	JLabel lblNewLabel;
	JButton btnNewButton, btnMainMenu;
	Game game;
	private JTable table;

	public Victory(Game game) {
		this.game = game;
		initComponents();
		loopSound("/menu.wav");
		this.lblNewLabel.setText(game.controller.model.winner.name + " has won the game!!");
		ArrayList<BattleShips.Record> record = game.game.findHighestAchievers();
		DefaultTableModel tblModel = (DefaultTableModel) table.getModel();
		for (BattleShips.Record rec : record) {
			tblModel.addRow(new String[] { rec.field, rec.name, rec.value });
		}
		table.repaint();
		table.revalidate();
	}

	Clip clip = null;

	public Clip loopSound(String soundFilePath) {
		try {
			// wrap the input stream inside a buffered stream
			InputStream inputStream = new BufferedInputStream(getClass().getResourceAsStream(soundFilePath));
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(inputStream);
			clip = AudioSystem.getClip();
			clip.open(audioStream);
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clip;
	}

	public void stopSound(Clip clip) {
		if (clip != null) {
			clip.stop();
		}
	}

	void initComponents() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 980, 543);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 128, 192));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		lblNewLabel = new JLabel("New label");
		lblNewLabel.setForeground(new Color(255, 255, 0));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 45));
		lblNewLabel.setBounds(10, 10, 956, 123);
		contentPane.add(lblNewLabel);
		btnNewButton = new JButton("Play again");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new GameLobby(game.game.party).setVisible(true);
				stopSound(clip);
				Victory.this.dispose();
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 45));
		btnNewButton.setForeground(new Color(0, 0, 255));
		btnNewButton.setBackground(new Color(255, 0, 128));
		btnNewButton.setBounds(64, 203, 300, 71);
		contentPane.add(btnNewButton);
		btnMainMenu = new JButton("Main menu");
		btnMainMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Menu().setVisible(true);
				stopSound(clip);
				Victory.this.dispose();
			}
		});
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(374, 97, 582, 399);
		contentPane.add(scrollPane);
		table = new JTable();
		table.setFont(new Font("Eurostile", Font.PLAIN, 16));
		scrollPane.setViewportView(table);
		table.setFillsViewportHeight(true);
		table.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Record", "Player", "Value" }) {
			Class[] columnTypes = new Class[] { String.class, String.class, String.class };

			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		table.getColumnModel().getColumn(0).setPreferredWidth(257);
		table.getColumnModel().getColumn(1).setPreferredWidth(250);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		table.setRowHeight(75);
		table.setForeground(new Color(0, 255, 0));
		table.setBackground(new Color(0, 0, 0));
		btnMainMenu.setFont(new Font("Tahoma", Font.BOLD, 45));
		btnMainMenu.setBackground(new Color(255, 0, 128));
		btnMainMenu.setForeground(new Color(0, 0, 255));
		btnMainMenu.setBounds(64, 310, 300, 71);
		contentPane.add(btnMainMenu);
	}
}
