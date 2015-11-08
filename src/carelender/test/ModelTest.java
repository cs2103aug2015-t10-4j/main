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
	    SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	    String dateInString = "31-8-2015 10:20:56";
		date = new Date();
		date = sdf.parse(dateInString);
		DateRange[] dates = new DateRange[1];
		dates[0] = new DateRange(date, true);
		dummyEvent1 = new Event(1, "Test Reminder 1", dates, null);
		dummyEvent2 = new Event(2, "Test Reminder 2", dates, null);
		dummyEvent3 = new Event(3, "Test Reminder 3", dates, null);
		dummyEvent4 = new Event(4, "Test Reminder 4", dates, null);
		
		currentTime = Calendar.getInstance();
	}
	@Test
	public void testAppSettings() {
		AppSettings.getInstance().setIntSetting(SettingName.CURRENT_INDEX, 56);
		AppSettings.getInstance().setBooleanSetting(SettingName.ISFREE, true);
		AppSettings.getInstance().setStringSetting(SettingName.USERNAME, "Shalom");
		
		//assertEquals(56, AppSettings.getInstance().getIntSetting(SettingName.CURRENT_INDEX));
		//Checks if Boolean Setting is set properly
		assertEquals(true, AppSettings.getInstance().getBooleanSetting(SettingName.ISFREE));
		//Checks if  Setting is set properly
		assertEquals("Shalom", AppSettings.getInstance().getStringSetting(SettingName.USERNAME));
		//fail("Not yet implemented");
	}
	@Test
	public void testModel() {
		Model.getInstance().setSaveFileName("data/test.dat");
		Model.getInstance().addEvent(dummyEvent1);
		Model.getInstance().addEvent(dummyEvent2);
		Model.getInstance().addEvent(dummyEvent3);
		Model.getInstance().addEvent(dummyEvent4);
		EventList eventList = new EventList();
		eventList = Model.getInstance().retrieveEvent();
		System.out.println(eventList.toString());
		assertNotNull(Model.getInstance().retrieveEvent());
		assertEquals(eventList.get(2).toString(), "2. Test Reminder 2 | Mon 31 Aug 10:20AM");
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