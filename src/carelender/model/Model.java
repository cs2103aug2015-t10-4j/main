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
	
	private String filename;
	private EventList events;
	private ArrayList<EventList> cache;
	private ArrayList<String> storage;
	
	public Model() {
		File file = new File("events.dat");
		events = new EventList();
		events = getFromFile();
	}
	
	public boolean addEvent(EventObject eventObj) {
		events.add(eventObj);
		return saveToFile(events);
	}
	
	public EventList retrieveEvent() {
		return events;
	}
	
	public boolean updateEvent(EventObject eventObj) {
		for (int i = 0; i < events.size(); i++) {
			if(events.get(i).getName().equals(eventObj.getName())) {
				events.remove(i);
				events.add(eventObj);
				return saveToFile(events);
			}
		}
		return false;
	}
	
	public void deleteEvent(QueryDelete queryDelete) {
		for (int i = 0; i < events.size(); i++) {
			if(events.get(i).getName().equals(queryDelete.getName())) {
				events.remove(i);
			}
			saveToFile(events);
		}
	}
	
	private boolean saveToFile(EventList eventList) {
		try {
			FileOutputStream fos = new FileOutputStream("events.dat");
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
	
	private EventList getFromFile(){
		try	{
            FileInputStream fis = new FileInputStream("events.dat");
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

}
