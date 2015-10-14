package carelender.model;

/**
 * Handles all database and file saving
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import carelender.model.data.*;

public class Model {
	
	private static Model singleton = null;
	public static Model getInstance(){
		if (singleton == null) {
			singleton = new Model();
		}
		return singleton;
	}
	private String filename;
	private EventList events;
	private ArrayList<EventList> cache;
	private ArrayList<String> storage;
	
	private int currentUid;
	
	private Model() {
		File file = new File("events.dat");
		File currentUidFile = new File("currentUid");
		events = new EventList();
		events = getFromFile("events.dat");
		currentUid = getFromFile("currentUid.dat", 1);
	}
	
	public boolean addEvent(EventObject eventObj) {
		//TODO: Add a uniqueID to the EventObject
		currentUid += 1;
		eventObj.setUid(currentUid);
		events.add(eventObj);
		saveToFile("currentUid.dat", currentUid);
		return saveToFile("events.dat", events);
	}
	
	public EventList retrieveEvent() {
		return events;
	}
	
	public boolean updateEvent(EventObject eventObj) {
		for (int i = 0; i < events.size(); i++) {
			if(events.get(i).getName().equals(eventObj.getName())) {
				events.remove(i);
				events.add(eventObj);
				return saveToFile("events.dat", events);
			}
		}
		return false;
	}
	
	public void deleteEvent(QueryDelete queryDelete) {
		for (int i = 0; i < events.size(); i++) {
			if(events.get(i).getName().equals(queryDelete.getName())) {
				events.remove(i);
			}
			saveToFile("events.dat", events);
		}
	}
	
	private boolean saveToFile(String filename, EventList eventList) {
		try {
			FileOutputStream fos = new FileOutputStream(filename);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			
			oos.writeObject(eventList);
			oos.close();
			fos.close();

	        return true;
		} catch (IOException ioe) {
			System.out.println("Failed to add event");
			ioe.printStackTrace();
			return false;
		}
	}
	
	private boolean saveToFile(String filename, int num) {
		try {
			FileOutputStream fos = new FileOutputStream(filename);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			
			oos.writeObject(num);
			oos.close();
			fos.close();

	        return true;
		} catch (IOException ioe) {
			System.out.println("Failed to add event");
			ioe.printStackTrace();
			return false;
		}
	}
	
	private EventList getFromFile(String filename){
		try	{
            FileInputStream fis = new FileInputStream(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);
            
            EventList eventList = (EventList) ois.readObject();
            ois.close();
            fis.close();
            return eventList;
		} catch(IOException ioe){
			ioe.printStackTrace();
		} catch(ClassNotFoundException c){
            System.out.println("Class not found");
            c.printStackTrace();
		} catch ( Exception e ) {
        }
	    return new EventList();
    }

	private int getFromFile(String filename, int num){
		try	{
            FileInputStream fis = new FileInputStream(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);
            
            int number = (int) ois.readObject();
            ois.close();
            fis.close();
            return number;
		} catch(IOException ioe){
			ioe.printStackTrace();
		} catch(ClassNotFoundException c){
            System.out.println("Class not found");
            c.printStackTrace();
		} catch ( Exception e ) {
        }
	    return 0;
    }

}
