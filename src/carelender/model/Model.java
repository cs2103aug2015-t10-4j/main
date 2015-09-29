package carelender.model;

/**
 * Handles all database and file saving
 */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Model {
	
	private static File eventFile;
	public static String filename;
	
	public Model() {
        filename = "events.dat";
		eventFile = new File(filename);
	}
	
	public static void addEvent(String string) {
		FileWriter fw;
		try {
			fw = new FileWriter(eventFile, true);
			BufferedWriter bw = new BufferedWriter(fw);
	        bw.write(string);
	        bw.newLine();
	        bw.flush();
	        bw.close();
	        System.out.println("added to " + eventFile + ": " + string);
		} catch (IOException e) {
			System.out.println("Failed to add event");
			e.printStackTrace();
		}
	}
}
