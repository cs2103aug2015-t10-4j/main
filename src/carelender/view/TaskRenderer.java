package carelender.view;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Calendar;
import carelender.model.data.DateRange;
import carelender.model.data.Event;
import carelender.model.data.EventList;

/**
 * Written by : Weizheng Lee 19/10/2015
 * This class contains static methods to help to render the calendar view
 */
public class TaskRenderer {
	private HashMap<String, EventList> taskDisplay;
	
	public TaskRenderer () {
		this.taskDisplay = new HashMap<String, EventList>();
	}
	
	public void addEvents ( EventList toDisplay ) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("d EEE");
		for ( Event event : toDisplay ) {
			DateRange [] dateRange = event.getDateRange();
			for ( DateRange date : dateRange ) {
				Date currentDay = date.getStart();
				for (int dayIterator = 0; dayIterator < date.getDaysBetween(); dayIterator++) {
					String day = dateFormat.format(currentDay);
					if (this.taskDisplay.containsKey(day)) {
						this.taskDisplay.get(day).add(event);
					} else {
						EventList tasksOnDay = new EventList();
						tasksOnDay.add(event);
						this.taskDisplay.put (day, tasksOnDay);
					}
					currentDay = this.addDays(currentDay, dayIterator + 1);
				}
			}
		}
	}
	
	public void drawTasks () {
		for ( Map.Entry<String, EventList> entry : this.taskDisplay.entrySet()) {
		    String key = entry.getKey();
		    EventList value = entry.getValue();
		    
		    System.out.println ("Day " + key);
		    
		    for (Event event : value) {
		    	System.out.println ("             " + event.getName());
		    }
		}
	}
	
	public Date addDays ( Date date, int days )
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        
        return calendar.getTime();
    }
    
    public static void calendarSquare ( GraphicsContext gc,  double x, double y, double w, double h, double dropOffset, String color, String text, Font font ) {
        gc.setFill(Color.web("#999"));
        gc.fillRect(x + dropOffset, y + dropOffset, w, h);

        gc.setFill(Color.web(color));
        gc.fillRect(x, y, w, h);

        gc.setFill(Color.web("#000"));
        gc.setTextAlign(TextAlignment.RIGHT);
        gc.setFont(font);
        gc.setTextBaseline(VPos.BOTTOM);
        gc.fillText(text, x + w - dropOffset * 0.5 , y + h - dropOffset * 0.5 );
    }


}
