package carelender.test;
import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import carelender.model.AppSettings;
import carelender.model.Model;
import carelender.model.ReminderManager;
import carelender.model.data.DateRange;
import carelender.model.data.Event;
import carelender.model.AppSettings.SettingName;

public class ModelTest {
	//Initialise a dummy event
	Date date;
	DateRange[] dates;
	Event dummyEvent;
	Calendar currentTime;
	
	public ModelTest() {
		date = new Date();
		DateRange[] dates = new DateRange[1];
		dates[0] = new DateRange(date, true);
		dummyEvent = new Event(1, "Test Reminder", dates, null);
		
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
		Model.getInstance().setSaveFileName("test.dat");
		//assertTrue(Model.getInstance().addEvent(dummyEvent));
		assertNotNull(Model.getInstance().retrieveEvent());
	}
	@Test
	public void testReminderManager() {
		
		Calendar reTime = Calendar.getInstance();
		reTime.set(Calendar.HOUR, currentTime.get(Calendar.HOUR) + 1);
		reTime.set(Calendar.HOUR_OF_DAY, currentTime.get(Calendar.HOUR_OF_DAY) + 1);
		
		ReminderManager.getInstance().addReminder(dummyEvent, reTime);
		//Checks if Time returned is the same
		assertEquals(ReminderManager.getInstance().getReminders().get(0).getReTime(), reTime);
		System.out.println(currentTime);
		System.out.println(reTime);
	}
}