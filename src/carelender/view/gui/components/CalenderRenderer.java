package carelender.view.gui.components;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import carelender.controller.HintGenerator;
import carelender.model.data.DateRange;
import carelender.model.data.Event;
import carelender.model.data.EventList;
import carelender.model.data.QueryList;
import carelender.model.strings.AppColours;
import carelender.model.strings.FontLoader;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * Renders the calendar view
 */
public class CalenderRenderer extends CanvasRenderer {
    private QueryList monthListQuery;

    final int NUMBER_OF_SQUARES = 42;
    final int NUMBER_OF_RANGES_PER_DAY = 3;
    final String [] DAYS = {"M", "T", "W", "T", "F", "S", "S"};

    private EventList monthEvents = null;
    private int[][] monthEventNumbers;
    private Date monthStartTime;
    private Date monthEndTime;

    private static final int NUMBER_OF_DAYS_PER_WEEK = 7;
    private static final int TIME_RANGE_UNIT = 8;
    private static final int NUMBER_OF_HOURS_PER_DAY = 24;

    private static final int ARC_THRESHOLD = 3;
    
    private static final double FIRST_FONTSIZE_RATIO = 0.5;
    private static final double SECOND_FONTSIZE_RATIO = 0.25;
    
    private static final double SCALED_OFFSETX_RATIO = 0.5;
    private static final double ASPECT = 9.0/9.5;

    private static final double CAL_CELL_SIDE_PADDING_RATIO = 0.025;
    private static final double CAL_CELL_USABLE_WIDTH_RATIO = 2.0;
    private static final double CAL_CELL_WIDTH_RATIO = 1.0 / 7.0;
    private static final double CAL_CELL_SPACING_RATIO = 0.1;
    private static final double CAL_CELL_HEIGHT_WIDTH_RATIO = 0.75;    
    private static final double CAL_CELL_SHADOW_OFFSET_RATIO = 0.7;
    
    //@@author A0133269A
    public CalenderRenderer() {        
        monthEventNumbers = new int[NUMBER_OF_SQUARES][NUMBER_OF_RANGES_PER_DAY];
        resetEventNumbers();
        
        monthListQuery = new QueryList();
        
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        monthStartTime = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, NUMBER_OF_SQUARES);
        monthEndTime = cal.getTime();

        monthListQuery.addSearchParam(QueryList.SearchParam.DATE_START, monthStartTime);
        monthListQuery.addSearchParam(QueryList.SearchParam.DATE_END, monthEndTime);
        refreshEventList();
    }

    public void refreshEventList() {
        monthEvents = monthListQuery.searchExecute();
        updateEventNumbers();
        HintGenerator.getInstance().setDailyEventNumbers(monthEventNumbers);
        System.out.println("CalendarRenderer refreshed: " + monthEvents.size() + " items in the month");
    }

    //@@author A0133907E
    double sidePadding;
    double usableWidth;
    double calCellWidth;
    double calCellHeight;
    double calCellSpacing;
    double calCellShadowOffset;

    double scaledWidth, scaledHeight;
    double offsetX, offsetY;

    @Override
    public void draw(GraphicsContext gc, double x, double y, double width, double height) {
        super.draw(gc, x, y, width, height);
        calculateScaledDimensions(width, height);
        calulateCellProperties();
        
        Calendar c = Calendar.getInstance();
        int todayDate = c.get(Calendar.DATE);
        int thisMonth = c.get(Calendar.MONTH);
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        gc.setFill(AppColours.panelBackground);
        gc.fillRect(x, y, width, height);

        Font font = FontLoader.load( calCellHeight * FIRST_FONTSIZE_RATIO);

        gc.setFill(AppColours.calendarCell);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(font);
        gc.setTextBaseline(VPos.TOP);
        for (int i = 0 ; i < NUMBER_OF_DAYS_PER_WEEK; i++ ) {
            double actualX = x + i * ( calCellWidth + calCellSpacing ) + sidePadding + offsetX + calCellWidth * 0.5;
            double actualY = y + offsetY + sidePadding;
            gc.fillText(DAYS[i], actualX, actualY);
        }

        font = FontLoader.load( calCellHeight * SECOND_FONTSIZE_RATIO);
        double monthSpacer = 0;
        for (int i = 0; i < NUMBER_OF_SQUARES; i++ ) {
            double actualX = x + i%NUMBER_OF_DAYS_PER_WEEK * ( calCellWidth + calCellSpacing ) + sidePadding;
            double actualY = y + (i/NUMBER_OF_DAYS_PER_WEEK) * ( calCellHeight + calCellSpacing ) + sidePadding;

            int date = c.get(Calendar.DATE);
            if ( i == 0 ) {
                monthSpacer += (calCellSpacing * 4) + sidePadding;
            } else if ( date == 1 ) {
                monthSpacer += (calCellHeight + calCellSpacing * 4) + sidePadding;
            }

            actualX += offsetX;
            actualY += offsetY + calCellHeight * 0.5 + monthSpacer;

            if ( date == 1 || i == 0 ) {
                gc.setFont(font);
                gc.setFill(Color.web("#BBB"));
                gc.fillRect(sidePadding + x + offsetX, actualY - calCellHeight * 0.6, scaledWidth - sidePadding * 2 - calCellShadowOffset, calCellHeight * 0.5);
                gc.setFill(Color.web("#000"));
                gc.setTextBaseline(VPos.TOP);
                gc.setTextAlign(TextAlignment.LEFT);

                gc.fillText(getMonth(c.get(Calendar.MONTH) ), sidePadding + x + offsetX + sidePadding, actualY - calCellHeight * 0.5);
            }

            int[] dailyEventNumbers = monthEventNumbers[i];

            Color calendarCell = AppColours.calendarCell;
            if ( c.get(Calendar.DATE) == todayDate && c.get(Calendar.MONTH) == thisMonth ) {
                calendarCell = AppColours.calendarTodayCell;
            }
            calendarSquare(actualX, actualY,
                    calCellWidth, calCellHeight,
                    calCellShadowOffset,
                    calendarCell, AppColours.primaryColour,
                    date + "", font, dailyEventNumbers);
            c.add(Calendar.DATE, 1);
            
        }
    }

    private void resetEventNumbers(){
        for(int i=0; i<NUMBER_OF_SQUARES; i++) {
            for (int j=0; j<NUMBER_OF_RANGES_PER_DAY; j++){
                monthEventNumbers[i][j] = 0;
            }
        }
    }
    
    private void updateEventNumbers() {
        resetEventNumbers();
        for (int i=0; i<monthEvents.size(); i++) {
            Event currentEvent = monthEvents.get(i);
            for (int j=0; j<currentEvent.getDateRange().length; j++) {
                DateRange currentRage = currentEvent.getDateRange()[j];
                Date taskStartTime = currentRage.getStart();
                Date taskEndTime = currentRage.getEnd();
                if (!(taskStartTime.after(monthEndTime) || taskEndTime.before(monthStartTime))) {
                    if (taskStartTime.before(monthStartTime)) {
                        taskStartTime = monthStartTime;
                    }
                    if (taskEndTime.after(monthEndTime)) {
                        taskEndTime = monthEndTime;
                    }

                    long offsetStartMilliseconds = taskStartTime.getTime() - monthStartTime.getTime();
                    long offsetStartDays = TimeUnit.MILLISECONDS.toDays(offsetStartMilliseconds);
                    long offsetStartHours = TimeUnit.MILLISECONDS.toHours(offsetStartMilliseconds) % (long) NUMBER_OF_HOURS_PER_DAY;
                    int offsetStartSlot = (int)offsetStartHours / TIME_RANGE_UNIT;
                    
                    long offsetEndMilliseconds = taskEndTime.getTime() - monthStartTime.getTime();
                    long offsetEndDays = TimeUnit.MILLISECONDS.toDays(offsetEndMilliseconds);
                    long offsetEndHours = TimeUnit.MILLISECONDS.toHours(offsetEndMilliseconds) % (long) NUMBER_OF_HOURS_PER_DAY;
                    int offsetEndSlot = (int)offsetEndHours / TIME_RANGE_UNIT;
                    
                    for(int t=(int)offsetStartDays; t<=(int)offsetEndDays; t++) {
                        if(t == (int) offsetStartDays && t == (int)offsetEndDays){
                            for(int a=offsetStartSlot; a<=offsetEndSlot; a++) {
                                monthEventNumbers[t][a]++;
                            }
                        } else if(t == (int) offsetStartDays){
                            for(int a=offsetStartSlot; a<NUMBER_OF_RANGES_PER_DAY; a++) {
                                monthEventNumbers[t][a]++;
                            }
                        } else if (t == (int)offsetEndDays){
                            for(int a=0; a<offsetEndSlot; a++) {
                                monthEventNumbers[t][a]++;
                            }
                        } else {
                            for(int a=0; a<NUMBER_OF_RANGES_PER_DAY; a++){
                                monthEventNumbers[t][a]++;
                            }
                        }
                    }
                }
            }
        }
    }
    
    //@@author A0133269A
    private String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month];
    }

    private void calculateScaledDimensions(double width, double height) {
       
        double squareHeight = height * ASPECT;

        if ( width > squareHeight ) { //Height is the constraint
            scaledWidth = height * ASPECT;
            scaledHeight = height;
        } else { //Width is the constraint
            scaledWidth = width;
            scaledHeight = width / ASPECT;
        }

        offsetX = (width - scaledWidth) * SCALED_OFFSETX_RATIO;
        offsetY = 0;//(height - scaledHeight) * 0.5;
    }

    private void calulateCellProperties() {
        sidePadding = scaledWidth * CAL_CELL_SIDE_PADDING_RATIO;
        usableWidth = scaledWidth - sidePadding * CAL_CELL_USABLE_WIDTH_RATIO; // Give 2.5% padding on each size
        calCellWidth = usableWidth * CAL_CELL_WIDTH_RATIO;
        calCellSpacing = calCellWidth * CAL_CELL_SPACING_RATIO; //Make spacing 10% of each cell size
        calCellWidth -= calCellSpacing;
        calCellHeight = calCellWidth * CAL_CELL_HEIGHT_WIDTH_RATIO;
        calCellShadowOffset = calCellSpacing * CAL_CELL_SHADOW_OFFSET_RATIO;
    }

    /**
     * Draws a calendar square with a drop shadow
     * @param x X position
     * @param y Y position
     * @param w Width
     * @param h Height
     * @param dropOffset Offset of drop shadow
     * @param background Colour of square
     * @param textColor Colour of text
     * @param text Text to show at bottom right
     * @param dailyEventNumbers Array of integers to show the dots
     */
    public void calendarSquare ( double x, double y, double w, double h, double dropOffset, Color background, Color textColor, String text, Font font, int[] dailyEventNumbers ) {
        gc.setFill(Color.web("#999"));
        gc.fillRect(x + dropOffset, y + dropOffset, w, h);

        gc.setFill(background);
        gc.fillRect(x, y, w, h);

        gc.setFill(textColor);
        gc.setTextAlign(TextAlignment.RIGHT);
        gc.setTextBaseline(VPos.BOTTOM);
        gc.setFont(font);
        gc.fillText(text, x + w - dropOffset * 0.5 , y + h - dropOffset * 0.5 );

        double x_offset;
        double y_offset;

        for (int i = 0; i < dailyEventNumbers.length; i++) {
            int numArc = dailyEventNumbers[i];

            if (numArc > ARC_THRESHOLD) {
                numArc = ARC_THRESHOLD;
            }
            for (int j=0; j<numArc; j++) {
                gc.setFill(AppColours.primaryColour);
                x_offset = x + w*((double) j)/10.0 + w*((double) j + 1.0)/20.0;
                y_offset = y + h*((double) i)/10.0 + h*((double) i + 1.0)/20.0;
                gc.fillRect(x_offset, y_offset, w/10.0, h/10.0);
            }
        }
    }
}
