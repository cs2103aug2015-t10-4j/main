//@@author A0125566B
package carelender.view.gui.components;

import carelender.model.data.DateRange;
import carelender.model.strings.AppColours;
import carelender.model.strings.DateFormats;
import carelender.model.strings.FontLoader;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.Calendar;

/**
 * This class contains methods to render a date range.
 */
public class DateRangeRenderer {
    private static final double STRIKEOUT_HEIGHT = 2;
    
    private static final String DATETEXT_NOTIME_START = "Starts";
    private static final String DATETEXT_NOTIME_END = "Ends";

    private double xPaddingRatio;
    private double yPaddingRatio;

    private double timeTextWidthRatio;
    private double timeTextHeightRatio;
    private double dateTextHeightRatio;
    private double connectorWidthRatio;

    private String timeStart;
    private String timeEnd;
    private String dateStart;
    private String dateEnd;
    
    private boolean strikeout;

    public DateRangeRenderer() {
        this.timeStart = "";
        this.timeEnd = "";
        this.dateStart = "";
        this.dateEnd = "";
        
        this.strikeout = false;
    }
    
    /**
     * Set the dimensions for the date range render.
     * Padding ratios are with respect to the dimensions of the elements they pad.
     * 		e.g. Padding for dateText is yPadRatio * dateTextHeight
     * Ratio for dateTextHeight and connecterWidth is with respect to height of timeText.
     * Ratio for timeText are with respect to width and height of window.
     * 
     * @param xPadRatio
     * 		With respect to the dimensions of individual elements.
     * @param yPadRatio
     * 		With respect to the dimensions of individual elements.
     * @param timeTextWidthRatio
     * @param timeTextHeightRatio
     * @param dateTextHeightRatio
     * 		With respect to height of timeText.
     * @param connectorWidthRatio
     * 		With respect to height of timeText.
     * @param dateRange
     */
    public void setParams (double xPadRatio, double yPadRatio,
                           double timeTextWidthRatio, double timeTextHeightRatio, double dateTextHeightRatio,
                           double connectorWidthRatio, DateRange dateRange) {
        this.xPaddingRatio = xPadRatio;
        this.yPaddingRatio = yPadRatio;

        this.timeTextWidthRatio = timeTextWidthRatio;
        this.timeTextHeightRatio = timeTextHeightRatio;
        
        this.dateTextHeightRatio = dateTextHeightRatio;
        
        this.connectorWidthRatio = connectorWidthRatio;

        //Set the content for the date ranges.
        if ( dateRange != null ) {
            if ( dateRange.hasTime() ) {
            	//Check if the start and date are in the same year.
                Calendar start = Calendar.getInstance();
                start.setTime(dateRange.getStart());
                Calendar end = Calendar.getInstance();
                end.setTime(dateRange.getEnd());
                boolean sameYear = start.get(Calendar.YEAR) == end.get(Calendar.YEAR);

                //Show only a single date when start and end are exactly the same date.
                if ( dateRange.getStart().equals(dateRange.getEnd()) ) {
                    this.timeStart = DateFormats.timeFormat.format(dateRange.getStart());

                    this.dateStart = DateFormats.dateFormatMonth.format(dateRange.getStart());
                } else {
                    this.timeStart = DateFormats.timeFormat.format(dateRange.getStart());
                    this.timeEnd = DateFormats.timeFormat.format(dateRange.getEnd());

                    //Show the year if the years are different between the dates.
                    if ( sameYear ) {
                        this.dateStart = DateFormats.dateFormatMonth.format(dateRange.getStart());
                        this.dateEnd = DateFormats.dateFormatMonth.format(dateRange.getEnd());
                    } else {
                        this.dateStart = DateFormats.dateFormatYear.format(dateRange.getStart());
                        this.dateEnd = DateFormats.dateFormatYear.format(dateRange.getEnd());
                    }
                }
            } else {
            	if ( dateRange.getStart().equals(dateRange.getEnd()) ) {
                    this.timeStart = DateFormats.dateFormatMonth.format(dateRange.getStart());
                    this.dateStart = DATETEXT_NOTIME_START;
                } else {
                	this.dateStart = DATETEXT_NOTIME_START;
                	this.dateEnd = DATETEXT_NOTIME_END;
                    Calendar start = Calendar.getInstance();
                    start.setTime(dateRange.getStart());
                    Calendar end = Calendar.getInstance();
                    end.setTime(dateRange.getEnd());
                    boolean sameYear = start.get(Calendar.YEAR) == end.get(Calendar.YEAR);
                    
                    //For events without time show the date in place of the time.
                    if ( sameYear ) {
                        this.timeStart = DateFormats.dateFormatMonth.format(dateRange.getStart());
                        this.timeEnd = DateFormats.dateFormatMonth.format(dateRange.getEnd());
                    } else {
                        this.timeStart = DateFormats.dateFormatYear.format(dateRange.getStart());
                        this.timeEnd = DateFormats.dateFormatYear.format(dateRange.getEnd());
                    }
                }
            }
        }
    }
    
    public double getTimeTextHeightRatio () {
        return this.timeTextHeightRatio;
    }
    
    public double getDateTextHeightRatio() {
        return this.dateTextHeightRatio;
    }
    
    public void strikeout () {
        this.strikeout = !this.strikeout;
    }
    
    public void draw (GraphicsContext gc, double x, double y, double width, double height, 
                        Color backgroundColour, Color textColour) {
        if (gc == null) {
            System.out.println("Error");
        } else {
            double xCurrent = x;
            double yCurrent = y;

            double timeWidth = timeTextWidthRatio * width;
            double timeHeight = timeTextHeightRatio * height;

            double dateHeight = dateTextHeightRatio * timeHeight;

            double connectorWidth = connectorWidthRatio * timeWidth;
            double strikeoutWidth = timeWidth;

            Font dateFont = FontLoader.load( dateHeight - (yPaddingRatio * dateHeight * 2));
            Font timeFont = FontLoader.load( timeHeight - (yPaddingRatio * timeHeight * 2));

            if ( !this.dateStart.equals("") ) {
            	renderDate(gc, xCurrent, yCurrent, backgroundColour, dateFont, dateStart);
            }

            xCurrent += (timeWidth + connectorWidth);
            if ( !this.dateEnd.equals("") ) {
            	renderDate(gc, xCurrent, yCurrent, backgroundColour, dateFont, dateEnd);
            }

            xCurrent = x;
            yCurrent += dateHeight;
            if ( !this.timeStart.equals("") ) {
            	renderTime(gc, xCurrent, yCurrent, timeWidth, timeHeight,
            				backgroundColour, textColour, timeFont, timeStart);
            }

            xCurrent += timeWidth;
            if ( !this.timeEnd.equals("") ) {
                renderConnector(gc, xCurrent, yCurrent, connectorWidth, timeHeight, backgroundColour);

                xCurrent += connectorWidth;

                renderTime(gc, xCurrent, yCurrent, timeWidth, timeHeight,
        				backgroundColour, textColour, timeFont, timeEnd);
                
                strikeoutWidth += (timeWidth + connectorWidth);
            }

            xCurrent = x;
            yCurrent += (timeHeight * 0.5) - (DateRangeRenderer.STRIKEOUT_HEIGHT * 0.5);
            if ( this.strikeout ) {
            	renderStrikeout(gc, xCurrent, yCurrent, strikeoutWidth,
            					DateRangeRenderer.STRIKEOUT_HEIGHT, AppColours.important);
            }
        }
    }
    
    /**
     * Render the date for a given date range.
     * 
     * @param gc
     * @param xCurrent
     * @param yCurrent
     * @param colour
     * @param dateFont
     * @param text
     * 		Date to be displayed.
     */
    private void renderDate (GraphicsContext gc, double xCurrent, double yCurrent,
    							Color colour, Font dateFont, String text) {
    	gc.setFill(colour);
        gc.setTextAlign(TextAlignment.LEFT);
        gc.setFont(dateFont);
        gc.setTextBaseline(VPos.TOP);

        gc.fillText ( text, xCurrent, yCurrent );
    }
    
    /**
     * Render the time for a given date range.
     * @param gc
     * @param xCurrent
     * @param yCurrent
     * @param width
     * @param height
     * @param backgroundColour
     * @param textColour
     * @param timeFont
     * @param text
     */
    private void renderTime (GraphicsContext gc, double xCurrent, double yCurrent,
    							double width, double height,
    							Color backgroundColour, Color textColour, Font timeFont, String text) {
    	gc.setFill(backgroundColour);
        gc.fillRect(xCurrent, yCurrent, width, height);

        gc.setFill(textColour);
        gc.setTextAlign(TextAlignment.LEFT);
        gc.setFont(timeFont);
        gc.setTextBaseline(VPos.TOP);

        gc.fillText ( text, xCurrent + (xPaddingRatio * width), yCurrent );
    }
    
    /**
     * Render the connector between the start and end date of a dateRange.
     * 
     * @param gc
     * @param xCurrent
     * @param yCurrent
     * @param width
     * @param height
     * 		Height of the timeText.
     * @param backgroundColour
     */
    private void renderConnector (GraphicsContext gc, double xCurrent, double yCurrent,
									double width, double height, Color backgroundColour) {
    	gc.setFill(backgroundColour);
        gc.fillPolygon(new double[]{xCurrent - 1, xCurrent + width + 1, xCurrent - 1},
                        new double[]{yCurrent, yCurrent + (height * 0.5), yCurrent + height}, 3);
    }
    
    /**
     * Render the strikeout to be displayed when dateRanges have passed.
     * 
     * @param gc
     * @param xCurrent
     * @param yCurrent
     * @param width
     * @param height
     * @param backgroundColour
     */
    private void renderStrikeout (GraphicsContext gc, double xCurrent, double yCurrent,
									double width, double height, Color backgroundColour) {
    	gc.setFill(backgroundColour);
        gc.fillRect(xCurrent, yCurrent, width, height);
    }
}
