//@@author A0133907E
package carelender.controller;

import java.util.TimerTask;

import carelender.model.ReminderList;
import carelender.model.ReminderManager;
import javafx.application.Platform;

public class ReminderCaller extends TimerTask {

	@Override
	public void run() {
		//Controller.getUI().clearMessageLog();
		Controller.displayAnnouncement(HintGenerator.getInstance().getHints());
		ReminderList reminders = new ReminderList();
		reminders = ReminderManager.getInstance().getReminders();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Controller.getUI().refresh();
			}
		});

	}

}
