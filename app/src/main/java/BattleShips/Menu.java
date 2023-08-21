package BattleShips;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.InputStream;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.awt.event.ActionEvent;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;

public class Menu extends JFrame {

	private JPanel contentPane;

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

	/**
	 * Create the frame.
	 */
	public Menu() {
		setTitle("Menu");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 864, 592);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 128, 192));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		loopSound("/menu.wav");
		JLabel lblNewLabel = new JLabel("Battleships");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 80));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(145, 10, 565, 115);
		contentPane.add(lblNewLabel);

		JButton btnNewButton = new JButton("Start");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new GameLobby(null).setVisible(true);
				stopSound(clip);
				dispose();
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 40));
		btnNewButton.setBounds(287, 252, 217, 52);
		contentPane.add(btnNewButton);

		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnExit.setFont(new Font("Tahoma", Font.BOLD, 40));
		btnExit.setBounds(287, 493, 217, 52);
		contentPane.add(btnExit);

		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setBounds(62, 221, 102, 115);
		contentPane.add(lblNewLabel_1);

		JButton btnGameRules = new JButton("Rules");
		btnGameRules.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Create a JPanel with a GridLayout
				String rule1 = "BattleShips rules:\n1. Each player has 5 ships of different sizes on a 10x10 grid.\n";
				String rule2 = "2. The ships cannot overlap or touch each other.\n";
				String rule3 = "3. The players take turns to fire missiles at the opponent's grid.\n";
				String rule5 = "5. The first player to sink all the opponent's ships wins.\n\n";
				String glhf = "Good luck and have fun!\n";
				JOptionPane.showMessageDialog(null, rule1 + rule2 + rule3 + rule5 + glhf);
			}

		});
		btnGameRules.setFont(new Font("Tahoma", Font.BOLD, 40));
		btnGameRules.setBounds(287, 328, 217, 52);
		contentPane.add(btnGameRules);

		JButton btnCredits = new JButton("Credits");
		btnCredits.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(Menu.this,
						"This game was created by Bakr Mohamed Bakr using Java Swing.\nLinkedin: www.linkedin.com/in/bakr-mohamed-bakr-3b815a248\n"
								+ "Github: https://github.com/bakr2023-2024" + "\nGmail: bakr2023.2024@gmail.com\n\n"
								+ "This game is a fan-made tribute to BattleShip, a strategy type guessing game.\nThis game is not intended for commercial use and all rights belong to the original creator.\nFor more information about BattleShips, please visit\nThe Wikipedia page: https://en.wikipedia.org/wiki/Battleship_(game) \n\nThank you for playing and enjoy the game!",
						"Game Credits", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		btnCredits.setFont(new Font("Tahoma", Font.BOLD, 40));
		btnCredits.setBounds(287, 412, 217, 52);
		contentPane.add(btnCredits);

		JLabel lblNewLabel_2 = new JLabel("");
		Image img = new ImageIcon(getClass().getResource("/shipee.jpg")).getImage();
		lblNewLabel_2.setIcon(new ImageIcon(img));
		lblNewLabel_2.setBounds(589, 154, 217, 293);
		contentPane.add(lblNewLabel_2);
	}
}
