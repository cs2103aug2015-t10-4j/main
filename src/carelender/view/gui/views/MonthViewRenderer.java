package carelender.view.gui.views;

import carelender.controller.Controller;
import carelender.model.data.EventList;
import carelender.model.strings.AppColours;
import carelender.view.gui.components.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;

import java.nio.file.FileSystemNotFoundException;

public class MonthViewRenderer extends CanvasRenderer {
    //CalenderRenderer
    //MessageboxRenderer
    //SelectionPopupRenderer

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

        //Todo: 20 -> meaningful expression
        double fontSize = width / 60.0; //Temporary
        Font font = Font.loadFont("file:res/monaco.ttf", fontSize);

        double windowPadding = 8;
        double textboxInnerPadding = 8;
        double topBarHeight = height * 0.13;
        double remainderHeight = height - topBarHeight;

        double announcementBoxY = topBarHeight + windowPadding;

        double leftColumnWidth = 0.5 * width - windowPadding;
        double rightColumnWidth = 0.5 * width;
        double leftColumnX = 0;
        double rightColumnX = leftColumnX + leftColumnWidth + windowPadding;

        double announcementHeight = fontSize + textboxInnerPadding * 2;
        double messageBoxHeight = fontSize + textboxInnerPadding * 2;
        double mainContentHeight = remainderHeight - announcementHeight - messageBoxHeight - windowPadding * 3;

        tab.draw(gc, 0, 0, width, topBarHeight, 1);

        /* Todo
         * replace magic numbers;
         * create specific class for these renderers;
         */

        announcementBox.setParams(gc, leftColumnX, announcementBoxY,
                width, announcementHeight,
                textboxInnerPadding, textboxInnerPadding,
                font, 0.6, 0.05);
        announcementBox.addText(announcementText);
        announcementBox.drawText(AppColours.panelBackground, AppColours.panelText);

        calender.draw(gc, rightColumnX, announcementBoxY + announcementHeight + windowPadding,
                rightColumnWidth, mainContentHeight);
        tasks.draw(gc, leftColumnX, announcementBoxY + announcementHeight + windowPadding,
                leftColumnWidth, mainContentHeight);

        messageBox.setParams(gc, leftColumnX, announcementBoxY + announcementHeight + windowPadding*2 + mainContentHeight,
                width , messageBoxHeight,
                textboxInnerPadding, textboxInnerPadding,
                font, 0.6, 0.05);
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