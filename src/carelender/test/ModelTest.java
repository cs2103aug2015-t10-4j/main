//@@author A0121815N
package carelender.test;
import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import carelender.model.AppSettings;
import carelender.model.Model;
import carelender.model.ReminderManager;
import carelender.model.UndoManager;
import carelender.model.data.DateRange;
import carelender.model.data.Event;
import carelender.model.data.EventList;
import carelender.model.AppSettings.SettingName;

public class ModelTest {
	//Initialise a dummy event
	Date date;
	DateRange[] dates;
	Event dummyEvent1;
	Event dummyEvent2;
	Event dummyEvent3;
	Event dummyEvent4;
	Calendar currentTime;
	
	public ModelTest() throws ParseException {
		//Init date
	    SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	    String dateInString = "31-8-2015 10:20:56";
		date = new Date();
		date = sdf.parse(dateInString);
		DateRange[] dates = new DateRange[1];
		dates[0] = new DateRange(date, true);
		currentTime = Calendar.getInstance();

		//Init test events
		dummyEvent1 = new Event(1, "Event 1", dates, null);
		dummyEvent2 = new Event(2, "Event 2", dates, null);
		dummyEvent3 = new Event(3, "Event 3", dates, null);
		dummyEvent4 = new Event(4, "Event 4", dates, null);
	}
	
	@Test
	public void testAppSettings() {
		//Initialise AppSettings with data
		AppSettings.getInstance().setIntSetting(SettingName.CURRENT_INDEX, 1);
		AppSettings.getInstance().setBooleanSetting(SettingName.ISFREE, true);
		AppSettings.getInstance().setStringSetting(SettingName.USERNAME, "Shalom");
		
		//Checks if Boolean Setting is set properly
		assertEquals(true, AppSettings.getInstance().getBooleanSetting(SettingName.ISFREE));
		//Checks if  Setting is set properly
		assertEquals("Shalom", AppSettings.getInstance().getStringSetting(SettingName.USERNAME));
		
		//Update boolean setting
		AppSettings.getInstance().setBooleanSetting(SettingName.ISFREE, false);
		assertEquals(false, AppSettings.getInstance().getBooleanSetting(SettingName.ISFREE));
		//Update string setting
		AppSettings.getInstance().setStringSetting(SettingName.USERNAME, "Jia Xun");
		assertEquals("Jia Xun", AppSettings.getInstance().getStringSetting(SettingName.USERNAME));
	}
	
	@Test
	public void testModel() {
		//Initiate test for model File Creation
		Model.getInstance().setSaveFileName("data/test.dat");
		
		//Create Event
		Model.getInstance().addEvent(dummyEvent1);
		Model.getInstance().addEvent(dummyEvent2);
		Model.getInstance().addEvent(dummyEvent3);
		Model.getInstance().addEvent(dummyEvent4);
		
		//Retrieve Event
		EventList eventList = new EventList();
		eventList = Model.getInstance().retrieveEvent();
		assertEquals(eventList.size(), 4);
		System.out.println(eventList.toString());
		assertNotNull(Model.getInstance().retrieveEvent());
		assertEquals("2 | Event 2 | Mon 31 Aug 10:20AM", eventList.get(1).toString());
		
		//Update Event
		Event updateEvent = eventList.get(1);
		updateEvent.setName("Update Name Test");
		Model.getInstance().updateEvent(updateEvent);
		eventList = Model.getInstance().retrieveEvent();
		System.out.println(eventList.toString());
		assertEquals("2 | Update Name Test | Mon 31 Aug 10:20AM", eventList.get(3).toString());
		System.out.println();

		//Delete Single
		Model.getInstance().deleteEvent(dummyEvent1);
		//Delete Multiple
		EventList deleteList = new EventList();
		deleteList.add(dummyEvent3);
		deleteList.add(dummyEvent4);
		Model.getInstance().deleteEvent(deleteList);
		eventList = Model.getInstance().retrieveEvent();
		assertEquals("2 | Update Name Test | Mon 31 Aug 10:20AM", eventList.get(0).toString());
		System.out.println(eventList.toString());
		System.out.println();
		
		//Undo command
		UndoManager.getInstance().undo();
		eventList = Model.getInstance().retrieveEvent();
		assertEquals(3, eventList.size());
		eventList = Model.getInstance().retrieveEvent();
		System.out.println(eventList.toString());
		System.out.println();
		//Redo command
		UndoManager.getInstance().redo();
		eventList = Model.getInstance().retrieveEvent();
		System.out.println(eventList.toString());
		//assertEquals(1, eventList.size());
	}
	
	
	@Test
	public void testReminderManager() {
		Calendar reTime = Calendar.getInstance();
		reTime.set(Calendar.HOUR, currentTime.get(Calendar.HOUR) + 1);
		reTime.set(Calendar.HOUR_OF_DAY, currentTime.get(Calendar.HOUR_OF_DAY) + 1);
		
		ReminderManager.getInstance().addReminder(dummyEvent1, reTime);
		//Checks if Time returned is the same
		//assertEquals(ReminderManager.getInstance().getReminders().get(0).getReTime(), reTime);
		System.out.println(currentTime);
		System.out.println(reTime);
	}
}