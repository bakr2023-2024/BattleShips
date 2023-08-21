package BattleShips;

import java.awt.Image;
import java.util.*;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingWorker;

public class Controller extends SwingWorker<Void, Object> {

	BattleShips model = null;
	Game view = null;
	HashMap<LOWER, Integer> ships = null;

	Controller(Game game, BattleShips gameModel) {
		this.model = gameModel;
		this.view = game;
		lowerGridCopy = new LOWER[model.N][model.N];
		clearCopyGrid();
	}

	@Override
	public Void doInBackground() {
		view.makeButtons();
		model.playGame();
		return null;
	}

	public void doneSetting() {
		this.model.pcs.firePropertyChange("doneSetting", false, true);
	}

	public LOWER[][] lowerGridCopy;

	void clearCopyGrid() {
		for (int i = 0; i < model.N; i++)
			for (int j = 0; j < model.N; j++)
				lowerGridCopy[i][j] = LOWER.EMPTY;
	}

	void applyChanges() {
		for (int i = 0; i < model.N; i++) {
			for (int j = 0; j < model.N; j++) {
				model.current.lowerGrid[i][j] = lowerGridCopy[i][j];
			}
		}
		clearCopyGrid();
		model.pcs.firePropertyChange("doneSetting", true, false);
	}

	public boolean insertShip(int i, int j, boolean horizontal, LOWER type, int size, JLabel[][] arr) {
		int p;
		if (horizontal) {
			if (j + size > view.N) {
				view.lblNewLabel_2.setText(
						"Collision with a boundary detected, please make sure to place the ship in an empty area");
				return false;
			}
			for (p = j; p < j + size; p++) {
				if (lowerGridCopy[i][p] != LOWER.EMPTY) {
					view.lblNewLabel_2.setText(
							"Collision with another ship detected, please make sure to place the ship in an empty area");
					return false;
				}
			}
			for (p = j; p < j + size; p++) {
				lowerGridCopy[i][p] = type;
				Image img = new ImageIcon(getClass().getResource("/" + type.toString() + ".png")).getImage();
				arr[i][p].setIcon(new ImageIcon(img));
			}

		} else {
			if (i + size > view.N) {
				view.lblNewLabel_2.setText(
						"Collision with a boundary detected, please make sure to place the ship in an empty area");
				return false;
			}
			for (p = i; p < i + size; p++) {
				if (lowerGridCopy[p][j] != LOWER.EMPTY) {
					view.lblNewLabel_2.setText(
							"Collision with another ship detected, please make sure to place the ship in an empty area");
					return false;
				}
			}
			for (p = i; p < i + size; p++) {
				lowerGridCopy[p][j] = type;
				Image img = new ImageIcon(getClass().getResource("/" + type.toString() + ".png")).getImage();
				arr[i][p].setIcon(new ImageIcon(img));
			}
		}
		view.lblNewLabel_2.setText("Successfully placed " + type.toString());
		return true;
	}

	public void callPublish(Object data) {
		if (data instanceof String[]) {
			this.publish(data);
		}
	}

	public void process(List<Object> data) {
		for (Object item : data) {
			if (item instanceof String[]) {
				String[] arr = (String[]) item;
				String arg1 = arr[0], arg2 = arr[1];
				if (arg2.equals("updateLog")) {
					view.updateActionLog(arg1);
					if (arg1.endsWith("!"))
						view.endGame();
				} else if (arg2.equals("setting")) {
					view.addSettingBoard();
				} else if (arg2.equals("doneSetting")) {
					view.doneSetting();
				} else if (arg2.equals("prompt")) {
					view.addAttackingBoard();
				}
			}
		}
	}
}
