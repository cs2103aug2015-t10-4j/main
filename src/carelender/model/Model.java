package carelender.model;

import java.io.BufferedOutputStream;
/**
 * Handles all database and file saving
 */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import carelender.model.data.*;

public class Model {
	
	private static File eventFile;
	private static File customCommands;
	public static String filename;
	//WZ: I initialised events. Before it was null and trying to add stuff to a null thing.
	public static EventList events = new EventList();
	//WZ: END
	public static ArrayList<EventList> cache;
	public static ArrayList<String> storage;
	
	public Model() {
        filename = "events.dat";
		eventFile = new File(filename);
	}
	
	public static boolean addEvent(EventObject eventObj) {
		events.add(eventObj);
		try {
			OutputStream file = new FileOutputStream("events.dat");
			OutputStream buffer = new BufferedOutputStream(file);
			ObjectOutput output = new ObjectOutputStream(buffer);
			
			output.writeObject(events);

	        return true;
		} catch (IOException e) {
			System.out.println("Failed to add event");
			return false;
		}
	}
	
	public static EventList retrieveEvent(QueryBase queryObj) {
		return events;
	}
	
	public static void updateEvent(EventObject eventObj) {
		//TODO: Checks cache and update the event.
	}
	
	public static void deleteEvent(QueryBase queryObj) {
		//TODO: Checks cache and update the event.
	}
}
