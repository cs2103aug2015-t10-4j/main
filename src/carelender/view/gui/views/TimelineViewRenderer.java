package carelender.view.gui.views;

import carelender.controller.Controller;
import carelender.model.data.EventList;
import carelender.model.strings.AppColours;
import carelender.model.strings.FontLoader;
import carelender.view.gui.components.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;

public class TimelineViewRenderer extends CanvasRenderer {
    private static final double TIMELINE_X_PADDING = 10;
    private static final double TIMELINE_Y_PADDING = 10;
    private static final double TIMELINE_BAR_WIDTH_RATIO = 0.07;
    private static final double TIMELINE_BAR_LABEL_RATIO = 0.07;

    private static final double TASK_X_PADDING = 15;
    private static final double TASK_Y_PADDING = 5;
    private static final double TASK_WIDTH_RATIO = 0.7;
    private static final double TASK_HEIGHT_RATIO = 0.1;
    private static final double DATE_WIDTH_RATIO = 0.2;
    private static final double DATE_HEIGHT_RATIO = 0.1;

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

        this.taskView = new TaskRenderer();

        
        listResults = new EventList();
        announcementBox = new TextRenderer();
        messageBox = new TextRenderer();
        tab = new TabRenderer();
        
        messageText = "";
    }

    @Override
    public void draw( GraphicsContext gc, double x, double y, double width, double height ) {
    	super.draw(gc, 0, 0, width, height);

        this.weekView.setParams(TIMELINE_X_PADDING, TIMELINE_Y_PADDING,
                                TIMELINE_BAR_WIDTH_RATIO * width, TIMELINE_BAR_LABEL_RATIO * width);
        this.taskView.setParams(TASK_X_PADDING, TASK_Y_PADDING, TASK_WIDTH_RATIO, TASK_HEIGHT_RATIO, DATE_WIDTH_RATIO, DATE_HEIGHT_RATIO);

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