package carelender.view.gui.components;

import carelender.model.strings.AppColours;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Calendar;

import carelender.model.data.Event;
import carelender.model.data.EventList;
import carelender.controller.Controller;

/**
 * Written by : Weizheng Lee 19/10/2015
 * This class contains static methods to help to render the calendar view
 */
public class TaskRenderer extends CanvasRenderer {
    private Map<String, EventList> taskDisplay;
    private TaskBarRenderer taskBarRender;
    private int totalTasks;
    private int displayStart;

    private double xPadding;
    private double yPadding;
    private double dateWidthRatio;
    private double dateHeightRatio;
    private double taskWidthRatio;
    private double taskHeightRatio;

    public TaskRenderer () {
        this.taskBarRender = new TaskBarRenderer();
        this.taskDisplay = new TreeMap<String, EventList>();
        
        this.totalTasks = 0;
        this.displayStart = 0;
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
    public void draw(GraphicsContext gc, double x, double y, double width, double height) {
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

        gc.setFill(AppColours.panelBackground);
        gc.fillRect(x, y, width, height);

        int currentTaskToDisplay = 0;
        double remainingHeight = this.height - (this.yPadding * 2);
        double xPositionDate = 0;
        double yPositionDate = 0;
        int index = 1;
        
        for ( Map.Entry<String, EventList> entry : this.taskDisplay.entrySet()) {
            String key = entry.getKey();
            EventList value = entry.getValue();

            this.gc.setFill (Color.web("757575"));
            this.gc.fillRect(xCurrent, yCurrent, this.width - (this.xPadding * 2), this.yPadding * 0.5);
            
            yCurrent += (this.yPadding * 1.5);
            
            xPositionDate = xCurrent;
            yPositionDate = yCurrent;
            
            for (Event event : value) {
            	if ( currentTaskToDisplay >= this.displayStart ) {
	                this.taskBarRender.setPosition(xCurrent + dateBarWidth + this.xPadding, yCurrent);
	                this.taskBarRender.setContent(event);
	                
	                if ( (remainingHeight - this.taskBarRender.getHeight()) >= 0 ) {
	                	this.gc.setFill(AppColours.tasklistRowBackground);
	                    this.gc.setTextAlign(TextAlignment.RIGHT);
	                    this.gc.setFont(font);
	                    this.gc.setTextBaseline(VPos.TOP);
	                    
	                    this.gc.fillText ( String.valueOf(index), xCurrent + dateBarWidth, yCurrent );
	                    
		                this.taskBarRender.drawTaskBar(AppColours.tasklistRowBackground, Color.web("eeeff0"));
		                yCurrent += ( this.taskBarRender.getHeight() + this.yPadding );
		                
		                remainingHeight -= (this.taskBarRender.getHeight() + this.yPadding);
		                index++;
	                } else {
	                	return;
	                }
            	}
            	currentTaskToDisplay++;
            }
            
            /*this.gc.setFill (Color.web("999"));
            this.gc.fillRect(xPositionDate, yPositionDate, dateBarWidth, dateBarHeight);*/

            this.gc.setFill(AppColours.primaryColour);
            this.gc.setTextAlign(TextAlignment.LEFT);
            this.gc.setFont(font);
            this.gc.setTextBaseline(VPos.TOP);

            String [] keyWords = key.split(" ");
            this.gc.fillText ( keyWords[1], xPositionDate, yPositionDate );
            this.gc.fillText ( keyWords[2], xPositionDate, yPositionDate + font.getSize() );
        }
    }
    
    public void scrollDown () {
    	if ( this.displayStart > 0 ) {
    		this.displayStart--;
    	}
    }
    
    public void scrollUp () {
    	if ( this.displayStart < this.totalTasks ) {
    		this.displayStart++;
    	}
    }

    public void clearEvents () {
        this.taskDisplay.clear();
        this.totalTasks = 0;
    }

    public void addEvents ( EventList toDisplay ) {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("D d EEE");
		for ( Event event : toDisplay ) {
			Date currentDay = event.getEarliestDateFromNow();
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
			this.totalTasks++;
		}
		this.setDisplayList();
    }
    
    private void setDisplayList () {
    	List<Event> concatList = new EventList();
    	for ( EventList events : this.taskDisplay.values()) {
            concatList.addAll(events);
    	}
    	Controller.setDisplayedList((EventList)concatList);
    }
    
    public EventList getDisplayList () {
    	List<Event> concatList = new EventList();
    	for ( EventList events : this.taskDisplay.values()) {
            concatList.addAll(events);
    	}
    	return (EventList)concatList;
    }
    /*
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
	*/
	
	public Date addDays ( Date date, int days )
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        
        return calendar.getTime();
    }


}
