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
    private double maxBarHeight;
    private double labelWidth;

    public TimelineRenderer() {
        this.weekDisplay = new TreeMap<String, ArrayList<TimelineBarRenderer>>();
	}

    public void setParams (double xPad, double yPad, double maxBarHeight, double labelWidth) {
        this.xPadding = xPad;
        this.yPadding = yPad;
        
        this.maxBarHeight = maxBarHeight;
        this.labelWidth = labelWidth;
    }

    @Override
    public void draw(GraphicsContext gc, double x, double y, double width, double height) {
    	super.draw(gc, x, y, width, height);

    	Font font = Font.loadFont("file:res/monaco.ttf", this.labelWidth/5);
    	
        double barHeight = (height - (this.yPadding * this.weekDisplay.size() * 2) - font.getSize()) / (this.weekDisplay.size());
        barHeight = (barHeight > this.maxBarHeight) ? this.maxBarHeight : barHeight;
        
        double usableWidth = width - this.labelWidth - (this.yPadding * 2);

		gc.setFill(AppColours.panelBackground);
        gc.fillRect(x, y, width, height);

        double xCurrent = x + this.xPadding + this.labelWidth;
        double yCurrent = y + (this.yPadding * 2) + font.getSize();
        double divisorWidth = (1 / TimelineBarRenderer.MINUTES_IN_DAY) * usableWidth;
        for (int i = 0; i <= 24; i++) {
        	gc.setFill(AppColours.tasklistRowBackground);
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setFont(font);
            gc.setTextBaseline(VPos.TOP);
            
            gc.fillText ( String.valueOf((i % 12) == 0 ? 12 : i % 12), xCurrent, y + this.yPadding );
            
        	gc.setFill(AppColours.primaryColour);
			gc.fillRect(xCurrent, y + this.yPadding + font.getSize(), divisorWidth, height - (this.xPadding * 2) - font.getSize());
        	xCurrent += (60 / TimelineBarRenderer.MINUTES_IN_DAY) * usableWidth;
        }
        xCurrent = x + this.labelWidth;
        
        for ( Map.Entry<String, ArrayList<TimelineBarRenderer>> entry : this.weekDisplay.entrySet()) {
        	String key = entry.getKey();
            ArrayList<TimelineBarRenderer> value = entry.getValue();

            gc.setFill(AppColours.primaryColour);
            gc.setTextAlign(TextAlignment.RIGHT);
            gc.setFont(font);
            gc.setTextBaseline(VPos.CENTER);

            String [] keyWords = key.split(" ");
            gc.fillText ( keyWords[1] + " " + keyWords[2], xCurrent - this.xPadding, yCurrent + (barHeight * 0.5) );
            
            for (TimelineBarRenderer bar : value) {
                bar.draw(gc, AppColours.tasklistRowBackground, AppColours.primaryColour, xCurrent + this.xPadding, yCurrent, usableWidth, barHeight);
            }
            
            yCurrent += ( barHeight + this.yPadding );
        }
    }

    public void clear () {
        this.weekDisplay.clear();
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
				if ( date.getDaysBetween() == 0 ) {
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
						this.addDateRangeToDisplay(dateFormat.format(currentDay),
													0, TimelineBarRenderer.MINUTES_IN_DAY, String.valueOf(index));
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
