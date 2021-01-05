package dresscode;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

public class DataHandler {

	private static DataHandler instance = null;
	private static LinkedList<State> victoriousStatesPlayer1 = null;
	private static LinkedList<State> victoriousStatesPlayer2 = null;
	private static String path = "data";
	
	private DataHandler() {
		loadStates(path);
	}
	
	public static DataHandler getInstance() {
		if (instance == null)
			instance = new DataHandler();
		return instance;
	}
	public static void generateStates() {
		StateGenerator sg = new StateGenerator();
		sg.generateAllWinningStates();
		victoriousStatesPlayer1 = sg.getVictoriousStatesPlayer1();
		victoriousStatesPlayer2 = sg.getVictoriousStatesPlayer2();
		writeBinaryDataToFile(path);
		
	}
	
	public static void loadStates(String path) {
		try {
			File file = new File(path+"1.dat");
			byte[] bytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
			victoriousStatesPlayer1 = Utility.convertByteArrayToStateList(bytes);
			
			File file2 = new File(path+"2.dat");
			bytes = Files.readAllBytes(Paths.get(file2.getAbsolutePath()));
			victoriousStatesPlayer2 = Utility.convertByteArrayToStateList(bytes);
		} catch (FileNotFoundException e) {
			System.out.println("File does not exist; Generating states from scratch...");
			generateStates();
			return;
		} catch (IOException e) {
			System.out.println("IO Exception; Generating states from scratch...");
			generateStates();
			return;

		}
		System.out.println("Loaded:" + (victoriousStatesPlayer1.size() + victoriousStatesPlayer1.size()) + " states.");
}
	
	public static void writeBinaryDataToFile(String path) {
		System.out.println("Writing data to cache file. This could take a few seconds.");
		DataOutputStream writerPlayer1 = null;
		DataOutputStream writerPlayer2 = null;
		try {
			writerPlayer1 = new DataOutputStream(new FileOutputStream(new File(path+"1.dat")));
			for (State s : victoriousStatesPlayer1) {
					byte[] toWrite = Utility.convertStringToByteArray(s.toString());
					for(int i = 0; i < 4; i++) {
						writerPlayer1.writeByte(toWrite[i]);
					}
				}
			writerPlayer2 = new DataOutputStream(new FileOutputStream(new File(path+"2.dat")));
			for (State s : victoriousStatesPlayer2) {
				byte[] toWrite = Utility.convertStringToByteArray(s.toString());
				for(int i = 0; i < 4; i++) {
					writerPlayer2.writeByte(toWrite[i]);
				}
			}
		}catch(IOException e){
			System.out.println("Input Output error.");
		}finally {
				try {
					if(writerPlayer1 != null)	writerPlayer1.close();
					if(writerPlayer2 != null)	writerPlayer2.close();
				} catch (IOException e) {
					System.out.println("Failed closing files.");
				}
			}
		}

	public static LinkedList<State> getVictoriousStatesPlayer1() {
		return victoriousStatesPlayer1;
	}

	public static LinkedList<State> getVictoriousStatesPlayer2() {
		return victoriousStatesPlayer2;
	}
	
	}
