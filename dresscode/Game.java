package dresscode;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Game {
	private static Random r;
	private static Scanner sc;
	private static boolean exit;
	public static void main(String[] args) throws IOException {
		  r = new Random();
		  sc = new Scanner(System.in);
		  exit = false;

		  while(!exit) {
			  newGame();
		  }
		
	}
	public static void newGame() {
		boolean isPlayer1 = r.nextBoolean();
		int move = 0;
		DecisionEngine de = new DecisionEngine();
		State state = new State();
		if(!isPlayer1) {
			move = de.findMaxValuePlay(state.toString());
			state.playMove(move);
		}
		while (state.getTurnNumber() != 16) {
			do {
				state.prettyPrint();
				System.out.println("Your move:");
				move = sc.nextInt();
			}while(!state.playMove(move));
			if (state.hasVictor(move)) {
				state.prettyPrint();
				System.out.println("YOU HAVE WON! Play again? (1) yes | (0) no");
				int playAgain = sc.nextInt();
				if (playAgain == 0)
					exit = true;
				return;
			}
				if(state.getTurnNumber()==16) break;
				state.prettyPrint();
				move = de.findMaxValuePlay(state.toString());
				state.playMove(move);
				if (state.hasVictor(move)) {
					state.prettyPrint();
					System.out.println("YOU HAVE LOST! Play again? (1) yes | (0) no");
					int playAgain = sc.nextInt();
					if (playAgain == 0)
						exit = true;
					return;
				}
			
		}
		state.prettyPrint();
		System.out.println("IT'S A DRAW! Play again? (1) yes | (0) no");
		int playAgain = sc.nextInt();
		if (playAgain == 0)
			exit = true;
		return;
		
	}
}
