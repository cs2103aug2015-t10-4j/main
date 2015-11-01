package carelender.view.gui.components;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import carelender.controller.HintGenerator;
import carelender.model.data.DateRange;
import carelender.model.data.Event;
import carelender.model.data.EventList;
import carelender.model.data.QueryList;
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

    int squaresToDraw; //Temp, testing purposes only
    final String [] days = {"M", "T", "W", "T", "F", "S", "S"};

    private EventList monthEvents = null;
    private int[][] monthEventNumbers;
    private Date monthStartTime;
    private Date monthEndTime;
    
    public CalenderRenderer() {
        squaresToDraw = 4*7;
        
        monthEventNumbers = new int[squaresToDraw][3];
        resetEventNumbers();
        
        monthListQuery = new QueryList();
        
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR, 0);
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        monthStartTime = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, squaresToDraw);
        monthEndTime = cal.getTime();

        monthListQuery.addSearchParam(QueryList.SearchParam.DATE_START, monthStartTime);
        monthListQuery.addSearchParam(QueryList.SearchParam.DATE_END, monthEndTime);
        refreshEventList();
    }
    
    private void resetEventNumbers(){
    	for(int i=0; i<squaresToDraw; i++) {
        	for (int j=0; j<3; j++){
        		monthEventNumbers[i][j] = 0;
        	}
        }
    }

    public void refreshEventList() {
        monthEvents = monthListQuery.searchExecute();
        HintGenerator.getInstance().setEventList(monthEvents);
        updateEventNumbers();
        HintGenerator.getInstance().setDailyEventNumbers(monthEventNumbers);
        System.out.println("CalendarRenderer refreshed: " + monthEvents.size() + " items in the month");
    }
    
    private void updateEventNumbers() {
    	resetEventNumbers();
    	for (int i=0; i<monthEvents.size(); i++) {
    		Event currentEvent = monthEvents.get(i);
    		for (int j=0; j<currentEvent.getDateRange().length; j++) {
    			DateRange currentRage = currentEvent.getDateRange()[j];
    			Date taskStartTime = currentRage.getStart();
    			//System.out.println("start time of the event " + taskStartTime);
    			Date taskEndTime = currentRage.getEnd();
    			//System.out.println("end time of the event " + taskEndTime);
    			if (!(taskStartTime.after(monthEndTime) || taskEndTime.before(monthStartTime))) {
                    //System.out.println("In range");
        			if (taskStartTime.before(monthStartTime)) {
        				taskStartTime = monthStartTime;
        			}
        			if (taskEndTime.after(monthEndTime)) {
        				taskEndTime = monthEndTime;
        			}

        			long offsetStartMilliseconds = taskStartTime.getTime() - monthStartTime.getTime();
        			long offsetStartDays = TimeUnit.MILLISECONDS.toDays(offsetStartMilliseconds);
                    //System.out.println("StartTime is away from the first day by " + offsetStartDays + " days");
        			long offsetStartHours = TimeUnit.MILLISECONDS.toHours(offsetStartMilliseconds) % (long) 24;
                    //System.out.println("StartTime is away from the first day by " + TimeUnit.MILLISECONDS.toHours(offsetStartMilliseconds) + " hours");
        			//System.out.println("It starts at " + offsetStartHours + " of that day");
                    int offsetStartSlot = (int)offsetStartHours / 8;
                    //System.out.println("It should fill in the " + offsetStartSlot + " slot");
        			
                    long offsetEndMilliseconds = taskEndTime.getTime() - monthStartTime.getTime();
        			long offsetEndDays = TimeUnit.MILLISECONDS.toDays(offsetEndMilliseconds);
                    //System.out.println("EndTime is away from the first day by " + offsetEndDays + " days");
        			long offsetEndHours = TimeUnit.MILLISECONDS.toHours(offsetEndMilliseconds) % (long) 24;
                    //System.out.println("EndTime is away from the first day by " + TimeUnit.MILLISECONDS.toHours(offsetEndMilliseconds) + " hours");
                    //System.out.println("It ends at " + offsetEndHours + " of that day");
        			int offsetEndSlot = (int)offsetEndHours / 8;
                    //System.out.println("It should fill in the " + offsetEndSlot + " slot");
        			
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
    	//drawEventArray();
    }
    
    private void drawEventArray(){
    	for(int i=0; i<28; i++){
    		System.out.println("Day " + i + " [" + monthEventNumbers[i][0] + "]" + " [" + monthEventNumbers[i][1] + "]" + " [" + monthEventNumbers[i][2] + "]\n");
    	}
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
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY );

        gc.strokeRect(x, y, width, height);

        Font font = Font.loadFont("file:res/monaco.ttf", calCellHeight * 0.5);

        gc.setFill(Color.web("556370"));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(font);
        gc.setTextBaseline(VPos.TOP);
        for (int i = 0 ; i < 7; i++ ) {
            double actualX = x + i * ( calCellWidth + calCellSpacing ) + sidePadding + offsetX + calCellWidth * 0.5;
            double actualY = y + offsetY;
            gc.fillText(days[i], actualX, actualY);
        }

        font = Font.loadFont("file:res/monaco.ttf", calCellHeight / 4.0);
        for (int i = 0; i < squaresToDraw; i++ ) {
            double actualX = x + i%7 * ( calCellWidth + calCellSpacing ) + sidePadding;
            double actualY = y + (i/7) * ( calCellHeight + calCellSpacing ) + sidePadding;
            actualX += offsetX;
            actualY += offsetY + calCellHeight * 0.5;

            String month = "";
            int date = c.get(Calendar.DATE);
            System.out.println("The date I get is " + date);
            if (date == 1) {
                month = (c.get(Calendar.MONTH) + 1) + "/";
            }
            int[] dailyEventNumbers = monthEventNumbers[i];

            RenderHelper.calendarSquare(gc, actualX, actualY,
                    calCellWidth, calCellHeight,
                    calCellShadowOffset, "556370", month + date, font, dailyEventNumbers);
            c.add(Calendar.DATE, 1);
            
        }

        /*
        TextRenderer textTest = new TextRenderer (gc, sidePadding + offsetX, sidePadding + offsetY,
                    scaledWidth * 0.6 , scaledHeight * 0.6 , 10, 10,
                font, calCellHeight / 3.0, calCellHeight / 6.0, 0 );

        textTest.addText("This is a test string for like, stuff and stuff.\n");
        textTest.addText("Give me the thing that I love.\n");
        textTest.addText("aaaaaaaaaaaaaaaaaaaaaaa\n");
        textTest.addText("Do I really wrap? Is this how a burrito feels like. The twice fried beans, the painted faces.\n");
        
        textTest.drawText();
        */
    }

    private void calculateScaledDimensions(double width, double height) {
        double aspect = 16.0/9.0;
        double squareHeight = height * aspect;

        if ( width > squareHeight ) { //Height is the constraint
            scaledWidth = height * aspect;
            scaledHeight = height;
            //System.out.println("Height constraint");
        } else { //Width is the constraint
            scaledWidth = width;
            scaledHeight = width / aspect;
            //System.out.println("Width constraint");
        }

        //System.out.println("Width : " + scaledWidth + "/" + width);
        //System.out.println("Height: " + scaledHeight + "/" + height);

        offsetX = (width - scaledWidth) * 0.5;
        offsetY = (height - scaledHeight) * 0.5;
    }

    private void calulateCellProperties() {
        sidePadding = scaledWidth * 0.025;
        double usableWidth = scaledWidth - sidePadding * 2; // Give 2.5% padding on each size
        calCellWidth = usableWidth / 7.0;
        calCellSpacing = calCellWidth * 0.1; //Make spacing 10% of each cell size
        calCellWidth -= calCellSpacing;
        calCellHeight = calCellWidth * 0.88;
        calCellShadowOffset = calCellSpacing * 0.7;
    }

    private void redCross() {
        gc.setStroke(Color.RED);

        gc.clearRect(0, 0, width, height);
        gc.strokeLine(0, 0, width, height);
        gc.strokeLine(0, height, width, 0);
    }

    public void increment() {
        squaresToDraw++;
    }
}
