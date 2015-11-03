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

        this.taskBarRender.setParams(this.xPadding, this.yPadding, 0.2, 0.4, 1);

        Font font = Font.loadFont("file:res/monaco.ttf", dateBarHeight * 0.5);

        gc.setFill(AppColours.panelBackground);
        gc.fillRect(x, y, width, height);

        int currentTaskToDisplay = 0;
        double remainingHeight = this.height - (this.yPadding * 2);
        double xPositionDate = 0;
        double yPositionDate = 0;
        boolean displayDate = false;
        int index = 1;
        
        for ( Map.Entry<String, EventList> entry : this.taskDisplay.entrySet()) {
            String key = entry.getKey();
            EventList value = entry.getValue();

            if ( currentTaskToDisplay >= this.displayStart ) {
	            gc.setFill (Color.web("757575"));
	            gc.fillRect(xCurrent, yCurrent, this.width - (this.xPadding * 2), this.yPadding * 0.5);
            
	            yCurrent += (this.yPadding * 1.5);
            }
            
            xPositionDate = xCurrent;
            yPositionDate = yCurrent;

            displayDate = false;
            for (Event event : value) {
                if ( currentTaskToDisplay >= this.displayStart ) {
                    this.taskBarRender.setContent(event);

                   if ( (remainingHeight - this.taskBarRender.getHeight(taskBarHeight)) >= 0 ) {
                    	displayDate = true;
                    	
                        gc.setFill(AppColours.primaryColour);
                        gc.setTextAlign(TextAlignment.RIGHT);
                        gc.setFont(font);
                        gc.setTextBaseline(VPos.TOP);

                        gc.fillText(String.valueOf(index), xCurrent + dateBarWidth, yCurrent);

                        this.taskBarRender.draw(gc, xCurrent + dateBarWidth + this.xPadding, yCurrent,
                        						taskBarWidth, taskBarHeight, AppColours.tasklistRowBackground,
                        						Color.web("eeeff0"));
                        yCurrent += (this.taskBarRender.getHeight(taskBarHeight) + this.yPadding);

                        remainingHeight -= (this.taskBarRender.getHeight(taskBarHeight) + (this.yPadding * 2));
                    } else {
                        return;
                    }
                }
                index++;
                currentTaskToDisplay++;
            }
            
            /*this.gc.setFill (Color.web("999"));
            this.gc.fillRect(xPositionDate, yPositionDate, dateBarWidth, dateBarHeight);*/

            if ( displayDate ) {
	            gc.setFill(AppColours.tasklistRowBackground);
	            gc.setTextAlign(TextAlignment.LEFT);
	           	gc.setFont(font);
            	gc.setTextBaseline(VPos.TOP);
	
	            String [] keyWords = key.split(" ");
	            if ( keyWords.length > 2 ) {
	                gc.fillText(keyWords[1], xPositionDate, yPositionDate);
	                gc.fillText(keyWords[2], xPositionDate, yPositionDate + font.getSize());
	            }
            }
        }
    }
    
    public void scrollDown () {
        if ( this.displayStart > 0 ) {
            this.displayStart--;
        }
        redraw();
    }
    
    public void scrollUp () {
        if ( this.displayStart < this.totalTasks - 1 ) {
            this.displayStart++;
        }
        redraw();
    }

    public void clearEvents () {
        this.taskDisplay.clear();
        this.totalTasks = 0;
    }

    public void addEvents ( EventList toDisplay ) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("D d EEE");
        for ( Event event : toDisplay ) {
            Date currentDay = event.getEarliestDateFromNow();
            if ( currentDay == null ) { //Floating task
                if (this.taskDisplay.containsKey("F")) {
                    if (!this.taskDisplay.get("F").contains(event)) {
                        this.taskDisplay.get("F").add(event);
                    }
                } else {
                    EventList tasksOnDay = new EventList();
                    tasksOnDay.add(event);
                    this.taskDisplay.put("F", tasksOnDay);
                }
            } else { //Day tasks
                String day = dateFormat.format(currentDay);
                if (this.taskDisplay.containsKey(day)) {
                    if (!this.taskDisplay.get(day).contains(event)) {
                        this.taskDisplay.get(day).add(event);
                    }
                } else {
                    EventList tasksOnDay = new EventList();
                    tasksOnDay.add(event);
                    this.taskDisplay.put(day, tasksOnDay);
                }
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
