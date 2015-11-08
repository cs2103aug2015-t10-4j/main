package carelender.view.gui.components;

import carelender.model.strings.AppColours;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.Calendar;
import carelender.model.data.DateRange;
import carelender.model.data.Event;
import carelender.model.data.EventList;

/**
 * Written by : Weizheng Lee 27/10/2015
 * This class contains static methods to help to render the calendar view
 */
public class TimelineRenderer extends CanvasRenderer {
    private Map<String, ArrayList<TimelineBarRenderer>> weekDisplay;

    private double xPadding;
    private double yPadding;
    private double maxBarWidth;
    private double minBarWidth;
    private double labelWidth;
    
    private int displayStart;

    public TimelineRenderer() {
        this.weekDisplay = new TreeMap<String, ArrayList<TimelineBarRenderer>>();
        this.displayStart = 0;
	}

    public void setParams (double xPad, double yPad, double maxBarWidth, double labelWidth) {
        this.xPadding = xPad;
        this.yPadding = yPad;
        
        this.maxBarWidth = maxBarWidth;
        this.minBarWidth = maxBarWidth * 0.1;
        this.labelWidth = labelWidth;
    }

    @Override
    public void draw(GraphicsContext gc, double x, double y, double width, double height) {
    	super.draw(gc, x, y, width, height);

    	Font font = Font.loadFont("file:res/monaco.ttf", this.labelWidth/5);
    	Font fontLarge = Font.loadFont("file:res/monaco.ttf", this.labelWidth/3);
    	
    	int numberOfBars = this.weekDisplay.size();
    	int lastDay = -1;
    	for ( Map.Entry<String, ArrayList<TimelineBarRenderer>> entry : this.weekDisplay.entrySet()) {
        	String key = entry.getKey();
            String [] keyWords = key.split(" ");
            if ( lastDay != -1 ) {
            	int freeDays = Integer.parseInt(keyWords[0]) - lastDay - 1;
            	
            	if ( freeDays > 0 ) {
            		numberOfBars += freeDays;
            	}
            }
            lastDay = Integer.parseInt(keyWords[0]);
    	}
    	
        double barWidth = (width - (this.xPadding * (numberOfBars - 1)) - font.getSize()) / (numberOfBars);
        barWidth = (barWidth > this.maxBarWidth) ? this.maxBarWidth : barWidth;
        barWidth = (barWidth < this.minBarWidth) ? this.minBarWidth : barWidth;
        
        double usableWidth = width - this.labelWidth - (this.xPadding * 2);
        double usableHeight = height - 50 - (this.yPadding * 2);

		gc.setFill(AppColours.panelBackground);
        gc.fillRect(x, y, width, height);

        double xCurrent = x + this.labelWidth - this.xPadding;
        double yCurrent = y + (this.yPadding * 2) + font.getSize();
        double divisorWidth = (1 / TimelineBarRenderer.MINUTES_IN_DAY) * usableHeight;
        for (int i = 0; i <= 24; i++) {
            gc.setTextAlign(TextAlignment.RIGHT);
            gc.setTextBaseline(VPos.CENTER);
            if ( i % 6 == 0 ) {
                gc.setFont(fontLarge);
                gc.setGlobalAlpha(1);
                
                gc.setFill(AppColours.tasklistRowBackground);
            	gc.fillText ( String.valueOf((i % 12) == 0 ? 12 : i % 12), xCurrent, yCurrent + (this.yPadding) );
            	
            	gc.setFill(AppColours.primaryColour);
            	gc.fillRect(xCurrent + this.xPadding, yCurrent + (this.yPadding) - (divisorWidth * 1.5), usableWidth, divisorWidth * 3);
            	gc.setGlobalAlpha(1);
            } else {
            	gc.setFont(font);
            	gc.setGlobalAlpha(0.3);
            	
            	gc.setFill(AppColours.tasklistRowBackground);
            	gc.fillText ( String.valueOf((i % 12) == 0 ? 12 : i % 12), xCurrent, yCurrent + (this.yPadding) );
            	
            	gc.setFill(AppColours.primaryColour);
            	gc.fillRect(xCurrent + this.xPadding, yCurrent + (this.yPadding) - (divisorWidth * 1.5), usableWidth, divisorWidth * 3);
            	gc.setGlobalAlpha(1);
            }
        	yCurrent += (60 / TimelineBarRenderer.MINUTES_IN_DAY) * usableHeight;
        }
        
        int currentTaskToDisplay = 0;
        double remainingHeight = usableWidth;
        lastDay = -1;
        xCurrent = x + this.labelWidth;
        for ( Map.Entry<String, ArrayList<TimelineBarRenderer>> entry : this.weekDisplay.entrySet()) {
        	if ( currentTaskToDisplay >= this.displayStart ) {
	        	String key = entry.getKey();
	            ArrayList<TimelineBarRenderer> value = entry.getValue();
	
	            gc.setTextAlign(TextAlignment.CENTER);
	            gc.setFont(font);
	            gc.setTextBaseline(VPos.CENTER);
	
	            String [] keyWords = key.split(" ");
	            if ( lastDay != -1 ) {
	            	int freeDays = Integer.parseInt(keyWords[0]) - lastDay - 1;
	            	
	            	if ( freeDays > 0 ) {
	            		if ( (remainingHeight - (barWidth) - this.xPadding) >= 0 ) {
		            		yCurrent = y + ( this.yPadding * 2) + font.getSize();
		            		gc.setGlobalAlpha(0.3);
		            		gc.setFill(AppColours.information);
		                	gc.fillRect(xCurrent, yCurrent + (this.yPadding), barWidth, usableHeight);
		                	gc.setGlobalAlpha(1);
		                	
		                	gc.setFill(AppColours.tasklistRowBackground);
		            		yCurrent = y + (this.yPadding) + (usableHeight * 0.5);
		                    gc.fillText ( String.valueOf(freeDays), xCurrent + (barWidth * 0.5), yCurrent );
		                    gc.fillText ( "Days", xCurrent + (barWidth * 0.5), yCurrent + font.getSize() );
		                    gc.fillText ( "Free", xCurrent + (barWidth * 0.5), yCurrent + (font.getSize() * 2) );
		                    
		                    xCurrent += ( barWidth + this.xPadding );
		                    remainingHeight -= (barWidth + this.xPadding);
	            		} else {
	            			System.out.println ( "YESSSSS" );
	            			break;
	            		}
	            	}
	            }
	            lastDay = Integer.parseInt(keyWords[0]);
		            
	            if ( (remainingHeight - barWidth - this.xPadding) >= 0 ) {
		            yCurrent = y + (this.yPadding);
		            gc.setFill(AppColours.primaryColour);
		            gc.fillText ( keyWords[1], xCurrent + (barWidth * 0.5), yCurrent );
		            gc.fillText ( keyWords[2], xCurrent + (barWidth * 0.5), yCurrent + font.getSize() );
		            
		            yCurrent = y + ( this.yPadding * 2) + font.getSize();
		            for (TimelineBarRenderer bar : value) {
		                bar.draw(gc, AppColours.tasklistRowBackground, AppColours.primaryColour, xCurrent, yCurrent + this.yPadding, barWidth, usableHeight);
		            }
		            
		            xCurrent += ( barWidth + this.xPadding );
		            remainingHeight -= (barWidth + this.xPadding);
        		} else {
        			System.out.println ( "AAAHHHH" );
        			break;
        		}	
        	}
        	currentTaskToDisplay++;
        }
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

	public void addEvents ( EventList toDisplay ) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("D d EEE");
		int index = 1;
		for ( Event event : toDisplay ) {
			DateRange [] dateRange = event.getDateRange();
			if ( dateRange == null ) {
				continue;
			}
			for ( DateRange date : dateRange ) {
				if (dateFormat.format(date.getEnd()).equals(dateFormat.format(date.getStart()))) {
					this.addDateRangeToDisplay(dateFormat.format(date.getStart()),
												this.getTimeInMinutes(date.getStart()),
												this.getTimeInMinutes(date.getEnd()), String.valueOf(index));
				} else {
					this.addDateRangeToDisplay(dateFormat.format(date.getStart()),
												this.getTimeInMinutes(date.getStart()),
												TimelineBarRenderer.MINUTES_IN_DAY, String.valueOf(index));
					Date currentDay = date.getStart();
					for ( int i = 0; i < date.getDaysBetween(); i++ ) {
						currentDay = this.addDays(currentDay, 1);
						if (!(dateFormat.format(date.getEnd()).equals(dateFormat.format(currentDay)))) {
							this.addDateRangeToDisplay(dateFormat.format(currentDay),
														0, TimelineBarRenderer.MINUTES_IN_DAY, String.valueOf(index));
					
						}
					}
					this.addDateRangeToDisplay(dateFormat.format(date.getEnd()),
												0, this.getTimeInMinutes(date.getEnd()), String.valueOf(index));
				}
			}
			index++;
		}
	}
	
	private void addDateRangeToDisplay ( String key, double startTime, double endTime, String content ) {
		if (this.weekDisplay.containsKey(key)) {
			this.weekDisplay.get(key).add(createWeekBar(startTime, endTime, content));
		} else {
			ArrayList<TimelineBarRenderer> tasksOnDay = new ArrayList<TimelineBarRenderer>();
			tasksOnDay.add(createWeekBar(startTime, endTime, content));
			this.weekDisplay.put (key, tasksOnDay);
		}
	}
	
	private TimelineBarRenderer createWeekBar (double startTime, double endTime, String content) {
		TimelineBarRenderer bar = new TimelineBarRenderer();
		bar.setParams(this.gc, this.width, this.height, startTime, endTime, content);
		return bar;
	}
	
	private double getTimeInMinutes ( Date date ) {
		if ( date == null ) {
			return 0;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int hours = calendar.get(Calendar.HOUR_OF_DAY);
		int minutes = calendar.get(Calendar.MINUTE);
		
		return ((hours * 60) + minutes);
	}
	
	public Date addDays ( Date date, int days )
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        
        return calendar.getTime();
    }


}
