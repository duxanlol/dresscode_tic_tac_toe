package dresscode;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

public class DecisionEngine {

	public LinkedList<State> player1WinStates;
	public LinkedList<State> player2WinStates;
	private int player1 = 1;
	private int player2 = 2;

	public DecisionEngine() {
		player1WinStates = new LinkedList<State>();
		player2WinStates = new LinkedList<State>();
		player1WinStates.addAll(DataHandler.getInstance().getVictoriousStatesPlayer1());
		player2WinStates.addAll(DataHandler.getInstance().getVictoriousStatesPlayer2());
		System.out.println("Player 1 size " + player1WinStates.size());
		System.out.println("Player 2 size " + player2WinStates.size());

	}


	public int purgeStatesByStateMask(int state) {
		int removedStates = 0;
		Iterator<State> itr = player1WinStates.iterator();
		while (itr.hasNext()) {
			State currentState = itr.next();
			if ((currentState.getState() & state) != state) {
				itr.remove();
				removedStates++;
			}
		}

		itr = player2WinStates.iterator();
		while (itr.hasNext()) {
			State currentState = itr.next();
			if ((currentState.getState() & state) != state) {
				itr.remove();
				removedStates++;
			}
		}
		System.out.println("Purged:" + removedStates + "states.");
		return removedStates;
	}

	public int findMaxValuePlay(String stateString) {
		purgeStatesByStateMask(Utility.convertStateStringToMask(stateString));
		int[][] p1WinsCount = new int[16][17];
		int[][] p2WinsCount = new int[16][17];
		boolean player1Turn = true;
		LinkedList<Integer> emptyFieldIndexes = getEmptyFieldIndexesByStateString(stateString);
		int turnNumber = 17 - emptyFieldIndexes.size();
		if (turnNumber % 2 == 0)
			player1Turn = false;

		int p1EarliestVictoryTurn = 15;
		int p2EarliestVictoryTurn = 16;

		int[] p1TurnsToWin = new int[17];
		int[] p2TurnsToWin = new int[17];

		// PLAYER 1 WINS STATES

		ListIterator<State> itr = player1WinStates.listIterator();
		while (itr.hasNext()) {
			State stateObject = itr.next();
			int currentState = stateObject.getState();
			int stateTurnNumber = stateObject.getTurnNumber();
			p1TurnsToWin[stateTurnNumber]++;
			for (int i = 15; i >= 0; i--) {
				if ((currentState & player1) == player1) {
					p1WinsCount[i][stateTurnNumber]++;
					if (stateTurnNumber < p1EarliestVictoryTurn)
						p1EarliestVictoryTurn = stateTurnNumber;
				}
				currentState >>= 2;
			}
		}

		// PLAYER 2 WINS STATES

		itr = player2WinStates.listIterator();
		while (itr.hasNext()) {
			State stateObject = itr.next();
			int currentState = stateObject.getState();
			int stateTurnNumber = stateObject.getTurnNumber();
			p2TurnsToWin[stateTurnNumber]++;
			for (int i = 15; i >= 0; i--) {
				if ((currentState & player2) == player2) {
					p2WinsCount[i][stateTurnNumber]++;
					if (stateTurnNumber < p2EarliestVictoryTurn)
						p2EarliestVictoryTurn = stateTurnNumber;
				}
				currentState >>= 2;
			}
		}
		/*
		System.out.println("turn number: " + turnNumber + "earliest p1 win:"+p1EarliestVictoryTurn+"earliest p2 win:"+p2EarliestVictoryTurn);
		System.out.println("P1 POSSIBLE VICTORY STATES PER TURN");
		for (int i = 0; i < 17; i++) {
			System.out.print(p1TurnsToWin[i] + " ");
		}
		System.out.println();
		System.out.println("P2 POSSIBLE VICTORY STATES PER TURN");
		for (int i = 0; i < 17; i++) {
			System.out.print(p2TurnsToWin[i] + " ");
		}
	
		System.out.println();
			*/
		int bestChoice = -1;
		int[] possibleWinningPlays = new int[16];
		if (player1Turn) {
			if (p1EarliestVictoryTurn < p2EarliestVictoryTurn)
				bestChoice= calculateBestPlayIfWinning(p1WinsCount, p2WinsCount, emptyFieldIndexes, p1EarliestVictoryTurn,
						p2EarliestVictoryTurn, 1);
			else
				bestChoice= calculateBestPlayIfLosing(p2WinsCount, p1WinsCount, emptyFieldIndexes, p2EarliestVictoryTurn,
						p1EarliestVictoryTurn,1);

		} else { // player2Turn
			if (p2EarliestVictoryTurn < p1EarliestVictoryTurn)
				bestChoice= calculateBestPlayIfWinning(p2WinsCount, p1WinsCount, emptyFieldIndexes, p2EarliestVictoryTurn,
						p1EarliestVictoryTurn, 2);
			else 
				bestChoice= calculateBestPlayIfLosing(p1WinsCount, p2WinsCount, emptyFieldIndexes, p1EarliestVictoryTurn,
						p2EarliestVictoryTurn, 2);
		}
		
		//System.out.println("It's player 1's turn: " + player1Turn);
		
		//System.out.println("The earliest turn p1 can win is " + p1EarliestVictoryTurn);
		//System.out.println("The earliest turn p2 can win is " + p2EarliestVictoryTurn);
		
		//System.out.println("IZABRAO SAM CHOICE JE " + bestChoice);
		
		return bestChoice;
	}

	private int calculateBestPlayIfLosing(int[][] victoryStatesLeader, int[][] victoryStatesLoser,
			LinkedList<Integer> emptyFieldIndexes, int leaderEarliestVictoryTurn, int loserEarliestVictoryTurn, int player) {
		LinkedList<Integer> maxDamageIndexes = new LinkedList<Integer>();
		int maxAllowedLoserDepth = 15;
		int maxAllowedLeaderDepth = 16;
		if(player == 2) {
			maxAllowedLoserDepth = 16;
			maxAllowedLeaderDepth = 15;
		}
		do { 
			// NADJI ONAJ KOJI RADI NAJVISE STETE PROTIVNIKU
			int maxDamageValue = Integer.MIN_VALUE;
			for (Integer i : emptyFieldIndexes) {
				if (maxDamageValue == Integer.MIN_VALUE) {
					maxDamageValue = victoryStatesLeader[i][leaderEarliestVictoryTurn];
					maxDamageIndexes.add(i);
				} else {
					if (victoryStatesLeader[i][leaderEarliestVictoryTurn] > maxDamageValue) {
						maxDamageValue = victoryStatesLeader[i][leaderEarliestVictoryTurn];
						maxDamageIndexes.clear();
						maxDamageIndexes.add(i);
					} else if (victoryStatesLeader[i][leaderEarliestVictoryTurn] == maxDamageValue) {
						maxDamageIndexes.add(i);
					}
				}
				
			}
			// AKO IMA VISE ONIH KOJI RADE ISTU NAJVECU STETU NADJI ONAJ KOJI TEBI NAJVISE
			// ODGOVARA

			if (maxDamageIndexes.size() > 1 && loserEarliestVictoryTurn <= maxAllowedLoserDepth) {
				int bestPlayValue = Integer.MIN_VALUE;
				LinkedList<Integer> bestPlayIndexes = new LinkedList<Integer>();
				for (Integer i : maxDamageIndexes) {
					if (bestPlayValue == Integer.MIN_VALUE) {
						bestPlayValue = victoryStatesLoser[i][loserEarliestVictoryTurn];
						bestPlayIndexes.add(i);
					} else {
						if (victoryStatesLoser[i][loserEarliestVictoryTurn] > bestPlayValue) {
							bestPlayValue = victoryStatesLoser[i][loserEarliestVictoryTurn];
							bestPlayIndexes.clear();
							bestPlayIndexes.add(i);
						} else if (victoryStatesLoser[i][loserEarliestVictoryTurn] == bestPlayValue) {
							bestPlayIndexes.add(i);
						}
					}
				}
				maxDamageIndexes = bestPlayIndexes;
			}
			
			leaderEarliestVictoryTurn += 2;
			loserEarliestVictoryTurn += 2;
		} while (maxDamageIndexes.size() > 1  && leaderEarliestVictoryTurn <= maxAllowedLeaderDepth);
		return maxDamageIndexes.getFirst();
	}

	private int calculateBestPlayIfWinning(int[][] victoryStatesLeader, int[][] victoryStatesLoser,
			LinkedList<Integer> emptyFieldIndexes, int leaderEarliestVictoryTurn, int loserEarliestVictoryTurn, int player) {
		int maxAllowedLeaderDepth = 15;
		int maxAllowedLoserDepth = 16;
		if(player == 2) {
			maxAllowedLeaderDepth = 16;
			maxAllowedLoserDepth = 15;
		}
		boolean goDeep = true;
		LinkedList<Integer> bestPlayIndexes = new LinkedList<Integer>();
		do {
				// NADJI ONAJ KOJI TE U NAJVISE NACINA VODI DO NAJBRZE POBEDE
			int bestPlayValue = Integer.MIN_VALUE;
			for (Integer i : emptyFieldIndexes) {
				if (bestPlayValue == Integer.MIN_VALUE) {
					bestPlayValue = victoryStatesLeader[i][leaderEarliestVictoryTurn];
					bestPlayIndexes.add(i);
				} else {
					if (victoryStatesLeader[i][leaderEarliestVictoryTurn] > bestPlayValue) {
						bestPlayValue = victoryStatesLeader[i][leaderEarliestVictoryTurn];
						bestPlayIndexes.clear();
						bestPlayIndexes.add(i);
					} else if (victoryStatesLeader[i][leaderEarliestVictoryTurn] == bestPlayValue) {
						bestPlayIndexes.add(i);
					}
				}
			}
			
			if(!goDeep) return bestPlayIndexes.getLast();
			// AKO IMA VISE NAJBOLJIH ODIGRAJ ONAJ KOJI JE NAJGORI ZA PROTIVNIKA
			if (bestPlayIndexes.size() > 1 && loserEarliestVictoryTurn <= maxAllowedLoserDepth) {
				int maxDamageValue = Integer.MIN_VALUE;
				LinkedList<Integer> maxDamageIndexes = new LinkedList<Integer>();
				for (Integer i : bestPlayIndexes) {
					if (maxDamageValue == Integer.MIN_VALUE) {
						maxDamageValue = victoryStatesLoser[i][loserEarliestVictoryTurn];
						maxDamageIndexes.add(i);
					} else {
						if (victoryStatesLoser[i][loserEarliestVictoryTurn] > maxDamageValue) {
							maxDamageValue = victoryStatesLoser[i][loserEarliestVictoryTurn];
							maxDamageIndexes.clear();
							maxDamageIndexes.add(i);
						} else if (victoryStatesLoser[i][loserEarliestVictoryTurn] == maxDamageValue) {
							maxDamageIndexes.add(i);
						}
					}
				}
				
				if(maxDamageIndexes.size() < bestPlayIndexes.size()) {
					
				}
				bestPlayIndexes = maxDamageIndexes;
			}
			leaderEarliestVictoryTurn += 2;
			loserEarliestVictoryTurn += 2;
		} while (bestPlayIndexes.size() > 1 && leaderEarliestVictoryTurn <= maxAllowedLeaderDepth);

		return bestPlayIndexes.getFirst();
	}


	private LinkedList<Integer> getEmptyFieldIndexesByStateString(String stateString) {
		LinkedList<Integer> emptyFieldIndexes = new LinkedList<Integer>();
		for (int i = 0; i < stateString.length(); i++) {
			if (stateString.charAt(i) == '0')
				emptyFieldIndexes.add(i);
		}
		return emptyFieldIndexes;
	}


	public LinkedList<State> getPlayerWinsStates() {
		return player1WinStates;
	}

	public void setPlayerWinsStates(LinkedList<State> playerWinsStates) {
		this.player1WinStates = playerWinsStates;
	}

	public LinkedList<State> getOpponentWinsStates() {
		return player2WinStates;
	}

	public void setOpponentWinsStates(LinkedList<State> opponentWinsStates) {
		this.player2WinStates = opponentWinsStates;
	}



}
