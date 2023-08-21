package BattleShips;

import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextArea;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Vector;

public class UpperGrid extends JDialog {
	BattleShips game;

	public UpperGrid() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

	}

	public UpperGrid(BattleShips model) {
		this.game = model;
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		Object[] arrr = model.current.upperGrids.keySet().toArray();
		String[] arr = new String[arrr.length];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = arrr[i].toString();
		}
		initComponents(arr);

	}

	JLabel[][] labels;

	void displayUpperGrid(UPPER[][] grid) {
		panel.removeAll();
		for (int i = 0; i < game.N; i++) {
			for (int j = 0; j < game.N; j++) {
				Image img = new ImageIcon(getClass().getResource("/" + grid[i][j].toString() + ".png")).getImage();
				labels[i][j].setIcon(new ImageIcon(img));
				panel.add(labels[i][j]);
			}
		}
	}

	JPanel panel;
	JLabel label1;
	JLabel lblNewLabel;
	JButton cancel;
	JComboBox comboBox;

	void initComponents(String[] opponents) {
		panel = new JPanel();
		label1 = new JLabel(game.current.name + "'s journal");
		lblNewLabel = new JLabel("Enemy's upper grid");
		labels = new JLabel[game.N][game.N];
		for (int i = 0; i < game.N; i++)
			for (int j = 0; j < game.N; j++) {
				labels[i][j] = new JLabel();
				labels[i][j].setPreferredSize(new Dimension(51, 51));
				labels[i][j].setIconTextGap(0);
			}
		getContentPane().setBackground(new Color(128, 128, 128));
		setBounds(100, 100, 953, 661);
		getContentPane().setLayout(null);
		panel.setBackground(new Color(0, 128, 192));
		panel.setBounds(315, 0, 624, 624);
		getContentPane().add(panel);
		panel.setLayout(new GridLayout(game.N, game.N, 0, 0));
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel.setBounds(10, 109, 173, 35);
		getContentPane().add(lblNewLabel);
		comboBox = new JComboBox();
		comboBox.setBounds(20, 145, 285, 27);
		getContentPane().add(comboBox);
		comboBox.setModel(new DefaultComboBoxModel<>(opponents));
		displayUpperGrid(game.current.upperGrids.get(opponents[0]));
		comboBox.addActionListener(e -> {
			String str = (String) comboBox.getSelectedItem();
			displayUpperGrid(game.current.upperGrids.get(str));
		});
		label1.setFont(new Font("Tahoma", Font.BOLD, 18));
		label1.setHorizontalAlignment(SwingConstants.CENTER);
		label1.setBounds(10, 22, 295, 65);
		cancel = new JButton("Return");
		cancel.setBounds(20, 180, 285, 27);
		cancel.addActionListener(e -> {
			game.pcs.firePropertyChange("doneScouting", false, false);
			dispose();
		});
		getContentPane().add(label1);
		getContentPane().add(cancel);
		this.addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent e) {
			}

			@Override
			public void windowClosing(WindowEvent e) {
				game.pcs.firePropertyChange("doneScouting", false, false);
				dispose();
			}

			@Override
			public void windowClosed(WindowEvent e) {
			}

			@Override
			public void windowIconified(WindowEvent e) {
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
			}

			@Override
			public void windowActivated(WindowEvent e) {
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
			}
		});
	}
}
