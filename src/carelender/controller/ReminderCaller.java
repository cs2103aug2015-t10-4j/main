package carelender.controller;

import java.util.TimerTask;
import carelender.controller.Controller;
import carelender.model.ReminderList;
import carelender.model.ReminderManager;
import javafx.application.Platform;

public class ReminderCaller extends TimerTask {

	@Override
	public void run() {
		//Controller.getGUI().clearMessageLog();
		Controller.displayAnnouncement(HintGenerator.getInstance().getHint());
		ReminderList reminders = new ReminderList();
		reminders = ReminderManager.getInstance().getReminders();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Controller.getGUI().refresh();
			}
		});

	}

}
