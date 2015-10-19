package carelender.model;

import java.io.BufferedReader;
/**
 * Handles all database and file saving
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.*;

import com.google.gson.Gson;

import carelender.model.data.*;

public class Model {

	private static Model singleton = null;

	public static Model getInstance() {
		if (singleton == null) {
			singleton = new Model();
		}
		return singleton;
	}

	private String filename;
	private EventList events;
	private ArrayList<EventList> cache;
	private ArrayList<String> storage;
	private static Logger log;

	private int currentUid;

	private Model() {
		log = Logger.getLogger(Model.class.getName());
		File file = new File("events.dat");
		File currentUidFile = new File("currentUid");
		events = new EventList();
		events = getFromFile("events.dat");
		currentUid = getFromFile("currentUid.dat", 1);
	}

	public boolean addEvent(Event eventObj) {
		eventObj.setUid(currentUid += 1);// Set incremented UID to Event
		events.add(eventObj);
		saveToFile("currentUid.dat", currentUid);
		System.out.println("Added UID:" + currentUid + "Event Name: " + eventObj.getName());
		return saveToFile("events.dat", events);
	}

	public EventList retrieveEvent() {
		return events;
	}

	public boolean updateEvent(Event eventObj) {
		for (int i = 0; i < events.size(); i++) {
			System.out.println("  " + eventObj.getName());
			if (events.get(i).getUid() == eventObj.getUid()) {
				events.remove(i);
				events.add(eventObj);
				return saveToFile("events.dat", events);
			}
		}
		return false;
	}

	public void deleteEvent(EventList eventObj) {

	}

	public void deleteEvent(Event eventObj) {
		for (int i = 0; i < events.size(); i++) {
			if (events.get(i).getUid() == eventObj.getUid()) {
				events.remove(i);
			}
			saveToFile("events.dat", events);
		}
	}

	private boolean saveToFile(String filename, EventList eventList) {
		try {
			PrintWriter printWriter = new PrintWriter(filename);
			Gson gson = new Gson();
			String json = gson.toJson(eventList);
			
			printWriter.println(json);
			printWriter.flush();
			printWriter.close();
			
			return true;
		} catch (IOException ioe) {
			log.log(Level.FINE, "Failed to save event to file");
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
			log.log(Level.FINE, "Failed to add event");
			return false;
		}
	}

	private EventList getFromFile(String filename) {
		try {
			FileReader fileReader = new FileReader(filename);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			Gson gson = new Gson();
			String json = bufferedReader.readLine();
			EventList eventList = new EventList();
			eventList = gson.fromJson(json, EventList.class);
			
			bufferedReader.close();
			fileReader.close();
			
			return eventList;
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (Exception e) {
		}
		return new EventList();
	}

	private int getFromFile(String filename, int num) {
		try {
			FileInputStream fis = new FileInputStream(filename);
			ObjectInputStream ois = new ObjectInputStream(fis);

			int number = (int) ois.readObject();
			ois.close();
			fis.close();
			return number;
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (ClassNotFoundException c) {
			System.out.println("Class not found");
			c.printStackTrace();
		} catch (Exception e) {
		}
		return 0;
	}

}
