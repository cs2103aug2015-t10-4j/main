package carelender.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.PriorityQueue;

import carelender.model.AppSettings.DataType;
import carelender.model.AppSettings.SettingName;
import carelender.model.data.Event;

public class ReminderManager {
	private static ReminderManager singleton = null;
	public static ReminderManager getInstance() {
		if (singleton == null) {
			singleton = new ReminderManager();
		}
		return singleton;
	}
	
	private PriorityQueue<Reminder> reminders;
	
	private ReminderManager(){
		//Calendar cal = Calendar.getInstance();
		reminders = new PriorityQueue<Reminder>();
	}
	
	public boolean addReminder(Event eventObj, Calendar reTime){
		Reminder reminder = new Reminder((int)eventObj.getUid(), eventObj.getEarliestDate(), reTime);
		reminders.add(reminder);
		return true;
	}
	
	public ReminderList getReminders() {
		Calendar cal = Calendar.getInstance();
		
		ReminderList reminderList = new ReminderList();
		while (reminders.peek().getReTime().compareTo(cal) == -1) {
			System.out.println("Pop Reminder!");
			reminderList.add(reminders.poll());
		}
		return reminderList;
	}
	
}
