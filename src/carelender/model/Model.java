//@@author A0121815N
package carelender.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.*;
import com.google.gson.Gson;
import carelender.model.AppSettings.SettingName;
import carelender.model.UndoStep.UndoType;
import carelender.model.data.*;

/**
 * Handles all database and file saving
 */
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
    private File fileName;
    private File folderName;
    private int currentUid;

    private Model() {
        log = Logger.getLogger(Model.class.getName());

        //Initiate the Directory
        folderName = new File(FOLDER_NAME);
        folderName.mkdir();

        //Get saved file/create one if none exists
        fileName = new File(FOLDER_NAME + FILE_NAME + FILE_TYPE);

        events = new EventList();
        if (!fileName.exists()) {
            try {
                fileName.createNewFile();
            } catch (IOException e) {
                log.log(Level.FINE, "Failed to create file");
                e.printStackTrace();
            }
        } else {
            //Get events from file
            events = getFromFile(fileName);
            if (events == null) {
                events = new EventList();
            }
            //Get Unique ID number
            if (AppSettings.getInstance().getIntSetting(SettingName.CURRENT_INDEX) != null) {
                currentUid = AppSettings.getInstance().getIntSetting(SettingName.CURRENT_INDEX);
            } else {
                currentUid = 1;
            }
        }
    }

    /**
     * Adds and event into model
     * @param eventObj Event to be added
     */
    public void addEvent(Event eventObj) {
        eventObj.setDateCreated(new Date());
        eventObj.setUid(currentUid);
        events.add(eventObj);
        updateUndoManager(eventObj, UndoType.ADD);
        AppSettings.getInstance().setIntSetting(SettingName.CURRENT_INDEX, currentUid +=1);
        saveToFile(fileName, events);
    }

    /**
     * Passes the caller an event list
     * @return EventList List of events
     */
    public EventList retrieveEvent() {
        return events;
    }

    /**
     * Finds the event object in EventList, and updates it with a new one
     * @param eventObj Event to be updated
     */
    public void updateEvent(EventList eventList) {
        EventList redoEventList = new EventList();
        for(int i = 0; i < eventList.size(); i++) {
            for (int j = 0; j < events.size(); j++) {
                if (events.get(j).getUid() == eventList.get(i).getUid()) {
                    redoEventList.add(events.get(j).copy());
                    events.remove(j);
                    events.add(eventList.get(i));
                    saveToFile(fileName, events);
                    break;
                }
            }
        }
        updateUndoManager(redoEventList, UndoType.UPDATE);
    }

    /**
     * Deletes a single Event
     * @param eventObj Event object to be deleted
     */
    public void deleteEvent(Event eventObj) {
        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).getUid() == eventObj.getUid()) {
                updateUndoManager(events.get(i), UndoType.DELETE);
                events.remove(i);
            }
            saveToFile(fileName, events);
        }
    }

    /**
     * Deletes multiple events with an EventList
     * @param eventList Events to be deleted
     */
    public void deleteEvent(EventList eventList) {
        EventList deletedEventList = new EventList();
        for (int i = 0; i < eventList.size(); i++) {
            for (int j = 0; j < events.size(); j++) {
                if (eventList.get(i).getUid() == events.get(j).getUid()) {
                    deletedEventList.add(events.get(j));
                    events.remove(j);
                    break;
                }
            }
        }
        updateUndoManager(deletedEventList, UndoStep.UndoType.DELETE);
        saveToFile(fileName, events);
    }

    /**
     * Undo an added event (Delete)
     * @param eventList Events to be undone
     */
    public void undoAddedEvent(EventList eventList) {
        EventList redoEventList = new EventList();
        for (int i = 0; i < eventList.size(); i++) {
            for (int j = 0; j < events.size(); j++) {
                if (events.get(j).getUid() == eventList.get(i).getUid()) {
                    redoEventList.add(events.get(j));
                    events.remove(j);
                    break;
                }
            }
        }
        saveToFile(fileName, events);
    }

    /**
     * Undo an updated event (Revert to old)
     * @param eventList Event to be undone/redone
     * @param isUndo Checks if is undo or redo command
     */
    public void undoUpdatedEvent(EventList eventList, boolean isUndo) {
        EventList redoEventList = new EventList();
        for (int i = 0; i < eventList.size(); i++) {
            for (int j = 0; j < events.size(); j++) {
                if (events.get(j).getUid() == eventList.get(i).getUid()) {
                    redoEventList.add(events.get(j));
                    events.remove(j);
                    events.add(eventList.get(i));
                    break;
                }
            }
        }
        // Checks if it is an undo or redo command
        if (isUndo) {
            UndoManager.getInstance().redoUpdate(redoEventList);
        } else {
            UndoManager.getInstance().update(redoEventList);
        }
        saveToFile(fileName, events);
    }

    /**
     * Undo a deleted event (Add back)
     * @param eventList Events to be added back upon undo
     */
    public void undoDeletedEvent(EventList eventList) {
        EventList redoEventList = new EventList();
        for (int i = 0; i < eventList.size(); i++) {
            redoEventList.add(eventList.get(i));
            events.add(eventList.get(i));
        }
        UndoManager.getInstance().add(redoEventList);
        saveToFile(fileName, events);
    }

    /**
     * Update the undo manager for single EventObject
     * @param eventObj Event to be undone
     * @param type type of undo commmad
     */
    private void updateUndoManager(Event eventObj, UndoStep.UndoType type) {
        EventList eventList = new EventList();
        UndoManager.getInstance().clearRedoStack();
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

    /**
     * Update undo manager with EventList
     * @param eventList
     */
    private void updateUndoManager(EventList eventList, UndoStep.UndoType type) {
        UndoManager.getInstance().clearRedoStack();
        switch (type) {
        case DELETE:
            UndoManager.getInstance().delete(eventList);
            break;
        case UPDATE:
            UndoManager.getInstance().update(eventList);
        }
    }

    /**
     * Method call to save EventList to disk
     * @param filename
     * @param eventList
     */
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

    /**
     * Method call to get an event list from disk
     * @param filename
     * @return
     */
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

    /**
     * Loads a string array from file
     * @param filename
     * @return
     */
    public String[] loadStringArray( String filename ) {
        ArrayList<String> strings = new ArrayList<>();
        return loadStringArrayList(filename).toArray(new String[strings.size()]);
    }

    /**
     * Loads an arrayList of
     * @param filename
     * @return
     */
    public ArrayList<String> loadStringArrayList( String filename ) {
        ArrayList<String> strings = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            while ( true ) {
                line = bufferedReader.readLine();
                if ( line == null ) {
                    break;
                }
                strings.add(line);
            }
            bufferedReader.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return strings;
    }

    public void setSaveFileName(String input) {
        fileName = new File(input);
        events = new EventList();
    }
}
