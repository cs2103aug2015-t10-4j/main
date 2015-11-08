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
import carelender.view.gui.RenderHelper;
import carelender.view.gui.components.CanvasRenderer;
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

    int squaresToDraw;
    final String [] days = {"M", "T", "W", "T", "F", "S", "S"};

    private EventList monthEvents = null;
    private int[][] monthEventNumbers;
    private Date monthStartTime;
    private Date monthEndTime;
    
    public CalenderRenderer() {
        squaresToDraw = 6*7;
        
        monthEventNumbers = new int[squaresToDraw][3];
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
        cal.add(Calendar.DAY_OF_MONTH, squaresToDraw);
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
    
    double sidePadding;
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

        Font font = Font.loadFont("file:res/monaco.ttf", calCellHeight * 0.5);

        gc.setFill(AppColours.calendarCell);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(font);
        gc.setTextBaseline(VPos.TOP);
        for (int i = 0 ; i < 7; i++ ) {
            double actualX = x + i * ( calCellWidth + calCellSpacing ) + sidePadding + offsetX + calCellWidth * 0.5;
            double actualY = y + offsetY + sidePadding;
            gc.fillText(days[i], actualX, actualY);
        }

        font = Font.loadFont("file:res/monaco.ttf", calCellHeight / 4.0);
        double monthSpacer = 0;
        for (int i = 0; i < squaresToDraw; i++ ) {
            double actualX = x + i%7 * ( calCellWidth + calCellSpacing ) + sidePadding;
            double actualY = y + (i/7) * ( calCellHeight + calCellSpacing ) + sidePadding;

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
            RenderHelper.calendarSquare(gc, actualX, actualY,
                    calCellWidth, calCellHeight,
                    calCellShadowOffset,
                    calendarCell, AppColours.primaryColour,
                    date + "", font, dailyEventNumbers);
            c.add(Calendar.DATE, 1);
            
        }
    }

    private void resetEventNumbers(){
        for(int i=0; i<squaresToDraw; i++) {
            for (int j=0; j<3; j++){
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
                    long offsetStartHours = TimeUnit.MILLISECONDS.toHours(offsetStartMilliseconds) % (long) 24;
                    int offsetStartSlot = (int)offsetStartHours / 8;
                    
                    long offsetEndMilliseconds = taskEndTime.getTime() - monthStartTime.getTime();
                    long offsetEndDays = TimeUnit.MILLISECONDS.toDays(offsetEndMilliseconds);
                    long offsetEndHours = TimeUnit.MILLISECONDS.toHours(offsetEndMilliseconds) % (long) 24;
                    int offsetEndSlot = (int)offsetEndHours / 8;
                    
                    for(int t=(int)offsetStartDays; t<=(int)offsetEndDays; t++) {
                        if(t == (int) offsetStartDays && t == (int)offsetEndDays){
                            for(int a=offsetStartSlot; a<=offsetEndSlot; a++) {
                                monthEventNumbers[t][a]++;
                            }
                        } else if(t == (int) offsetStartDays){
                            for(int a=offsetStartSlot; a<3; a++) {
                                monthEventNumbers[t][a]++;
                            }
                        } else if (t == (int)offsetEndDays){
                            for(int a=0; a<offsetEndSlot; a++) {
                                monthEventNumbers[t][a]++;
                            }
                        } else {
                            for(int a=0; a<3; a++){
                                monthEventNumbers[t][a]++;
                            }
                        }
                    }
                }
            }
        }
    }
    
    private String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month];
    }

    private void calculateScaledDimensions(double width, double height) {
        double aspect = 9.0/9.5;
        double squareHeight = height * aspect;

        if ( width > squareHeight ) { //Height is the constraint
            scaledWidth = height * aspect;
            scaledHeight = height;
        } else { //Width is the constraint
            scaledWidth = width;
            scaledHeight = width / aspect;
        }

        offsetX = (width - scaledWidth) * 0.5;
        offsetY = 0;//(height - scaledHeight) * 0.5;
    }

    private void calulateCellProperties() {
        sidePadding = scaledWidth * 0.025;
        double usableWidth = scaledWidth - sidePadding * 2; // Give 2.5% padding on each size
        calCellWidth = usableWidth / 7.0;
        calCellSpacing = calCellWidth * 0.1; //Make spacing 10% of each cell size
        calCellWidth -= calCellSpacing;
        calCellHeight = calCellWidth * 0.75;
        calCellShadowOffset = calCellSpacing * 0.7;
    }
}
