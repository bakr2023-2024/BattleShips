package BattleShips;

import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class Attack extends UpperGrid {
	private static final long serialVersionUID = -388770784590392251L;

	public Attack(BattleShips model) {
		this.game = model;
		Object[] opponents = model.current.upperGrids.keySet().toArray();
		String[] arr = new String[opponents.length];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = opponents[i].toString();
		}
		initComponents(arr);
		for (ActionListener l : cancel.getActionListeners())
			cancel.removeActionListener(l);
		cancel.addActionListener(e -> {
			ArrayList<String> arra = new ArrayList<String>();
			game.pcs.firePropertyChange("doneAttacking", arra, arra);
			dispose();
		});
		lblNewLabel.setText("Attacking " + arr[0]);
		label1.setText(model.current.name + "'s WarRoom");
		for (int i = 0; i < game.N; i++) {
			for (int j = 0; j < game.N; j++) {
				final int r = i, c = j;
				labels[i][j].addMouseListener(new MouseListener() {
					@Override
					public void mouseClicked(MouseEvent e) {
						String opponent = (String) comboBox.getSelectedItem();
						if (game.current.upperGrids.get(opponent)[r][c] != UPPER.EMPTY)
							lblNewLabel.setText("you already attacked this cell, choose another one");
						else {
							ArrayList<String> attack = new ArrayList<String>();
							attack.add(opponent);
							attack.add("" + r);
							attack.add("" + c);
							game.pcs.firePropertyChange("doneAttacking", null, attack);
							dispose();
						}
					}

					@Override
					public void mousePressed(MouseEvent e) {
					}

					@Override
					public void mouseReleased(MouseEvent e) {
					}

					@Override
					public void mouseEntered(MouseEvent e) {
					}

					@Override
					public void mouseExited(MouseEvent e) {
					}

				});
			}
		}
	}
}
