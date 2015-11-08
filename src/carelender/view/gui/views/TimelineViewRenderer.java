package carelender.view.gui.views;

import carelender.controller.Controller;
import carelender.model.data.EventList;
import carelender.model.strings.AppColours;
import carelender.model.strings.FontLoader;
import carelender.view.gui.components.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;

public class TimelineViewRenderer extends CanvasRenderer {
	TextRenderer messageBox;
    TextRenderer announcementBox;

    private EventList listResults;
    private TimelineRenderer weekView;
    private TaskRenderer taskView;
    
    String messageText;
    String announcementText;
    private TabRenderer tab;

    public TimelineViewRenderer() {
        this.weekView = new TimelineRenderer();
        this.weekView.setParams(10, 10, 50, 50);
        this.taskView = new TaskRenderer();
        this.taskView.setParams(15, 5, 0.7, 0.1, 0.2, 0.1);
        
        listResults = new EventList();
        announcementBox = new TextRenderer();
        messageBox = new TextRenderer();
        tab = new TabRenderer();
        
        messageText = "";
    }

    @Override
    public void draw( GraphicsContext gc, double x, double y, double width, double height ) {
    	super.draw(gc, 0, 0, width, height);

        LayoutHelper.setParams(x,y,width,height);

        tab.draw(gc, 0, 0, width, LayoutHelper.getTopBarHeight(), TabRenderer.TIMELINE_INDEX);

        announcementBox.setParams(gc, LayoutHelper.getLeftColumnX(), LayoutHelper.getAnnouncementBoxY(),
                width, LayoutHelper.getAnnouncementHeight(),
                LayoutHelper.getTextboxInnerPadding(), LayoutHelper.getTextboxInnerPadding(),
                LayoutHelper.getFont(), FontLoader.DEFAULT_LINE_HEIGHT_RATIO);
        announcementBox.addText(announcementText);
        announcementBox.drawText(AppColours.panelBackground, AppColours.panelText);

        weekView.draw(gc, LayoutHelper.getRightColumnX(), LayoutHelper.getMainContentY(),
                LayoutHelper.getRightColumnWidth(), LayoutHelper.getMainContentHeight());
        taskView.draw(gc, LayoutHelper.getLeftColumnX(), LayoutHelper.getMainContentY(),
                LayoutHelper.getLeftColumnWidth(), LayoutHelper.getMainContentHeight());

        messageBox.setParams(gc, LayoutHelper.getLeftColumnX(), LayoutHelper.getMessageBoxY(),
                width , LayoutHelper.getMessageBoxHeight(),
                LayoutHelper.getTextboxInnerPadding(), LayoutHelper.getTextboxInnerPadding(),
                LayoutHelper.getFont(), FontLoader.DEFAULT_LINE_HEIGHT_RATIO);
        messageBox.addText(messageText);
        messageBox.drawText(AppColours.panelBackground, AppColours.panelText);

    }

    /**
    * Sets the text of the announcement box
    * @param text Text to set
    */
    public void setAnnouncementBoxText ( String text ) {
        if ( text != null ) {
            announcementText = text;
        }
    }

    /**
     * Sets the text for the message box
     * @param text Text to set
     */
    public void setMessageBoxText(String text) {
        if ( text != null ) {
            messageText = text;
        }
    }

    public TaskRenderer getTaskRenderer() {
        return taskView;
    }
    
    public TimelineRenderer getTimelineRenderer() {
        return weekView;
    }

    public void setTaskview(EventList tasks) {
        this.listResults = tasks;
        this.taskView.clearEvents();
        this.taskView.addEvents(this.listResults);
        
        this.weekView.clear();
        this.weekView.addEvents(this.taskView.getDisplayList());
        redraw();
    }

    /**
     * Sets the display list on the parser so it knows what the indices are
     */
    public void setTaskDisplayList() {
        Controller.setDisplayedList(taskView.getDisplayList());
    }


    public void setWeekView(EventList tasks) {
        this.listResults = tasks;
        this.weekView.clear();
        this.weekView.addEvents(this.listResults);
        redraw();
    }

    public void refreshData() {
        //weekView.redraw();
    }
}