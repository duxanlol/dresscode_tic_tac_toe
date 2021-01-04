package dresscode;

import java.nio.ByteBuffer;
import java.util.LinkedList;

public class Utility {

	public static byte[] convertStringToByteArray(String string) {
		byte[] bytes = new byte[4];
		for (int byteCount = 3; byteCount >= 0; byteCount--) {
			byte tempByte = 0;
			for (int i = 0; i <= 3; i++) {
				switch (string.charAt((byteCount * 4) + i)) {
				case '0':
					tempByte <<= 2;
					break;
				case '1':
					tempByte <<= 2;
					tempByte += 1;
					break;
				case '2':
					tempByte <<= 2;
					tempByte += 2;
					break;
				}
			}

			bytes[byteCount] = tempByte;
		}
		return bytes;
	}

	public static int convertStateStringToMask(String stateString) {
		byte[] bits = Utility.convertStringToByteArray(stateString);
		int mask = Utility.convertByteArrayToInt(bits);
		return mask;
	}

	public static LinkedList<State> convertByteArrayToStateList(byte[] bytes) {
		if (bytes == null || bytes.length % 4 != 0)
			return null;
		LinkedList<State> states = new LinkedList<State>();
		int state;
		for (int i = 0; i < bytes.length / 4; i++) {
			state = (convertByteArrayToInt(
					new byte[] { bytes[(i * 4)], bytes[(i * 4) + 1], bytes[(i * 4) + 2], bytes[(i * 4) + 3], }));
			states.add(new State(state));
		}
		return states;
	}

	public static int convertByteArrayToInt(byte[] intBytes) {
		ByteBuffer byteBuffer = ByteBuffer.wrap(intBytes);
		return byteBuffer.getInt();
	}

}