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
public class TaskRenderer extends CanvasRenderer {
    private HashMap<String, EventList> taskDisplay;
    private TaskBarRenderer taskBarRender;

    private double xPadding;
    private double yPadding;
    private double dateWidthRatio;
    private double dateHeightRatio;
    private double taskWidthRatio;
    private double taskHeightRatio;

    public TaskRenderer () {
        this.taskBarRender = new TaskBarRenderer();
        this.taskDisplay = new HashMap<String, EventList>();
	}

    public void setParams (double xPad, double yPad,
                           double taskWidthRatio, double taskHeightRatio,
                           double dateWidthRatio, double dateHeightRatio) {

        this.taskWidthRatio = taskWidthRatio;
        this.taskHeightRatio = taskHeightRatio;
        this.dateWidthRatio = dateWidthRatio;
        this.dateHeightRatio = dateHeightRatio;

        this.xPadding = xPad;
        this.yPadding = yPad;
    }

    @Override
    void draw(GraphicsContext gc, double x, double y, double width, double height) {
        super.draw(gc, x, y, width, height);

        double xCurrent = x + this.xPadding;
        double yCurrent = y + this.yPadding;

        double dateBarWidth = this.width * dateWidthRatio;
        double dateBarHeight = this.height * dateHeightRatio;


        double taskBarWidth = this.width * taskWidthRatio;
        double taskBarHeight = this.height * taskHeightRatio;

        this.taskBarRender.setParams(gc, taskBarWidth, taskBarHeight,
                this.xPadding, this.yPadding, 0.2, 0.4);

        Font font = Font.loadFont("file:res/monaco.ttf", dateBarHeight * 0.5);

        gc.strokeRect(x,y, width, height);

        for ( Map.Entry<String, EventList> entry : this.taskDisplay.entrySet()) {
            String key = entry.getKey();
            EventList value = entry.getValue();

            this.gc.setFill (Color.web("999"));
            this.gc.fillRect(xCurrent, yCurrent, dateBarWidth, dateBarHeight);

            this.gc.setFill(Color.web("979"));
            this.gc.setTextAlign(TextAlignment.LEFT);
            this.gc.setFont(font);
            this.gc.setTextBaseline(VPos.TOP);

            this.gc.fillText ( key, xCurrent, yCurrent );

            System.out.println ("Day " + key);

            for (Event event : value) {
                this.taskBarRender.setPosition(xCurrent + dateBarWidth + this.xPadding, yCurrent);
                this.taskBarRender.setContent(event);
                this.taskBarRender.drawTaskBar("999", "000");
                System.out.println ("             " + event.getName());
                yCurrent += ( taskBarHeight + this.yPadding );
            }
        }

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
				while (!currentDay.after(date.getEnd())) {
					String day = dateFormat.format(currentDay);
					if (this.taskDisplay.containsKey(day)) {
						if (!this.taskDisplay.get(day).contains(event)) {
							this.taskDisplay.get(day).add(event);
						}
					} else {
						EventList tasksOnDay = new EventList();
						tasksOnDay.add(event);
						this.taskDisplay.put (day, tasksOnDay);
					}
					currentDay = this.addDays(currentDay, 1);
				}
                if (!dateFormat.format(date.getStart()).equals(dateFormat.format(date.getEnd()))) {
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
	
	public Date addDays ( Date date, int days )
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        
        return calendar.getTime();
    }


}
