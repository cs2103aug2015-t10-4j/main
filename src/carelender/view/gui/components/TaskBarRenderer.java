package carelender.view.gui.components;

import carelender.model.data.DateRange;
import carelender.model.data.Event;
import carelender.model.strings.AppColours;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Written by : Weizheng Lee 19/10/2015
 * This class contains static methods to help to render the calendar view
 */
public class TaskBarRenderer {
	private final static double STRIKEOUT_WIDTH = 5;
	
    private double xPadding;
    private double yPadding;

    private ArrayList<DateRangeRenderer> dateRangeRendererList;
    
    private TextRenderer timeText;
    private TextRenderer nameText;

    private double nameTextRatio;
    //private double barRatio;
    
    private boolean strikeout = false;

    private Event event;

    public TaskBarRenderer() {
        this.timeText = new TextRenderer();
        this.nameText = new TextRenderer();
        
        this.dateRangeRendererList = new ArrayList<DateRangeRenderer>();
        this.strikeout = false;
    }

    public void setContent (Event event) {
    	this.event = event;
        /*
        font = Font.loadFont("file:res/monaco.ttf", this.initialHeight * timeTextRatio);
        this.timeText.setParams (gc, this.xPosition + this.xPadding, this.yPosition + this.yPadding + (this.nameText.getTextHeight()),
                this.width - (this.xPadding * 2), font.getSize(), 0, 0, font, 0.6, 0.1);
        this.timeText.clearText();
        */
    	this.strikeout = event.getCompleted();
    	
        this.dateRangeRendererList.clear();
        Date currentDate = new Date();
        DateRange [] dateRanges = this.event.getDateRange();
        if ( dateRanges != null ) {
			for (DateRange date : dateRanges) {
				DateRangeRenderer dateRangeRenderer = new DateRangeRenderer();
				if (date.getStart().after(currentDate) || date.getEnd().after(currentDate)) {
					/*
					this.timeText.addTextEllipsis(timeFormat.format(date.getStart())
							+ " to " + timeFormat.format(date.getEnd()));
							*/
					dateRangeRenderer.setParams(0.1, 0.1,
												0.2, 0.4,
												0.7, 0.7, 0.3,
												date);
					this.dateRangeRendererList.add(dateRangeRenderer);
				} else {
					/*
					this.timeText.addTextEllipsis(timeFormat.format(date.getStart())
							+ " to " + timeFormat.format(date.getEnd()) + "[OVER]");
							*/
					dateRangeRenderer.setParams(0.1, 0.1,
												0.2, 0.4,
												0.7, 0.7, 0.3,
												date);
					dateRangeRenderer.strikeout();
					this.dateRangeRendererList.add(dateRangeRenderer);
				}
			}
		}
        /*
        long difference = date.getEnd().getTime() - date.getStart().getTime();
		long days = TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS);
		numOfDays += days;
        this.timeText.addTextEllipsis(timeFormat.format(this.event.getEarliestDateFromNow())
                                        + "    [" + numOfDays + " day]");*/
    }

    public void setParams (double xPad, double yPad, double nameTextRatio) {
        this.xPadding = xPad;
        this.yPadding = yPad;

        this.nameTextRatio = nameTextRatio;
        //this.barRatio = barRatio;
    }
    
    public void strikeout () {
    	this.strikeout = !this.strikeout;
    }

    public double getHeight (double height) {
    	double toReturnHeight = 0;
    	toReturnHeight += (height * this.nameTextRatio);
    	toReturnHeight += this.yPadding;
    	for (DateRangeRenderer dateRangeRenderer : this.dateRangeRendererList) {
    		toReturnHeight += (dateRangeRenderer.getTimeTextHeightRatio() * height);
    		toReturnHeight += dateRangeRenderer.getDateTextHeightRatio() * (dateRangeRenderer.getTimeTextHeightRatio() * height);
    		toReturnHeight += this.yPadding;
    	}
    	toReturnHeight += this.yPadding;
        return toReturnHeight;
    }
    
    public void draw (GraphicsContext gc, double x, double y,
    					double width, double height,
    					Color backgroundColour, Color textColour, boolean ellipsize) {
        if (gc == null) {
            System.out.println("Error");
        } else {
        	double xCurrent = x;
            double yCurrent = y;
            
        	Font font = Font.loadFont("file:res/monaco.ttf", height * this.nameTextRatio);
        	
        	if ( this.strikeout ) {
        		gc.setGlobalAlpha(0.3);
            } else {
            	gc.setGlobalAlpha(1);
            }
        	
            gc.setFill(backgroundColour);
            gc.fillRect(xCurrent, yCurrent, width, this.getHeight(height));
            
            xCurrent += this.xPadding;
            yCurrent += this.yPadding;
            this.nameText.setParams(gc, xCurrent, yCurrent,
                    width - (this.xPadding * 2), font.getSize(),
                    0, 0, font, 0.6, 0);
            this.nameText.clearText();
            if ( ellipsize ) {
                this.nameText.addTextEllipsis(this.event.getName());
            } else {
                this.nameText.addText(this.event.getName());
            }
            
            this.nameText.drawText(backgroundColour, textColour);

            yCurrent += (this.nameText.getTextHeight() + this.yPadding);
            for (DateRangeRenderer dateRangeRenderer : this.dateRangeRendererList) {
            	dateRangeRenderer.draw(gc, xCurrent, yCurrent, width, height,
            							AppColours.panelBackground, backgroundColour);
            	
            	yCurrent += dateRangeRenderer.getTimeTextHeightRatio() * height;
            	yCurrent += dateRangeRenderer.getDateTextHeightRatio() * (dateRangeRenderer.getTimeTextHeightRatio() * height);
            	yCurrent += this.yPadding;
            }
            gc.setGlobalAlpha(1);
        }
    }
}
