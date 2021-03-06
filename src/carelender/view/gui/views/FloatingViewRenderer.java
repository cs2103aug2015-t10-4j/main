//@@author A0133269A
package carelender.view.gui.views;

import carelender.controller.Controller;
import carelender.model.data.EventList;
import carelender.model.data.QueryList;
import carelender.model.strings.AppColours;
import carelender.model.strings.FontLoader;
import carelender.view.gui.CanvasRenderer;
import carelender.view.gui.components.*;
import javafx.scene.canvas.GraphicsContext;

public class FloatingViewRenderer extends CanvasRenderer {
    TextRenderer messageBox;
    TextRenderer announcementBox;
    CalenderRenderer calender;

    private QueryList resultsList;
    private EventList listResults;
    private TaskRenderer tasks;

    String messageText, announcementText;
    private TabRenderer tab;

    public FloatingViewRenderer() {
        tasks = new TaskRenderer();
        tasks.setParams(10, 10, 0.7, 0.1, 0.2, 0.1);

        resultsList = new QueryList();
        resultsList.addSearchParam(QueryList.SearchParam.DATE_START, null);
        resultsList.addSearchParam(QueryList.SearchParam.DATE_END, null);
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


        tab.draw(gc, 0, 0, width, LayoutHelper.getTopBarHeight(), TabRenderer.FLOATING_INDEX);

        announcementBox.setParams(gc, LayoutHelper.getLeftColumnX(), LayoutHelper.getAnnouncementBoxY(),
                width, LayoutHelper.getAnnouncementHeight(),
                LayoutHelper.getTextboxInnerPadding(), LayoutHelper.getTextboxInnerPadding(),
                LayoutHelper.getFont(), FontLoader.DEFAULT_LINE_HEIGHT_RATIO);
        announcementBox.addText(announcementText);
        announcementBox.drawText(AppColours.panelBackground, AppColours.panelText);

        tasks.draw(gc, LayoutHelper.getLeftColumnX(), LayoutHelper.getMainContentY(),
                width, LayoutHelper.getMainContentHeight());

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
        return tasks;
    }

    public void setTaskview() {
        this.tasks.clearEvents();
        this.listResults = resultsList.searchExecute();
        this.tasks.addEvents(this.listResults);
        redraw();
    }

    /**
     * Sets the display list on the parser so it knows what the indices are
     */
    public void setTaskDisplayList() {
        Controller.setDisplayedList(tasks.getDisplayList());
    }



    public void refreshData() {
        calender.refreshEventList();
    }
}