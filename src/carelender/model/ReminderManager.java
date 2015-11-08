package carelender.model;

import java.util.Calendar;
import java.util.PriorityQueue;

import carelender.model.data.Event;

/**
 * Handles reminders for events
 */
public class ReminderManager {
	private static ReminderManager singleton = null;

	public static ReminderManager getInstance() {
		if (singleton == null) {
			singleton = new ReminderManager();
		}
		return singleton;
	}

	private PriorityQueue<Reminder> reminders;

	private ReminderManager() {
		reminders = new PriorityQueue<>();
	}

	public boolean addReminder(Event eventObj, Calendar reTime) {
		Reminder reminder = new Reminder((int) eventObj.getUid(), eventObj.getEarliestDate(), reTime);
		reminders.add(reminder);
		return true;
	}
	
	/**
	 * Get reminders which have been triggered
	 * @return List of reminders which have been triggered
	 */
	public ReminderList getReminders() {
		Calendar cal = Calendar.getInstance();

		ReminderList reminderList = new ReminderList();
		while (!reminders.isEmpty()) {
			if (reminders.peek().getReTime().compareTo(cal) == -1) {
				reminderList.add(reminders.poll());
			} else {
				break;
			}
		}
		return reminderList;
	}
	
	/**
	 * Readds reminder into the reminders PriorityQueue
	 * @param reminder
	 */
	public void snoozeReminder(Reminder reminder){
		reminder.snooze();
		reminders.add(reminder);
	}

}
