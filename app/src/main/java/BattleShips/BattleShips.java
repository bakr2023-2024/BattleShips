package BattleShips;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

import com.google.common.base.Objects;
import java.util.Arrays;

enum UPPER {
	HIT, MISS, EMPTY;
}

enum LOWER {
	CARRIER, BATTLESHIP, CRUISER, SUBMARINE, DESTROYER, DESTROYED, EMPTY;
}

public class BattleShips {
	int N = 10;
	Player current, winner;
	ArrayList<Player> players;
	ArrayList<Player> party;
	HashMap<LOWER, Integer> ships;
	private int menuChoice = 0;
	private ArrayList<String> targetInfo = null;
	private int surr = 0;
	private boolean waiting = true;
	PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	Controller controller = null;
	int rounds;

	BattleShips(ArrayList<String> names) {
		rounds = 0;
		players = new ArrayList<Player>();
		setFleet();
		for (String name : names)
			players.add(new Player(name));
		this.setUpUpperGrids();
		Collections.shuffle(players);
		pcs.addPropertyChangeListener(e -> {
			String prop = e.getPropertyName();
			if (prop.equals("doneSetting"))
				waiting = false;
			else if (prop.equals("doneChoosing"))
				menuChoice = (Integer) e.getNewValue();
			else if (prop.equals("doneSurrendering"))
				surr = (Integer) e.getNewValue();
			else if (prop.equals("doneAttacking")) {
				if (e.getNewValue() instanceof ArrayList<?>)
					targetInfo = (ArrayList<String>) e.getNewValue();
			} else if (prop.equals("doneScouting"))
				waiting = false;
		});
		party = new ArrayList<Player>(players);
	}

	void setFleet() {
		LOWER[] arr = { LOWER.CARRIER, LOWER.BATTLESHIP, LOWER.CRUISER, LOWER.SUBMARINE, LOWER.DESTROYER };
		int[] health = { 5, 4, 3, 3, 2 };
		ships = new HashMap<LOWER, Integer>();
		for (int i = 0; i < arr.length; i++)
			ships.put(arr[i], health[i]);
	}

	class Player {
		HashMap<String, UPPER[][]> upperGrids;
		LOWER[][] lowerGrid;
		HashMap<LOWER, Integer> playerShips;
		boolean lost = false;
		String name;
		int attacks;
		int hits;
		int comboHits;
		int sunk;
		int kills;

		Player(String name) {
			playerShips = new HashMap<LOWER, Integer>(ships);
			this.name = name;
			lowerGrid = new LOWER[N][N];
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					lowerGrid[i][j] = LOWER.EMPTY;
				}
			}
			attacks = hits = comboHits = sunk = 0;
		}

		void surrender() {
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					this.lowerGrid[i][j] = LOWER.DESTROYED;
				}
			}
			this.lost = true;
			controller.callPublish(new String[] { this.name + " has surrendered:(", "updateLog" });
		}

		void attack(Player opponent, int row, int column) {
			current.attacks += 1;
			if (opponent.lowerGrid[row][column] == LOWER.EMPTY) {
				current.comboHits = 0;
				UPPER[][] updated = this.upperGrids.get(opponent.name);
				updated[row][column] = UPPER.MISS;
				this.upperGrids.put(opponent.name, updated);
				controller.callPublish(
						new String[] { this.name + " MISSed " + opponent.name + "'s fleet*", "updateLog" });
			} else if (opponent.lowerGrid[row][column] == LOWER.DESTROYED) {
				current.comboHits = 0;
				UPPER[][] updated = this.upperGrids.get(opponent.name);
				updated[row][column] = UPPER.MISS;
				this.upperGrids.put(opponent.name, updated);
				controller.callPublish(new String[] {
						this.name + " wasted an attack on a DESTROYED part of " + opponent.name + "'s fleet*",
						"updateLog" });
			} else {
				current.hits += 1;
				current.comboHits += 1;
				LOWER type = opponent.lowerGrid[row][column];
				opponent.lowerGrid[row][column] = LOWER.DESTROYED;
				opponent.playerShips.put(type, opponent.playerShips.get(type) - 1);
				UPPER[][] updated = this.upperGrids.get(opponent.name);
				updated[row][column] = UPPER.HIT;
				this.upperGrids.put(opponent.name, updated);
				controller.callPublish(
						new String[] { this.name + " successfully HIT " + opponent.name + "'s fleet^", "updateLog" });
				if (opponent.playerShips.get(type) == 0) {
					current.sunk += 1;
					controller.callPublish(new String[] {
							this.name + " DESTROYED " + opponent.name + "'s " + type.toString() + "^", "updateLog" });
					opponent.playerShips.remove(type);
					if (opponent.playerShips.size() == 0) {
						current.kills += 1;
						controller.callPublish(
								new String[] { opponent.name + " lost their entire fleet>:)", "updateLog" });
						opponent.lost = true;
					}
				}
			}
		}
	}

	Player checkWinner() {
		ArrayList<Player> winners = new ArrayList<Player>(players);
		winners.removeIf(n -> n.lost);
		if (winners.size() == 1)
			return winners.get(0);
		return null;
	}

	public void delay(int delay) {
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	Player getPlayer(String name) {
		for (Player player : players) {
			if (player.name.equals(name))
				return player;
		}
		return null;
	}

	void userChoice() {
		if (menuChoice == 1) {
			targetInfo = null;
			do {
				delay(200);
			} while (targetInfo == null);
			if (targetInfo.size() != 0) {
				String name = targetInfo.get(0);
				String row = targetInfo.get(1);
				String column = targetInfo.get(2);
				current.attack(getPlayer(name), Integer.parseInt(row), Integer.parseInt(column));
			} else {
				menuChoice = -1;
			}
		} else if (menuChoice == 2) {
			waiting = true;
			do {
				delay(200);
			} while (waiting);
		} else if (menuChoice == 3) {
			surr = 0;
			do {
				delay(200);
			} while (surr == 0);
			if (surr == 1)
				current.surrender();
			else
				menuChoice = -1;
		}
	}

	void attackingPhase() {
		while (winner == null) {
			rounds++;
			for (Player player : players) {
				if (menuChoice != -1) {
					if (player.lost)
						continue;
					current = player;
					controller.callPublish(new String[] { "", "prompt" });
				}
				menuChoice = 0;
				do {
					delay(100);
				} while (menuChoice == 0);
				userChoice();
				winner = checkWinner();
				if (winner != null)
					break;
			}
			players.removeIf(n -> n.lost);
		}
		controller.callPublish(new String[] { winner.name + " has won the game!", "updateLog" });
	}

	ArrayList<Record> findHighestAchievers() {
		double maxAccuracy = 0.0;
		Player maxAccuracyPlayer = null;
		int maxSunk = 0;
		Player maxSunkPlayer = null;
		int maxComboHits = 0;
		Player maxComboHitsPlayer = null;
		int maxShots = 0;
		Player maxShotsPlayer = null;
		int maxKills = 0;
		Player maxKillsPlayer = null;
		for (Player p : party) {
			double accuracy = (double) p.hits / p.attacks;
			if (accuracy > maxAccuracy) {
				maxAccuracy = accuracy;
				maxAccuracyPlayer = p;
			}
			if (p.sunk > maxSunk) {
				maxSunk = p.sunk;
				maxSunkPlayer = p;
			}
			if (p.comboHits > maxComboHits) {
				maxComboHits = p.comboHits;
				maxComboHitsPlayer = p;
			}
			if (p.attacks > maxShots) {
				maxShots = p.attacks;
				maxShotsPlayer = p;
			}
			if (p.kills > maxKills) {
				maxKills = p.kills;
				maxKillsPlayer = p;
			}
		}
		ArrayList<Record> record = new ArrayList<>();
		String accVal = String.format("%.2f", (double) (maxAccuracyPlayer.hits / maxAccuracyPlayer.attacks) * 100);
		record.add(new Record(maxAccuracyPlayer.name, "Highest accuracy", accVal + "%"));
		record.add(new Record(maxSunkPlayer.name, "Most ships sunk", maxSunkPlayer.sunk + ""));
		record.add(new Record(maxComboHitsPlayer.name, "Most combo hits", maxComboHitsPlayer.comboHits + ""));
		record.add(new Record(maxShotsPlayer.name, "Most shots fired", maxShotsPlayer.attacks + ""));
		record.add(new Record(maxKillsPlayer.name, "Most kills", maxKillsPlayer.kills + ""));
		return record;
	}

	class Record {
		String name;
		String field;
		String value;

		Record(String name, String field, String value) {
			this.name = name;
			this.field = field;
			this.value = value;
		}
	}

	void setUpUpperGrids() {
		for (Player player : players) {
			player.upperGrids = new HashMap<String, UPPER[][]>();
			ArrayList<Player> opponents = new ArrayList<Player>(players);
			opponents.remove(player);
			for (Player p : opponents) {
				player.upperGrids.put(p.name, new UPPER[N][N]);
			}
			for (String key : player.upperGrids.keySet()) {
				for (int i = 0; i < N; i++)
					for (int j = 0; j < N; j++)
						player.upperGrids.get(key)[i][j] = UPPER.EMPTY;
			}
		}
	}

	void settingPhase() {
		for (Player player : players) {
			current = player;
			waiting = true;
			controller.callPublish(new String[] { "", "setting" });
			do {
				delay(100);
			} while (waiting);
		}
		controller.callPublish(new String[] { "", "doneSetting" });
	}

	void playGame() {
		settingPhase();
		attackingPhase();
	}
}
