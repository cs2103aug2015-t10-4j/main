package carelender.controller;

import java.util.TimerTask;
import carelender.controller.Controller;
import javafx.application.Platform;

public class Reminder extends TimerTask {

	@Override
	public void run() {
		//Controller.getGUI().clearMessageLog();
		Controller.displayAnnouncement(HintGenerator.getInstance().getHint());

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Controller.getGUI().refresh();
			}
		});

	}

}
