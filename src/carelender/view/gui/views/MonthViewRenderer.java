//@@author A0133907E
package carelender.view.gui.views;

import carelender.controller.Controller;
import carelender.model.data.EventList;
import carelender.model.strings.AppColours;
import carelender.model.strings.FontLoader;
import carelender.view.gui.components.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;

import java.nio.file.FileSystemNotFoundException;

public class MonthViewRenderer extends CanvasRenderer {
    TextRenderer messageBox;
    TextRenderer announcementBox;
    //AutocompleteRenderer autocompleteRenderer;
    CalenderRenderer calender;

    private EventList listResults;
    private TaskRenderer tasks;

    String messageText;
    String announcementText;
    private TabRenderer tab;

    public MonthViewRenderer() {
        tasks = new TaskRenderer();
        tasks.setParams(15, 5, 0.7, 0.1, 0.2, 0.1);
        
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



    public void refreshData() {
        calender.refreshEventList();
    }
}