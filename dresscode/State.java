package dresscode;
import java.io.Serializable;

public class State implements Cloneable, Serializable {
	private int state;
	private int turnNumber;

	public State(int state, int turnNumber) {
		this.state = state;
		this.turnNumber = turnNumber;
	}

	public State(int state) {
		this.state = state;
		this.turnNumber = this.numberOfSetBits();
	}

	public State(String stateString) {
		this.state = Utility.convertStateStringToMask(stateString);
		this.turnNumber = this.numberOfSetBits();
	}

	public State() {
		this.state = 0;
		this.turnNumber = 0;
	}

	public State clone() {
		State clone = new State();
		clone.state = this.state;
		clone.turnNumber = this.turnNumber;
		return clone;
	}
													 //  0  1  2   3   4  5  6   7   8  9  10 11 12  13 14 15
	public static final byte[] ROTATION_SHIFT_STEPS = {  3, 7, 11, 15, 2, 6, 10, 14, 1, 5, 9, 13, 0, 4, 8, 12  };

	public int rotateClockwise() {
		int rotatedState = 0;
		int fieldIndex = 0;
		int currentBit = 0;
		int mask = 0b11000000000000000000000000000000;
		while (fieldIndex < 16) {
			currentBit = state & mask;
			state <<= 2;
			currentBit >>>= (ROTATION_SHIFT_STEPS[fieldIndex] * 2);
			rotatedState += currentBit;
			fieldIndex++;

		}
		this.state = rotatedState;
		return rotatedState;
	}

	public String toBinaryString() {
		String binaryWithOutLeading0 = Integer.toBinaryString(this.state);
		return "00000000000000000000000000000000".substring(binaryWithOutLeading0.length()) + binaryWithOutLeading0;
	}

	public String toString() {
		String stateString = "";
		int state = this.state;
		int fieldIndex = 15;
		int mask = 0b11;
		int currentBit = 0;
		while (fieldIndex >= 0) {
			currentBit = state & mask;
			if (currentBit == 1) {
				stateString = '1' + stateString;
			} else if (currentBit == 2) {
				stateString = '2' + stateString;

			} else {
				stateString = '0' + stateString;
			}
			state >>>= 2;
			fieldIndex--;
		}
		return stateString;
	}

	public int numberOfSetBits() {
		int i = this.state;
		i = i - ((i >>> 1) & 0x55555555);
		i = (i & 0x33333333) + ((i >> 2) & 0x33333333);
		return (((i + (i >> 4)) & 0x0F0F0F0F) * 0x01010101) >> 24;
	}

	public static final int ILLEGAL_MOVE = Integer.MIN_VALUE;
	public static final int PLAYER_1 = 1;
	public static final int PLAYER_2 = 2;
	public static final int PLAYER_1_MASK = 0b01000000000000000000000000000000;
	public static final int PLAYER_2_MASK = 0b10000000000000000000000000000000;
	public static final int[] VICTORY_STATE_MASKS = { 1347420160, 336855040, 84213760, // BOX 0-1
			5263360, 1315840, 328960, // BOX 1-2
			20560, 5140, 1285, // BOX 2-3
			1426063360, 5570560, 21760, 85, // COLLUMN
			1077952576, 269488144, 67372036, 16843009, // ROW
			1074791425, // DIAGONAL
			17043520 };// ANTIDIAGONAL

	public static final int[][] FIELD_VICTORY_STATE_MASKS = { { 0, 9, 13, 17 }, // 0
			{ 0, 1, 9, 14 }, // 1
			{ 1, 2, 9, 15 }, // ...
			{ 2, 9, 16, 18 }, { 0, 3, 10, 13 }, { 0, 1, 3, 4, 10, 14, 17 }, { 1, 2, 4, 5, 10, 15, 18 },
			{ 2, 5, 10, 16 }, { 3, 6, 11, 13 }, { 3, 4, 6, 7, 11, 14, 18 }, { 4, 5, 7, 8, 11, 15, 17 },
			{ 5, 8, 11, 16 }, { 6, 12, 13, 18 }, { 6, 7, 12, 14 }, { 7, 8, 12, 15 }, { 8, 12, 16, 17 } // 15
	};

	public boolean hasVictor(int offset) {
		if (this.turnNumber < 7)
			return false;
		int player2LeftShift = 0;
		if (this.turnNumber % 2 == 0)
			player2LeftShift = 1;
		int victoryMask, victoryMaskIndex;
		for (int i = 0; i < FIELD_VICTORY_STATE_MASKS[offset].length; i++) {
			victoryMaskIndex = FIELD_VICTORY_STATE_MASKS[offset][i];
			victoryMask = VICTORY_STATE_MASKS[victoryMaskIndex];
			if ((this.state & (victoryMask << player2LeftShift)) == (victoryMask << player2LeftShift)) {
				return true;
			}
		}
		return false;
	}

	public boolean playMove(int offset) {
		int mask = 0b11000000000000000000000000000000;
		mask >>>= offset * 2;
		if ((this.state & mask) != 0) {
			return false;
		}
		if (this.turnNumber % 2 == 0) { // Player 1's turn to play.
			this.state = this.state + (PLAYER_1_MASK >>> (offset * 2));
		} else {
			this.state = this.state + (PLAYER_2_MASK >>> (offset * 2));
		}
		this.turnNumber++;
		return true;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getTurnNumber() {
		return turnNumber;
	}

	public void setTurnNumber(int turnNumber) {
		this.turnNumber = turnNumber;
	}

	public void prettyPrint() {
		String stateString = this.toString();
		String table = "-----------------\n" + "|0aa|1bb|2cc|3dd|\n" + "|aaa|bbb|ccc|ddd|\n" + "|aaa|bbb|ccc|ddd|\n"
				+ "-----------------\n" + "|4ee|5ff|6gg|7hh|\n" + "|eee|fff|ggg|hhh|\n" + "|eee|fff|ggg|hhh|\n"
				+ "-----------------\n" + "|8ii|9jj|10k|11l|\n" + "|iii|jjj|kkk|lll|\n" + "|iii|jjj|kkk|lll|\n"
				+ "-----------------\n" + "|12m|13n|14o|15p|\n" + "|mmm|nnn|ooo|ppp|\n" + "|mmm|nnn|ooo|ppp|\n"
				+ "-----------------\n";
		for (int i = 0; i < stateString.length(); i++) {
			if (stateString.charAt(i) == '1') {
				table = table.replace((char) (97 + i), 'X');
			} else if (stateString.charAt(i) == '2') {
				table = table.replace((char) (97 + i), 'O');
			} else {
				table = table.replace((char) (97 + i), ' ');
			}
		}
		System.out.println(table);
	}

	@Override
	public int hashCode() {
		return this.state;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null)
			return false;
		if (this.getClass() != o.getClass())
			return false;
		State state = (State) o;
		return this.state == state.getState();
	}
}
