//@@author A0125566B
package carelender.view.gui.components;

import carelender.model.strings.AppColours;
import carelender.model.strings.DateFormats;
import carelender.model.strings.FontLoader;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.Calendar;
import carelender.model.data.DateRange;
import carelender.model.data.Event;
import carelender.model.data.EventList;

/**
 * This class contains methods to render the Timeline View
 */
public class TimelineRenderer extends CanvasRenderer {
	private static final double BAR_SIZE_MIN_RATIO = 0.5;
	
	private static final double FADEOUT_ALPHA = 0.3;
	private static final double DEFAULT_ALPHA = 1;
	
	private static final double FONT_SIZE_RATIO = 0.2;
	private static final double FONT_LARGE_SIZE_RATIO = 0.3;
	
	private static final double DIVISOR_SCALE_RATIO = 3;
	private static final double DIVISOR_LARGE_FONT_MOD = 6;
	
	private static final String FREE_BAR_TEXT_LINE1 = "Days";
	private static final String FREE_BAR_TEXT_LINE2 = "Free";
	
    private Map<String, ArrayList<TimelineBarRenderer>> weekDisplay;

    private double xPadding;
    private double yPadding;
    private double maxBarWidth;
    private double minBarWidth;
    private double labelWidth;
    
    private int displayStart;

    public TimelineRenderer() {
        this.weekDisplay = new TreeMap<>();
        this.displayStart = 0;
	}

    /**
     * Set up the dimensions for the timeline view.
     * 
     * @param xPad
     * @param yPad
     * @param maxBarWidth
     * 		Maximum size of each timeline bar.
     * @param labelWidth
     * 		Size to be reserved on the left for time labels.
     */
    public void setParams (double xPad, double yPad, double maxBarWidth, double labelWidth) {
        this.xPadding = xPad;
        this.yPadding = yPad;
        
        this.maxBarWidth = maxBarWidth;
        this.minBarWidth = maxBarWidth * BAR_SIZE_MIN_RATIO;
        this.labelWidth = labelWidth;
    }

    @Override
    public void draw (GraphicsContext gc, double x, double y, double width, double height) {
    	super.draw(gc, x, y, width, height);

    	Font font = FontLoader.load( labelWidth * FONT_SIZE_RATIO);
    	Font fontLarge = FontLoader.load( labelWidth * FONT_LARGE_SIZE_RATIO);
        
        double xCurrent = x + this.labelWidth - this.xPadding;
        double yCurrent = y + (this.yPadding * 2) + font.getSize();
    	
		gc.setFill(AppColours.panelBackground);
        gc.fillRect(x, y, width, height);

        renderDivisors(gc, xCurrent, yCurrent, width, height, font, fontLarge);

        xCurrent = x + this.labelWidth;
        renderBars(gc, xCurrent, yCurrent, width, height, font);
    }

    public void clear () {
        this.weekDisplay.clear();
        this.displayStart = 0;
    }
    
    public void scrollDown () {
        if ( this.displayStart > 0 ) {
            this.displayStart--;
        }
        redraw();
    }
    
    public void scrollUp () {
        if ( this.displayStart < this.weekDisplay.size() - 1 ) {
            this.displayStart++;
        }
        redraw();
    }

    /**
     * Add events from EventList to weekDisplay.
     * Events are indexed according to the order within toDisplay.
     * 
     * @param toDisplay
     * 		EventList to add to weekDisplay.
     */
	public void addEvents ( EventList toDisplay ) {
		int index = 1;
		for ( Event event : toDisplay ) {
			DateRange [] dateRange = event.getDateRange();
			if ( dateRange == null ) {
				continue;
			}
			for ( DateRange date : dateRange ) {
				String keyEnd = DateFormats.FORMAT_KEY(date.getEnd());
				String keyStart = DateFormats.FORMAT_KEY(date.getStart());
				//Event has a single date
				if (keyEnd.equals(keyStart)) {
					addDateRangeToDisplay(keyStart, DateFormats.TIME_IN_MINUTES(date.getStart()),
											DateFormats.TIME_IN_MINUTES(date.getEnd()), String.valueOf(index));
				} else {
					addDateRangeToDisplay(keyStart, DateFormats.TIME_IN_MINUTES(date.getStart()),
											TimelineBarRenderer.MINUTES_IN_DAY, String.valueOf(index));
					Date currentDay = date.getStart();
					//Event spans multiple days, add event to each day between start and end dates.
					for ( int i = 0; i < date.getDaysBetween(); i++ ) {
						currentDay = DateFormats.ADD_DAYS(currentDay, 1);
						if (!(keyEnd.equals(DateFormats.FORMAT_KEY(currentDay)))) {
							addDateRangeToDisplay(DateFormats.FORMAT_KEY(currentDay), 0,
													TimelineBarRenderer.MINUTES_IN_DAY, String.valueOf(index));
					
						}
					}
					this.addDateRangeToDisplay(keyEnd, 0, DateFormats.TIME_IN_MINUTES(date.getEnd()),
												String.valueOf(index));
				}
			}
			index++;
		}
	}
	
	/**
	 * Render all the bars in the timeline.
	 * 
	 * @param gc
	 * @param xCurrent
	 * @param yCurrent
	 * @param width
	 * @param height
	 * @param font
	 */
	private void renderBars (GraphicsContext gc, double xCurrent, double yCurrent,
								double width, double height, Font font) {
    	int currentTaskToDisplay = 0;
    	int lastDay = -1;
    	int numberOfBars = getNumberOfBars();
    	
    	double barWidth = (width - (xPadding * (numberOfBars - 1)) - font.getSize()) / (numberOfBars);
        barWidth = (barWidth > maxBarWidth) ? maxBarWidth : barWidth;
        barWidth = (barWidth < minBarWidth) ? minBarWidth : barWidth;
        
        double usableWidth = width - labelWidth - (xPadding * 2);
        double usableHeight = height - labelWidth - (yPadding * 2);

        double remainingHeight = usableWidth;
        
		for ( Map.Entry<String, ArrayList<TimelineBarRenderer>> entry : weekDisplay.entrySet()) {
        	if ( currentTaskToDisplay >= displayStart ) {
	        	String key = entry.getKey();
	            ArrayList<TimelineBarRenderer> value = entry.getValue();
	
	            gc.setTextAlign(TextAlignment.CENTER);
	            gc.setFont(font);
	            gc.setTextBaseline(VPos.CENTER);
	
	            String [] keyWords = key.split(" ");
	            if ( lastDay != -1 ) {
	            	int freeDays = Integer.parseInt(keyWords[1]) - lastDay - 1;
	            	
	            	if ( freeDays > 0 ) {
	            		if ( (remainingHeight - barWidth - xPadding) >= 0 ) {
		            		yCurrent = y + (yPadding * 2) + font.getSize();
		            		
		            		renderFreeBar(gc, xCurrent, yCurrent, barWidth, usableHeight,
		            						font, String.valueOf(freeDays));
		                    
		                    xCurrent += ( barWidth + xPadding );
		                    remainingHeight -= (barWidth + xPadding);
	            		} else {
	            			break;
	            		}
	            	}
	            }
	            lastDay = Integer.parseInt(keyWords[1]);
		            
	            if ( (remainingHeight - barWidth - xPadding) >= 0 ) {
		            yCurrent = y + yPadding;
		            
		            renderTimelineBar(gc, xCurrent, yCurrent, barWidth, usableHeight,
		            					font, keyWords[2], keyWords[3], value);	

		            xCurrent += (barWidth + xPadding);
		            remainingHeight -= (barWidth + xPadding);
        		} else {
        			break;
        		}	
        	}
        	currentTaskToDisplay++;
        }
	}
	
	/**
	 * Render bars used to display free days between two days.
	 * 
	 * @param gc
	 * @param xCurrent
	 * @param yCurrent
	 * @param width
	 * 		Width of each bar.
	 * @param height
	 * 		Maximum height of each bar.
	 * @param font
	 * @param text
	 * 		The number of days between the two days as a String.
	 */
	private void renderFreeBar (GraphicsContext gc, double xCurrent, double yCurrent,
								double width, double height, Font font, String text) {
		gc.setGlobalAlpha(FADEOUT_ALPHA);
		gc.setFill(AppColours.information);
    	gc.fillRect(xCurrent, yCurrent + yPadding, width, height);
    	gc.setGlobalAlpha(DEFAULT_ALPHA);
    	
    	gc.setFill(AppColours.tasklistRowBackground);
		yCurrent = y + yPadding + (height * 0.5);
        gc.fillText ( text, xCurrent + (width * 0.5), yCurrent );
        gc.fillText ( FREE_BAR_TEXT_LINE1, xCurrent + (width * 0.5), yCurrent + font.getSize() );
        gc.fillText ( FREE_BAR_TEXT_LINE2, xCurrent + (width * 0.5), yCurrent + (font.getSize() * 2) );
	}

	/**
	 * Render the bars within a single day.
	 * Each TimelineBarRenderer renders one block of time used by an event.
	 * 
	 * @param gc
	 * @param xCurrent
	 * @param yCurrent
	 * @param width
	 * 		Width of each bar.
	 * @param height
	 * 		Maximum height of each bar.
	 * @param font
	 * @param dateText
	 * 		String in the form d, where d is the day in the month.
	 * @param dayText
	 * 		String in the form EEE, where E is the day of the week.
	 * @param value
	 * 		ArrayList of TimelineBarRenderer for each bar.
	 */
	private void renderTimelineBar (GraphicsContext gc, double xCurrent, double yCurrent,
									double width, double height, Font font,
									String dateText, String dayText, ArrayList<TimelineBarRenderer> value) {
		gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(font);
        gc.setTextBaseline(VPos.CENTER);
		gc.setFill(AppColours.primaryColour);
        gc.fillText ( dateText, xCurrent + (width * 0.5), yCurrent );
        gc.fillText ( dayText, xCurrent + (width * 0.5), yCurrent + font.getSize() );
        
        yCurrent += yPadding + font.getSize();
        for (TimelineBarRenderer bar : value) {
            bar.draw(gc, AppColours.tasklistRowBackground, AppColours.primaryColour,
            			xCurrent, yCurrent + yPadding, width, height);
        }
	}
	
	/**
	 * Render divisors used to demarcate the time of day.
	 * 24 divisors are rendered for each hour of the day.
	 * 
	 * @param gc
	 * @param xCurrent
	 * @param yCurrent
	 * @param width
	 * @param height
	 * @param font
	 * @param fontLarge
	 */
	private void renderDivisors (GraphicsContext gc, double xCurrent, double yCurrent,
									double width, double height, Font font, Font fontLarge) {
		double usableWidth = width - labelWidth - (xPadding * 2);
        double usableHeight = height - labelWidth - (yPadding * 2);
        double divisorWidth = (1 / TimelineBarRenderer.MINUTES_IN_DAY) * usableHeight;
        
		for (int i = 0; i <= 24; i++) {
            gc.setTextAlign(TextAlignment.RIGHT);
            gc.setTextBaseline(VPos.CENTER);
            //Every (DIVISOR_LARGE_FONT_MOD)th divisor is increased in size and opacity.
            if ( i % DIVISOR_LARGE_FONT_MOD == 0 ) {
            	renderDivisor(gc, xCurrent, yCurrent, usableWidth, divisorWidth,
    					fontLarge, String.valueOf((i % 12) == 0 ? 12 : i % 12), DEFAULT_ALPHA);
            } else {
            	renderDivisor(gc, xCurrent, yCurrent, usableWidth, divisorWidth,
            					font, String.valueOf((i % 12) == 0 ? 12 : i % 12), FADEOUT_ALPHA);
            }
        	yCurrent += (60 / TimelineBarRenderer.MINUTES_IN_DAY) * usableHeight;
        }
	}
	
	/**
	 * Render each divisor.
	 * 
	 * @param gc
	 * @param xCurrent
	 * @param yCurrent
	 * @param width
	 * 		Width of the divisor.
	 * @param height
	 * 		Height of the divisor.
	 * @param font
	 * @param text
	 *		Time of the day in hours, as a String.
	 * @param alpha
	 */
	private void renderDivisor (GraphicsContext gc, double xCurrent, double yCurrent,
								double width, double height, Font font, String text, double alpha) {
		gc.setFont(font);
    	gc.setGlobalAlpha(alpha);
    	
    	gc.setFill(AppColours.tasklistRowBackground);
    	gc.fillText ( text, xCurrent, yCurrent + yPadding );
    	
    	gc.setFill(AppColours.primaryColour);
    	gc.fillRect(xCurrent + xPadding, yCurrent + yPadding - (height * DIVISOR_SCALE_RATIO * 0.5),
    				width, height * DIVISOR_SCALE_RATIO);
    	gc.setGlobalAlpha(DEFAULT_ALPHA);
	}

    /**
     * Return number of bars in total.
     * Bars used to display free days are counted as well.
     * 
     * @return
     * 		Number of bars.
     */
    private int getNumberOfBars () {
    	int numberOfBars = weekDisplay.size();
    	int lastDay = -1;
    	//Count the number of bars to display in total, including bars used to show free days.
    	for (Map.Entry<String, ArrayList<TimelineBarRenderer>> entry : weekDisplay.entrySet()) {
        	String key = entry.getKey();
            String [] keyWords = key.split(" ");
            //Compare against previous day in the display.
            if ( lastDay != -1 ) {
            	//Compare using the 2nd delimited chunk of the key, which is the day of the year.
            	int freeDays = Integer.parseInt(keyWords[1]) - lastDay - 1;
            	
            	if ( freeDays > 0 ) {
            		numberOfBars += freeDays;
            	}
            }
            lastDay = Integer.parseInt(keyWords[1]);
    	}
    	return numberOfBars;
    }
	
    /**
     * Add a date range to the weekDisplay.
     * Date ranges with the same key are stored in an ArrayList.
     * Each date range represents a bar on a single day.
     * 
     * @param key
     * @param startTime
     * @param endTime
     * @param content
     * 		The index of the event to be displayed on the bar.
     */
	private void addDateRangeToDisplay (String key, double startTime, double endTime, String content) {
		if (weekDisplay.containsKey(key)) {
			weekDisplay.get(key).add(createWeekBar(startTime, endTime, content));
		} else {
			ArrayList<TimelineBarRenderer> tasksOnDay = new ArrayList<TimelineBarRenderer>();
			tasksOnDay.add(createWeekBar(startTime, endTime, content));
			this.weekDisplay.put (key, tasksOnDay);
		}
	}
	
	/**
	 * Create the TimelineBarRenderer object
	 * 
	 * @param startTime
	 * @param endTime
	 * @param content
	 * @return
	 * 		The TimelineBarRenderer to be added to the weekDisplay.
	 */
	private TimelineBarRenderer createWeekBar (double startTime, double endTime, String content) {
		TimelineBarRenderer bar = new TimelineBarRenderer();
		bar.setParams(startTime, endTime, content);
		return bar;
	}
}
