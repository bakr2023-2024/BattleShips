package BattleShips;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.Image;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.BevelBorder;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Color;

public class GameLobby extends JFrame {

	private JPanel contentPane;
	private JTable table;

	String promptName() {
		return JOptionPane.showInputDialog(null, "Enter name: ");
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
		setBounds(100, 100, 1016, 603);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 0, 0));
		setContentPane(contentPane);
		JButton btnNewButton = new JButton("Add");
		btnNewButton.setForeground(new Color(255, 128, 0));
		btnNewButton.setBackground(new Color(0, 255, 0));
		btnNewButton.setBounds(707, 112, 257, 70);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String str = promptName();
				DefaultTableModel tblModel = (DefaultTableModel) table.getModel();
				if (str == null)
					return;
				if (tblModel.getRowCount() >= 8) {
					JOptionPane.showMessageDialog(null, "Party is full");
					return;
				}
				if (str.contains(" ") || str.equals("")) {
					JOptionPane.showMessageDialog(null, str + " contains whitespace");
					return;
				} else {
					for (int i = 0; i < tblModel.getRowCount(); i++) {
						if (str.equals((String) table.getValueAt(i, 0))) {
							JOptionPane.showMessageDialog(null, str + " is duplicated");
							return;
						}
					}
					String[] arr = { str };
					tblModel.addRow(arr);
				}
			}
		});
		contentPane.setLayout(null);
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 35));
		contentPane.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("Confirm");
		btnNewButton_1.setForeground(new Color(255, 255, 255));
		btnNewButton_1.setBackground(new Color(128, 0, 128));
		btnNewButton_1.setBounds(707, 406, 257, 70);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel tblModel = (DefaultTableModel) table.getModel();
				if (tblModel.getRowCount() < 2 || tblModel.getRowCount() > 8) {
					JOptionPane.showMessageDialog(null, "Make sure the party is between 2~8");
					return;
				}
				ArrayList<String> arr = new ArrayList<String>();
				for (int i = 0; i < tblModel.getRowCount(); i++) {
					arr.add((String) table.getValueAt(i, 0));
				}
				new Game(new BattleShips(arr)).setVisible(true);
				stopSound(clip);
				dispose();
			}
		});
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 35));
		contentPane.add(btnNewButton_1);

		JButton btnNewButton_1_1 = new JButton("Remove");
		btnNewButton_1_1.setForeground(new Color(0, 0, 255));
		btnNewButton_1_1.setBackground(new Color(255, 0, 0));
		btnNewButton_1_1.setBounds(707, 198, 257, 70);
		btnNewButton_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel tblModel = (DefaultTableModel) table.getModel();
				if (table.getSelectedRowCount() == 1) {
					tblModel.removeRow(table.getSelectedRow());
				} else if (table.getRowCount() == 0) {
					JOptionPane.showMessageDialog(null, "Party is Empty");
				} else if (table.getSelectedRowCount() == 0) {
					JOptionPane.showMessageDialog(null, "Please select a player to remove");
				}
			}
		});
		btnNewButton_1_1.setFont(new Font("Tahoma", Font.BOLD, 35));
		contentPane.add(btnNewButton_1_1);

		table = new JTable();
		table.setBounds(10, 10, 663, 546);
		table.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		table.setFillsViewportHeight(true);
		table.setFont(new Font("Mona Lisa Recut", Font.PLAIN, 50));
		table.setRowHeight(68);
		table.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Name" }) {
			Class[] columnTypes = new Class[] { String.class };

			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		contentPane.add(table);

		JButton btnNewButton_1_2 = new JButton("Main Menu");
		btnNewButton_1_2.setBackground(new Color(128, 128, 0));
		btnNewButton_1_2.setBounds(707, 486, 257, 70);
		btnNewButton_1_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Menu().setVisible(true);
				stopSound(clip);
				dispose();
			}
		});
		btnNewButton_1_2.setFont(new Font("Tahoma", Font.BOLD, 35));
		contentPane.add(btnNewButton_1_2);
		JLabel lblNewLabel = new JLabel("Game Lobby");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 40));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(683, 10, 309, 92);
		contentPane.add(lblNewLabel);
	}

	public GameLobby(ArrayList<BattleShips.Player> party) {
		initComponents();
		loopSound("/lobby.wav");
		if (party != null) {
			DefaultTableModel tblModel = (DefaultTableModel) table.getModel();
			for (BattleShips.Player par : party) {
				tblModel.addRow(new String[] { par.name });
			}
		}
	}
}
