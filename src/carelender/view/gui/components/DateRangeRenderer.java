package carelender.view.gui.components;

import carelender.model.data.DateRange;
import carelender.model.data.Event;
import carelender.model.strings.AppColours;
import carelender.model.strings.DateFormats;
import carelender.model.strings.FontLoader;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * This class contains static methods to help to render the calendar view
 */
public class DateRangeRenderer {
    private static final double STRIKEOUT_WIDTH = 2;

    private double xPaddingRatio;
    private double yPaddingRatio;

    private double timeTextWidthRatio;
    private double timeTextHeightRatio;
    private double dateTextWidthRatio;
    private double dateTextHeightRatio;
    
    private double connectorWidthRatio;

    private String timeStart = "";
    private String timeEnd = "";
    private String dateStart = "";
    private String dateEnd = "";
    
    private boolean strikeout = false;

    public DateRangeRenderer() {
        this.timeStart = "";
        this.timeEnd = "";
        this.dateStart = "";
        this.dateEnd = "";
        
        this.strikeout = false;
    }
    
    public void setParams (double xPadRatio, double yPadRatio,
                           double timeTextWidthRatio, double timeTextHeightRatio, 
                           double dateTextWidthRatio, double dateTextHeightRatio,
                           double connectorWidthRatio, DateRange dateRange) {
        this.xPaddingRatio = xPadRatio;
        this.yPaddingRatio = yPadRatio;

        this.timeTextWidthRatio = timeTextWidthRatio;
        this.timeTextHeightRatio = timeTextHeightRatio;
        
        this.dateTextWidthRatio = dateTextWidthRatio;
        this.dateTextHeightRatio = dateTextHeightRatio;
        
        this.connectorWidthRatio = connectorWidthRatio;

        if ( dateRange != null ) {
            if ( dateRange.hasTime() ) {
                Calendar start = Calendar.getInstance();
                start.setTime(dateRange.getStart());
                Calendar end = Calendar.getInstance();
                end.setTime(dateRange.getEnd());
                boolean sameYear = start.get(Calendar.YEAR) == end.get(Calendar.YEAR);

                if ( dateRange.getStart().equals(dateRange.getEnd()) ) {
                    this.timeStart = DateFormats.timeFormat.format(dateRange.getStart());

                    this.dateStart = DateFormats.dateFormat.format(dateRange.getStart());
                } else {
                    this.timeStart = DateFormats.timeFormat.format(dateRange.getStart());
                    this.timeEnd = DateFormats.timeFormat.format(dateRange.getEnd());

                    if ( sameYear ) {
                        this.dateStart = DateFormats.dateFormat.format(dateRange.getStart());
                        this.dateEnd = DateFormats.dateFormat.format(dateRange.getEnd());
                    } else {
                        this.dateStart = DateFormats.dateFormatYear.format(dateRange.getStart());
                        this.dateEnd = DateFormats.dateFormatYear.format(dateRange.getEnd());
                    }
                }
            } else {
            	if ( dateRange.getStart().equals(dateRange.getEnd()) ) {
                    this.timeStart = DateFormats.dateFormat.format(dateRange.getStart());
                    this.dateStart = "Starts";
                } else {
                	this.dateStart = "Starts";
                	this.dateEnd = "Ends";
                    Calendar start = Calendar.getInstance();
                    start.setTime(dateRange.getStart());
                    Calendar end = Calendar.getInstance();
                    end.setTime(dateRange.getEnd());
                    boolean sameYear = start.get(Calendar.YEAR) == end.get(Calendar.YEAR);
                    
                    if ( sameYear ) {
                        this.timeStart = DateFormats.dateFormat.format(dateRange.getStart());
                        this.timeEnd = DateFormats.dateFormat.format(dateRange.getEnd());
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

            double timeWidth = this.timeTextWidthRatio * width;
            double timeHeight = this.timeTextHeightRatio * height;

            double dateHeight = this.dateTextHeightRatio * timeHeight;

            double connectorWidth = this.connectorWidthRatio * timeWidth;
            double strikeoutWidth = timeWidth;

            Font dateFont = FontLoader.load( dateHeight - (this.yPaddingRatio * dateHeight * 2));
            Font timeFont = FontLoader.load( timeHeight - (this.yPaddingRatio * timeHeight * 2));

            if ( !this.dateStart.equals("") ) {

                gc.setFill(backgroundColour);
                gc.setTextAlign(TextAlignment.LEFT);
                gc.setFont(dateFont);
                gc.setTextBaseline(VPos.TOP);

                gc.fillText ( this.dateStart, xCurrent, yCurrent );
            }

            xCurrent += (timeWidth + connectorWidth);
            if ( !this.dateEnd.equals("") ) {

                gc.setFill(backgroundColour);
                gc.setTextAlign(TextAlignment.LEFT);
                gc.setFont(dateFont);
                gc.setTextBaseline(VPos.TOP);

                gc.fillText ( this.dateEnd, xCurrent, yCurrent );
            }

            xCurrent = x;
            yCurrent += dateHeight;
            if ( !this.timeStart.equals("") ) {
                gc.setFill(backgroundColour);
                gc.fillRect(xCurrent, yCurrent, timeWidth, timeHeight);

                gc.setFill(textColour);
                gc.setTextAlign(TextAlignment.LEFT);
                gc.setFont(timeFont);
                gc.setTextBaseline(VPos.TOP);

                gc.fillText ( this.timeStart, xCurrent + this.xPaddingRatio * timeWidth,
                            yCurrent );
            }

            xCurrent += timeWidth;
            if ( !this.timeEnd.equals("") ) {
                gc.setFill(backgroundColour);
                gc.fillPolygon(new double[]{xCurrent - 1, xCurrent + connectorWidth + 1, xCurrent - 1},
                                new double[]{yCurrent, yCurrent + (timeHeight * 0.5), yCurrent + timeHeight}, 3);

                xCurrent += connectorWidth;

                gc.fillRect(xCurrent, yCurrent, timeWidth, timeHeight);

                gc.setFill(textColour);
                gc.setTextAlign(TextAlignment.LEFT);
                gc.setFont(timeFont);
                gc.setTextBaseline(VPos.TOP);

                gc.fillText ( this.timeEnd, xCurrent + this.xPaddingRatio * timeWidth,
                            yCurrent );
                
                strikeoutWidth += (timeWidth + connectorWidth);
            }

            xCurrent = x;
            yCurrent += (timeHeight * 0.5) - (DateRangeRenderer.STRIKEOUT_WIDTH * 0.5);
            if ( this.strikeout ) {
                gc.setFill(AppColours.important);
                gc.fillRect(xCurrent, yCurrent, strikeoutWidth, DateRangeRenderer.STRIKEOUT_WIDTH);
            }
        }
    }
}
