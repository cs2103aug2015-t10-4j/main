package carelender.model;

import java.io.BufferedReader;
/**
 * Handles all database and file saving
 */
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.*;
import com.google.gson.Gson;
import carelender.model.AppSettings.SettingName;
import carelender.model.UndoStep.UndoType;
import carelender.model.data.*;

public class Model {

	private static Model singleton = null;
	private static final String FOLDER_NAME = "data/";	
	private static final String FILE_NAME = "events";
	private static final String FILE_TYPE = ".dat";

	public static Model getInstance() {
		if (singleton == null) {
			singleton = new Model();
		}
		return singleton;
	}
	
	private static Logger log;
	private EventList events;

	File fileName;
	File folderName;

	private int currentUid;

	private Model() {
		log = Logger.getLogger(Model.class.getName());
		//Get current time
		Calendar cal = Calendar.getInstance();

		//Initiate the Directory
		folderName = new File(FOLDER_NAME);
		folderName.mkdir();

		//Get saved file/create one if none exists
		fileName = new File(FOLDER_NAME + FILE_NAME + cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.MONTH) + FILE_TYPE);
		events = new EventList();	
		if (!fileName.exists()) {
			try {
				fileName.createNewFile();
			} catch (IOException e) {
				log.log(Level.FINE, "Failed to create file");
				e.printStackTrace();
			}
		} else {
			events = getFromFile(fileName);
		}
		//Get Unique ID number
		if (AppSettings.getInstance().getIntSetting(SettingName.CURRENT_INDEX) != null) {
			currentUid = AppSettings.getInstance().getIntSetting(SettingName.CURRENT_INDEX);
		} else {
			currentUid = 1;
		}
	}

	public void addEvent(Event eventObj) {
		eventObj.setDateCreated(new Date());
		eventObj.setUid(currentUid);
		events.add(eventObj);
		updateUndoManager(eventObj, UndoType.ADD);	
		AppSettings.getInstance().setIntSetting(SettingName.CURRENT_INDEX, currentUid +=1);
		saveToFile(fileName, events);
	}

	public EventList retrieveEvent() {
		return events;
	}

	public void updateEvent(Event eventObj) {
		for (int i = 0; i < events.size(); i++) {
			if (events.get(i).getUid() == eventObj.getUid()) {
				updateUndoManager(events.get(i), UndoType.UPDATE);
				events.remove(i);
				events.add(eventObj);
				saveToFile(fileName, events);
			}
		}
	}
	// Delete single Event
	public void deleteEvent(Event eventObj) {
		for (int i = 0; i < events.size(); i++) {
			if (events.get(i).getUid() == eventObj.getUid()) {
				updateUndoManager(events.get(i), UndoType.DELETE);
				events.remove(i);
			}
			saveToFile(fileName, events);
		}
	}
	// Delete multiple Events
	public void deleteEvent(EventList eventList) {
		EventList deletedEventList = new EventList();
		for (int i = 0; i < events.size(); i++) {
			for (Event eventObj : eventList) {
				if (events.get(i).getUid() == eventObj.getUid()) {
					deletedEventList.add(events.get(i));
					events.remove(i);
				}
			}
		}
		updateUndoManager(deletedEventList);
		saveToFile(fileName, events);
	}

	public void undoAddedEvent(EventList eventList) {
		for (int i = 0; i < events.size(); i++) {
			for (Event eventObj : eventList) {
				if (events.get(i).getUid() == eventObj.getUid()) {
					System.out.println("REMOVED ID" + events.get(i).getUid());
					events.remove(i);
				}
			}
		}
		saveToFile(fileName, events);
	}

	public void undoUpdatedEvent(EventList eventList) {
		EventList redoEventList = new EventList();
		
		for (int i = 0; i < eventList.size(); i++) {
			for (int j = 0; j < events.size(); j++) {
				if (events.get(j).getUid() == eventList.get(i).getUid()) {
					redoEventList.add(events.get(j));
					events.remove(j);
				}
				saveToFile(fileName, events);
			}
			events.add(eventList.get(i));
		}
		UndoManager.getInstance().redoUpdate(redoEventList);
		saveToFile(fileName, events);
	}

	public void undoDeletedEvent(EventList eventList) {
		for (int i = 0; i < eventList.size(); i++) {
			events.add(eventList.get(i));
		}
		saveToFile(fileName, events);
	}

	private void updateUndoManager(Event eventObj, UndoStep.UndoType type) {
		EventList eventList = new EventList();
		eventList.add(eventObj);
		switch (type) {
		case ADD:
			UndoManager.getInstance().add(eventList);
			break;
		case DELETE:
			UndoManager.getInstance().delete(eventList);
			break;
		case UPDATE:
			UndoManager.getInstance().update(eventList);
			break;
		default:
			break;
		}
	}

	private void updateUndoManager(EventList eventList) {
		UndoManager.getInstance().delete(eventList);
	}

	public void setSaveFileName(String input) {
		//filename = input;
	}
	
	private void saveToFile(File filename, EventList eventList) {
		try {
			PrintWriter printWriter = new PrintWriter(filename);
			Gson gson = new Gson();
			String json = gson.toJson(eventList);

			printWriter.println(json);
			printWriter.flush();
			printWriter.close();
		} catch (IOException ioe) {
			log.log(Level.FINE, "Failed saving to file");
		}
	}

	private EventList getFromFile(File filename) {
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
		} catch (Exception e) {
			log.log(Level.FINE, "Failed getting from file");
		}
		return new EventList();
	}
}
