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
	
	private File eventFile;
	private File customCommands;
	private String filename;
	private EventList events;
	private ArrayList<EventList> cache;
	private ArrayList<String> storage;
	
	public Model() {
        filename = "events.dat";
		eventFile = new File(filename);
		events = new EventList();
	}
	
	public boolean addEvent(QueryAdd queryAdd) {
		DateRange dateRange = new DateRange(queryAdd.getTime());
		DateRange[] dateRangeArray = new DateRange[1];
		dateRangeArray[0] = dateRange;
		EventObject eventObj = new EventObject(0, queryAdd.getName(), dateRangeArray);
		
		events.add(eventObj);
		System.out.println(eventObj.getName());
		return true;
//		try {
//			OutputStream file = new FileOutputStream("events.dat");
//			OutputStream buffer = new BufferedOutputStream(file);
//			ObjectOutput output = new ObjectOutputStream(buffer);
//			
//			output.writeObject(events);
//
//	        return true;
//		} catch (IOException e) {
//			System.out.println("Failed to add event");
//			return false;
//		}
	}
	
	public EventList retrieveEvent() {
		return events;
	}
	
	public void updateEvent() {
//		for (int i = 0; i < events.size(); i++) {
//			if(events.get(i).getName().equals(queryDelete.getName())) {
//				events.remove(i);
//			}
//		}
	}
	
	public void deleteEvent(QueryDelete queryDelete) {
		for (int i = 0; i < events.size(); i++) {
			if(events.get(i).getName().equals(queryDelete.getName())) {
				events.remove(i);
			}
		}
	}
}
