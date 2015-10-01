package carelender.model;

import java.io.BufferedReader;
/**
 * Handles all database and file saving
 */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import carelender.model.data.*;

public class Model {
	
	private static File eventFile;
	public static String filename;
	public static ArrayList<EventList> cache;
	public static ArrayList<String> storage;
	
	public Model() {
        filename = "events.dat";
		eventFile = new File(filename);
	}
	
	public static void addEvent(EventObject eventObj) {
		FileWriter fw;
		try {
			fw = new FileWriter(eventFile, true);
			BufferedWriter bw = new BufferedWriter(fw);
	        //bw.write(eventObj);
	        bw.newLine();
	        bw.flush();
	        bw.close();
	        ///System.out.println("added to " + eventFile + ": " + string);
		} catch (IOException e) {
			System.out.println("Failed to add event");
			e.printStackTrace();
		}
	}
	
	public static EventList retrieveEvent(QueryObject queryObj) {
		EventList eventList = new EventList();
		return eventList;
	}
	
	public static void updateEvent(EventObject eventObj) {
		//TODO: Checks cache and update the event.
	}
	
	public static void deleteEvent(QueryObject queryObj) {
		//TODO: Checks cache and update the event.
	}
}
