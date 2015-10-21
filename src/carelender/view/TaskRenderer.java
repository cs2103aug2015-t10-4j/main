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
    private GraphicsContext gc;
    private double xPosition;
    private double yPosition;

    private double width;
    private double height;

    private double xPadding;
    private double yPadding;

    private double dateBarWidth;
    private double dateBarHeight;

    private double taskBarWidth;
    private double taskBarHeight;

    private int windowSize;
    private int windowStart;
	private HashMap<String, EventList> taskDisplay;
    private TaskBarRenderer taskBarRender;
	
	public TaskRenderer () {
        this.taskBarRender = new TaskBarRenderer();
        this.taskDisplay = new HashMap<String, EventList>();
	}

    public void setParams (GraphicsContext gc, double x, double y,
                           double w, double h, double xPad, double yPad,
                           double taskWidthRatio, double taskHeightRatio,
                           double dateWidthRatio, double dateHeightRatio) {
        this.gc = gc;

        this.xPosition = x;
        this.yPosition = y;

        this.width = w;
        this.height = h;

        this.xPadding = xPad;
        this.yPadding = yPad;

        this.dateBarWidth = this.width * dateWidthRatio;
        this.dateBarHeight = this.height * dateHeightRatio;

        this.taskBarWidth = this.width * taskWidthRatio;
        this.taskBarHeight = this.height * taskHeightRatio;

        this.taskBarRender.setParams (gc, this.taskBarWidth, this.taskBarHeight,
                                        this.xPadding, this.yPadding, 0.2, 0.4);
    }

    public void clearEvents () {
        this.taskDisplay.clear();
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
					currentDay = this.addDays(currentDay, 1);
				}
                if ( !date.getStart().equals(date.getEnd()) ) {
                    String day = dateFormat.format(date.getEnd());
                    System.out.println(day);
                    if (this.taskDisplay.containsKey(day)) {
                        this.taskDisplay.get(day).add(event);
                    } else {
                        EventList tasksOnDay = new EventList();
                        tasksOnDay.add(event);
                        this.taskDisplay.put(day, tasksOnDay);
                    }
                }
			}
		}
	}
	
	public void drawTasks () {
        double xCurrent = this.xPosition + this.xPadding;
        double yCurrent = this.yPosition + this.yPadding;

        Font font = Font.loadFont("file:res/monaco.ttf", this.dateBarHeight * 0.5);

		for ( Map.Entry<String, EventList> entry : this.taskDisplay.entrySet()) {
		    String key = entry.getKey();
		    EventList value = entry.getValue();

            this.gc.setFill (Color.web("999"));
            this.gc.fillRect(xCurrent, yCurrent, this.dateBarWidth, this.dateBarHeight);

            this.gc.setFill(Color.web("979"));
            this.gc.setTextAlign(TextAlignment.LEFT);
            this.gc.setFont(font);
            this.gc.setTextBaseline(VPos.TOP);

            this.gc.fillText ( key, xCurrent, yCurrent );

		    System.out.println ("Day " + key);
		    
		    for (Event event : value) {
                this.taskBarRender.setPosition(xCurrent + this.dateBarWidth + this.xPadding, yCurrent);
                this.taskBarRender.setContent(event);
                this.taskBarRender.drawTaskBar("999", "000");
		    	System.out.println ("             " + event.getName());
                yCurrent += ( this.taskBarHeight + this.yPadding );
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


}
