//@@author A0133907E
package carelender.view.gui.views;

import carelender.controller.Controller;
import carelender.model.data.EventList;
import carelender.model.strings.AppColours;
import carelender.model.strings.FontLoader;
import carelender.view.gui.CanvasRenderer;
import carelender.view.gui.components.*;
import javafx.scene.canvas.GraphicsContext;

public class MonthViewRenderer extends CanvasRenderer {
    TextRenderer messageBox;
    TextRenderer announcementBox;
    //AutocompleteRenderer autocompleteRenderer;
    CalenderRenderer calender;

    private EventList listResults;
    private TaskRenderer tasks;

    private static final double TASK_XPAD = 15;
    private static final double TASK_YPAD = 5;
    private static final double TASK_WITTH_RATIO = 0.7;
    private static final double TASK_HEIGTH_RATIO = 0.1;
    private static final double DATE_WITTH_RATIO = 0.2;
    private static final double DATE_HEIGTH_RATIO = 0.1;

    String messageText;
    String announcementText;
    private TabRenderer tab;

    public MonthViewRenderer() {
        tasks = new TaskRenderer();
        tasks.setParams(TASK_XPAD, TASK_YPAD, TASK_WITTH_RATIO, TASK_HEIGTH_RATIO, DATE_WITTH_RATIO, DATE_HEIGTH_RATIO);
        
        listResults = new EventList();
        calender = new CalenderRenderer();
        announcementBox = new TextRenderer();
        messageBox = new TextRenderer();
        tab = new TabRenderer();
    }

    @Override
    public void draw( GraphicsContext gc, double x, double y, double width, double height ) {
        super.draw(gc, 0, 0, width, height);
        LayoutHelper.setParams(x,y,width,height);

        tab.draw(gc, 0, 0, width, LayoutHelper.getTopBarHeight(), TabRenderer.CALENDER_INDEX);

        announcementBox.setParams(gc, LayoutHelper.getLeftColumnX(), LayoutHelper.getAnnouncementBoxY(),
                width, LayoutHelper.getAnnouncementHeight(),
                LayoutHelper.getTextboxInnerPadding(), LayoutHelper.getTextboxInnerPadding(),
                LayoutHelper.getFont(), FontLoader.DEFAULT_LINE_HEIGHT_RATIO);
        announcementBox.addText(announcementText);
        announcementBox.drawText(AppColours.panelBackground, AppColours.panelText);

        calender.draw(gc, LayoutHelper.getRightColumnX(), LayoutHelper.getMainContentY(),
                LayoutHelper.getRightColumnWidth(), LayoutHelper.getMainContentHeight());
        tasks.draw(gc, LayoutHelper.getLeftColumnX(), LayoutHelper.getMainContentY(),
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

    public void setTaskview(EventList tasks) {
        this.listResults = tasks;
        this.tasks.clearEvents();
        this.tasks.addEvents(this.listResults);
        redraw();
    }

    /**
     * Sets the display list on the parser so it knows what the indices are
     */
    public void setTaskDisplayList() {
        Controller.setDisplayedList(tasks.getDisplayList());
    }

    public TaskRenderer getTaskRenderer() {
        return tasks;
    }

    /**
     * Refresh the data stored in eventlist
     */
    public void refreshData() {
        calender.refreshEventList();
    }
}