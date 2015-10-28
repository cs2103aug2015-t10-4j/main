package carelender.view;

import carelender.model.data.EventList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class WeekViewRenderer extends CanvasRenderer {
	//TaskRenderer
	//MessageboxRenderer
	//SelectionPopupRenderer
	
	TextRenderer messageBox;
    TextRenderer announcementBox;
    AutocompleteRenderer autocompleteRenderer;

    private EventList listResults;
    private WeekRenderer weekView;
    private TaskRenderer taskView;
    
    String messageText;
    private TabRenderer tab;

    public WeekViewRenderer() {
        autocompleteRenderer = new AutocompleteRenderer();
        
        this.weekView = new WeekRenderer();
        this.weekView.setParams(10, 10, 50, 50);
        this.taskView = new TaskRenderer();
        this.taskView.setParams(10, 10, 0.7, 0.1, 0.2, 0.1);
        
        listResults = new EventList();
        announcementBox = new TextRenderer();
        messageBox = new TextRenderer();
        tab = new TabRenderer();
        
        messageText = "";
    }

    @Override
    public void draw( GraphicsContext gc, double x, double y, double width, double height ) {
    	super.draw(gc, 0, 0, width, height);

        //Todo: 20 -> meaningful expression
        double fontSize = width / 60.0; //Temporary
        Font font = Font.loadFont("file:res/monaco.ttf", fontSize);

        double windowPadding = 8;
        double textboxInnerPadding = 8;
        double topBarHeight = height * 0.13;
        double remainderHeight = height - topBarHeight;
        double announcementBoxH = remainderHeight * 0.3 - windowPadding;
        double messageBoxH = remainderHeight * 0.7 - windowPadding;

        double announcementBoxY = topBarHeight + windowPadding;
        double messageBoxY = announcementBoxY + announcementBoxH + windowPadding;

        double leftColumnWidth = 0.4 * width - windowPadding;
        double rightColumnWidth = 0.6 * width - 2*windowPadding;
        double leftColumnX = windowPadding;
        double rightColumnX = leftColumnX + leftColumnWidth + windowPadding;

		double calendarHeight = remainderHeight * 0.4 - windowPadding;
		double taskviewHeight = remainderHeight - calendarHeight - 2* windowPadding;


        tab.draw(gc, 0, 0, width, topBarHeight, 0 );

        announcementBox.setParams(gc, leftColumnX, announcementBoxY,
                leftColumnWidth, announcementBoxH, textboxInnerPadding, textboxInnerPadding,
                font, 0.6, 0.05);
        announcementBox.addText("This is a announcementRenderer.\n");
        announcementBox.drawText();

        messageBox.setParams(gc, rightColumnX, announcementBoxY, rightColumnWidth, calendarHeight,
				textboxInnerPadding, textboxInnerPadding,
                font, 0.6, 0.05);
        messageBox.addText(messageText);
        messageBox.drawText();

        this.taskView.draw(gc, leftColumnX, messageBoxY, leftColumnWidth, messageBoxH);
        this.weekView.draw(gc, rightColumnX, announcementBoxY + calendarHeight + windowPadding , rightColumnWidth, taskviewHeight);

        autocompleteRenderer.draw(gc, 0, height, width, 0);
    }

    public void setMessageBoxText(String text) {
    	if ( text == null ) {
    		messageText = "";
    	} else {
    		messageText = text;
    	}
    }

    public void setTaskview(EventList tasks) {
        this.listResults = tasks;
        this.taskView.clearEvents();
        this.taskView.addEvents(this.listResults);
        
        this.weekView.clear();
        this.weekView.addEvents(this.taskView.getDisplayList());
        redraw();
    }
    
    public void setWeekView(EventList tasks) {
        this.listResults = tasks;
        this.weekView.clear();
        this.weekView.addEvents(this.listResults);
        redraw();
    }

    public void setAutocompleteOptions ( String [] options ) {
        autocompleteRenderer.setAutocompleteOptions(options);
    }
}