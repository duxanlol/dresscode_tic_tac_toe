package dresscode;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class StateGenerator {

	private Queue<State> queue;
	private Set<Integer> stateHistory; 
	private LinkedList<State> victoriousStatesPlayer1;
	private LinkedList<State> victoriousStatesPlayer2;
	private LinkedList<State> wonCurrentState;

	public StateGenerator() {
		queue = new LinkedList<State>();
		stateHistory = new HashSet<Integer>();
		victoriousStatesPlayer1 = new LinkedList<State>();
		victoriousStatesPlayer2 = new LinkedList<State>();

	}

	public boolean existsInHistory(State state) {
		if (stateHistory.contains(state.getState())) {
			return true;
		} else {
			stateHistory.add(state.getState());
			State cloneState = state.clone();
			//for(int rotation = 0; rotation < 3; rotation++) 
			//	stateHistory.add(cloneState.rotateClockwise());
		}
		return false;
	}

	public void generateAllWinningStates() {
		State startState = new State();
		queue.add(startState);
		int cnt = 0;
		while (!queue.isEmpty()) {
			processState(queue.poll());
			cnt++;
			if (cnt % 1000000 == 0) {
				System.out.println(""+cnt+"/8341357");
			}
		}
	}

	public void processState(State state) {
		for (int offset = 0; offset < 16; offset++) {
			State cloneState = state.clone();
			if (cloneState.playMove(offset)) { // Ako je potez validan
				if (!existsInHistory(cloneState)) {
					if (!cloneState.hasVictor(offset) && cloneState.getTurnNumber() <= 16) {
						queue.add(cloneState);
					} else {
						if(cloneState.getTurnNumber() % 2 == 1) 
							wonCurrentState = victoriousStatesPlayer1;
						else
							wonCurrentState = victoriousStatesPlayer2;	
						wonCurrentState.add(cloneState);
						//for(int rotation = 0; rotation < 3; rotation++) {
						//	cloneState.rotateClockwise();
						//	wonCurrentState.add(cloneState);
						//}
					}
				}
			}
		}
	}

	

	public LinkedList<State> getVictoriousStatesPlayer1() {
		return victoriousStatesPlayer1;
	}

	public void setVictoriousStatesPlayer1(LinkedList<State> victoriousStatesPlayer1) {
		this.victoriousStatesPlayer1 = victoriousStatesPlayer1;
	}

	public LinkedList<State> getVictoriousStatesPlayer2() {
		return victoriousStatesPlayer2;
	}

	public void setVictoriousStatesPlayer2(LinkedList<State> victoriousStatesPlayer2) {
		this.victoriousStatesPlayer2 = victoriousStatesPlayer2;
	}
	
	
}
