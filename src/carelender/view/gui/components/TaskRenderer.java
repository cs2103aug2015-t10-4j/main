//@@author A0125566B
package carelender.view.gui.components;

import carelender.model.strings.AppColours;
import carelender.model.strings.DateFormats;
import carelender.model.strings.FontLoader;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Collections;
import java.util.Comparator;

import carelender.model.data.Event;
import carelender.model.data.EventList;

/**
<<<<<<< HEAD
 * This class contains methods to render the task view.
=======
 * Render the Task View.
>>>>>>> 37a3a97e647e06c6b39ed41f6b50286090262797
 */
public class TaskRenderer extends CanvasRenderer {
	//Ratios used to ensure sizes scale with the window height and width.
	private static final double TASKBAR_NAME_RATIO = 0.4;
	private static final double DIVISOR_HEIGHT_RATIO = 0.5;
	private static final double SCROLLPOINTER_HEIGHT_RATIO = 0.04;
	private static final double SCROLLPOINTER_WIDTH_RATIO = 0.02;
	
	private static final double INDEX_FONTSIZE_RATIO = 0.5;
	private static final double DATE_FONTSIZE_RATIO = 0.5;
	private static final double SCROLLPOINTER_FONTSIZE_RATIO = 0.02;
	
	private static final String FLOATING_TASK_KEY = "F";
	private static final String SCROLLPOINTER_PAGEUP = "Pg Up";
	private static final String SCROLLPOINTER_PAGEDOWN = "Pg Dn";
	
    private Map<String, EventList> taskDisplay;
    private Comparator<Event> eventComparator;
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
        this.taskDisplay = new TreeMap<>();
        
        this.totalTasks = 0;
        this.displayStart = 0;

        //Comparator to compare events by date range.
        this.eventComparator = (Event eventAgainst, Event eventTo) -> {
            if ( eventAgainst.getEarliestDate() == null || eventTo.getEarliestDate() == null ) {
                return 0;
            }

            if (eventAgainst.getEarliestDate().before(eventTo.getEarliestDate())) {
                return -1;
            } else if (eventAgainst.getEarliestDate().after(eventTo.getEarliestDate())) {
                return 1;
            }
            return 0;
        };
    }
    
    /**
     * Set the ratios for the task bars and date display, along with padding.
     * Ratios are with respect to the width and height of the window respectively.
     * 
     * @param xPad
     * @param yPad
     * @param taskWidthRatio
     * @param taskHeightRatio
     * @param dateWidthRatio
     * @param dateHeightRatio
     */
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
        
        taskBarRender.setParams(xPadding, yPadding, TASKBAR_NAME_RATIO);
        
        gc.setFill(AppColours.panelBackground);
        gc.fillRect(x, y, width, height);
        
        renderTasks (gc, x, y, width, height);
    }
    
    /**
     * Shifts the scroll window up for the task renderer.
     * Redraws canvas on each call.
     */
    public void scrollDown () {
        if (displayStart > 0) {
        	displayStart--;
        }
        redraw();
    }
    
    /**
     * Shifts the scroll window down for the task renderer.
     * Redraws canvas on each call.
     */
    public void scrollUp () {
        if (displayStart < totalTasks - 1) {
            displayStart++;
        }
        redraw();
    }

    /**
     * Remove events in taskDisplay map and sets the scroll window back to default.
     */
    public void clearEvents () {
        taskDisplay.clear();
        totalTasks = 0;
        displayStart = 0;
    }
    
    /**
     * Concatenates content of each EventList within taskDisplay TreeMap into one EventList.
     * 
     * @return
     * 		EventList with all events in the taskDisplay.
     */
    public EventList getDisplayList () {
        List<Event> concatList = new EventList();
        for (EventList events : taskDisplay.values()) {
            concatList.addAll(events);
        }
        return (EventList)concatList;
    }

    /**
     * Adds all events in EventList to taskDisplay, with the day of the event as keys
     * Events stretching over several days are added into several key-value pairs in the taskDislay
     * 
     * @param toDisplay
     * 		EventList containing events to be added to taskDisplay
     */
    public void addEvents (EventList toDisplay) {
        for ( Event event : toDisplay ) {
            Date currentDay = event.getEarliestDateFromNow();
            //If there is no earliest date range from the current time
            //Find the earliest date of all the date ranges.
            if ( currentDay == null ) {
            	currentDay = event.getEarliestDate();
            }
            
            if ( currentDay == null ) { //Event is a floating task.
                addEventToMap(FLOATING_TASK_KEY, event);
            } else {
                //Concatenate all the parts of the key together.
                addEventToMap(formatKey(currentDay), event);
            }
            totalTasks++;
        }
        
        for (EventList events : taskDisplay.values()) {
        	Collections.sort(events, eventComparator);
        }
    }
    
    /**
     * Format key as YYYY D d EEE given date.
     * YYYY and D are used to sort the keys by date.
     * 
     * YYYY is the year of the event.
     * D is the day in the year.
     * d is the day in the month.
     * EEE is the day of the week.
     * 
     * @param date
     * 		Date to format as key.
     * @return
     * 		Key to use in taskDisplay
     */
    private String formatKey (Date date) {
		
        String day = DateFormats.DATE_FORMAT_DAY.format(date);
        String dayInYear = DateFormats.DAY_IN_YEAR.format(date);
        //If the day in the year is less than 3 digits, prepend with 0s, eg. 64 to 064
        //Necessary for radix sort of String keys in TreeMap to ensure order is preserved.
        for (int i = 0; i < (3 - dayInYear.length()); i++ ) {
        	dayInYear = "0" + dayInYear;
        }
        String year = DateFormats.YEAR.format(date);
        
        //Concatenate all the parts of the key together.
        return (year + " " + dayInYear + " " + day);
	}
    
    /**
     * Add an event to taskDisplay with key.
     * If the key already exists in taskDisplay add it to the EventList associated.
     * Otherwise, create a new EventList containing the event to add associated with the key.
     * 
     * @param key
     * 		The day of the event is used as the key as YYYY D d EEE
     * @param event
     * 		The event to add.
     */
    private void addEventToMap (String key, Event event) {
    	if (taskDisplay.containsKey(key)) {
            if (!taskDisplay.get(key).contains(event)) {
                taskDisplay.get(key).add(event);
            }
        } else {
            EventList tasksOnDay = new EventList();
            tasksOnDay.add(event);
            taskDisplay.put(key, tasksOnDay);
        }
    }
    
    /**
     * Render the task bars.
     * 
     * @param gc
     * @param x
     * @param y
     * @param width
     * @param height
     */
    private void renderTasks (GraphicsContext gc, double x, double y, double width, double height) {
    	double xCurrent = x + xPadding;
        double yCurrent = y + yPadding;

        double dateBarWidth = width * dateWidthRatio;
        double dateBarHeight = height * dateHeightRatio;
        
        double taskBarWidth = width * taskWidthRatio;
        double taskBarHeight = height * taskHeightRatio;
        
        Font dateFont = FontLoader.load(dateBarHeight * DATE_FONTSIZE_RATIO);
        Font indexFont = FontLoader.load(dateBarHeight * INDEX_FONTSIZE_RATIO);
        Font scrollPointerFont = FontLoader.load(width * SCROLLPOINTER_FONTSIZE_RATIO);
    	
        //Variables to keep track of scroll window.
        int currentTaskToDisplay = 0;
        //Available space in the task view window to draw task bars.
        double remainingHeight = this.height - (this.yPadding * 2);
        boolean showBottomArrow = false;
        //Variables for date display.
        double xPositionDate;
        double yPositionDate;
        boolean displayDate;
        
        //Indexing for events.
        //Used for update/delete/show commands.
        int index = 1;

        for ( Map.Entry<String, EventList> entry : this.taskDisplay.entrySet()) {
            String key = entry.getKey();
            EventList value = entry.getValue();

            if ( currentTaskToDisplay >= displayStart ) {
            	renderDivisor(gc, xCurrent, yCurrent, width, yPadding);
	            yCurrent += (yPadding * 1.5);
            }
            
            xPositionDate = xCurrent;
            yPositionDate = yCurrent;

            displayDate = false;
            for (Event event : value) {
                if ( currentTaskToDisplay >= displayStart ) {
                	taskBarRender.setContent(event);

				    if ( (remainingHeight - taskBarRender.getHeight(taskBarHeight)) >= 0 ) {
						displayDate = true;
						
						renderIndex(gc, xCurrent + dateBarWidth, yCurrent, indexFont, String.valueOf(index));
						
						taskBarRender.draw(gc, xCurrent + dateBarWidth + xPadding, yCurrent,
												taskBarWidth, taskBarHeight, AppColours.tasklistRowBackground,
												AppColours.tasklistText, true);
						
						yCurrent += (taskBarRender.getHeight(taskBarHeight) + yPadding);
						remainingHeight -= (taskBarRender.getHeight(taskBarHeight) + (yPadding * 2));
				     } else {
				    	//Task to add exceeds the remaining space in the window.
						showBottomArrow = true;
						break;
				    }
            	}
                index++;
                currentTaskToDisplay++;
            }

            if ( displayDate ) {
            	renderDate(gc, xPositionDate, yPositionDate, dateFont, key);
            }
            
            if ( showBottomArrow ) {
            	//Prevent subsequent tasks from being printed within scroll window.
            	break;
            }
        }
        
        if ( this.displayStart > 0 ) {
        	xCurrent = x + width - (width * SCROLLPOINTER_HEIGHT_RATIO) - xPadding;
            yCurrent = y + yPadding;
            
            renderScrollPointerUp(gc, xCurrent, yCurrent, width, scrollPointerFont);
        }
        
        if ( showBottomArrow ) {
        	xCurrent = x + width - (width * SCROLLPOINTER_HEIGHT_RATIO) - xPadding;
            yCurrent = y + height - (width * SCROLLPOINTER_HEIGHT_RATIO) - yPadding;
            
        	renderScrollPointerDown(gc, xCurrent, yCurrent, width, scrollPointerFont);
        }
    }
    
    /**
     * Render the divisors between each day in task view.
     * 
     * @param gc
     * @param xCurrent
     * @param yCurrent
     * @param width
     * @param height
     */
    private void renderDivisor (GraphicsContext gc, double xCurrent, double yCurrent, double width, double height) {
    	gc.setFill (AppColours.tasklistRowBackground);
        gc.fillRect(xCurrent, yCurrent, width - (xPadding * 2), height * DIVISOR_HEIGHT_RATIO);
    }
    
    /**
     * Render the index for each task.
     * 
     * @param gc
     * @param xCurrent
     * @param yCurrent
     * @param indexFont
     * @param index
     */
    private void renderIndex (GraphicsContext gc, double xCurrent, double yCurrent,
    							Font indexFont, String index) {
    	gc.setFill(AppColours.primaryColour);
        gc.setTextAlign(TextAlignment.RIGHT);
        gc.setFont(indexFont);
        gc.setTextBaseline(VPos.TOP);

        gc.fillText(index, xCurrent, yCurrent);
    }
    
    /**
     * Render the date for each day in task view.
     * 
     * @param gc
     * @param xCurrent
     * @param yCurrent
     * @param dateFont
     * @param key
     */
    private void renderDate (GraphicsContext gc, double xCurrent, double yCurrent,
    							Font dateFont, String key) {
    	gc.setFill(AppColours.tasklistRowBackground);
        gc.setTextAlign(TextAlignment.LEFT);
       	gc.setFont(dateFont);
    	gc.setTextBaseline(VPos.TOP);

        String [] keyWords = key.split(" ");
        //Only display the day of month and day of week of the key.
        if ( keyWords.length > 2 ) {
            gc.fillText(keyWords[2], xCurrent, yCurrent);
            gc.fillText(keyWords[3], xCurrent, yCurrent + dateFont.getSize());
        }
    }
    
    /**
     * Render the scroll pointer for page up.
     * 
     * @param gc
     * @param xCurrent
     * @param yCurrent
     * @param width
     * @param scrollPointerFont
     */
    private void renderScrollPointerUp (GraphicsContext gc, double xCurrent, double yCurrent,
    									double width, Font scrollPointerFont) {
    	gc.setFill (AppColours.important);
    	gc.fillPolygon(new double[] {xCurrent, xCurrent + (width * SCROLLPOINTER_WIDTH_RATIO), xCurrent - (width * SCROLLPOINTER_WIDTH_RATIO)},
    					new double[] {yCurrent, yCurrent + (width * SCROLLPOINTER_HEIGHT_RATIO), yCurrent + (width * SCROLLPOINTER_HEIGHT_RATIO)}, 3);
    	
    	yCurrent += (width * SCROLLPOINTER_HEIGHT_RATIO);
    	
    	gc.setFill(AppColours.important);
        gc.setTextAlign(TextAlignment.CENTER);
       	gc.setFont(scrollPointerFont);
    	gc.setTextBaseline(VPos.TOP);
    	gc.fillText(SCROLLPOINTER_PAGEUP, xCurrent, yCurrent);
    }
    
    /**
     * Render the scroll pointer for page down.
     * 
     * @param gc
     * @param xCurrent
     * @param yCurrent
     * @param width
     * @param scrollPointerFont
     */
    private void renderScrollPointerDown (GraphicsContext gc, double xCurrent, double yCurrent,
    										double width, Font scrollPointerFont) {
    	gc.setFill (AppColours.important);
    	gc.fillPolygon(new double[] {xCurrent, xCurrent + (width * SCROLLPOINTER_WIDTH_RATIO), xCurrent - (width * SCROLLPOINTER_WIDTH_RATIO)},
    					new double[] {yCurrent + (width * SCROLLPOINTER_HEIGHT_RATIO), yCurrent, yCurrent}, 3);
    	
    	gc.setFill(AppColours.important);
        gc.setTextAlign(TextAlignment.CENTER);
       	gc.setFont(scrollPointerFont);
    	gc.setTextBaseline(VPos.BOTTOM);
    	gc.fillText(SCROLLPOINTER_PAGEDOWN, xCurrent, yCurrent);
    }
}
