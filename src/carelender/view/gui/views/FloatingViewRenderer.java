package carelender.view.gui.views;

import carelender.controller.Controller;
import carelender.model.data.EventList;
import carelender.model.data.QueryList;
import carelender.model.strings.AppColours;
import carelender.view.gui.components.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;

public class FloatingViewRenderer extends CanvasRenderer {
    //CalenderRenderer
    //MessageboxRenderer
    //SelectionPopupRenderer

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

        //Todo: 20 -> meaningful expression
        double fontSize = width / 60.0; //Temporary
        Font font = Font.loadFont("file:res/monaco.ttf", fontSize);

        double windowPadding = 8;
        double textboxInnerPadding = 8;
        double topBarHeight = height * 0.13;
        double remainderHeight = height - topBarHeight;

        double announcementBoxY = topBarHeight + windowPadding;

        double leftColumnX = 0;

        double announcementHeight = fontSize + textboxInnerPadding * 2;
        double messageBoxHeight = fontSize + textboxInnerPadding * 2;
        double mainContentHeight = remainderHeight - announcementHeight - messageBoxHeight - windowPadding * 3;



        tab.draw(gc, 0, 0, width, topBarHeight, 2);

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

        tasks.draw(gc, leftColumnX, announcementBoxY + announcementHeight + windowPadding,
                width, mainContentHeight);

        messageBox.setParams(gc, leftColumnX, announcementBoxY + announcementHeight + windowPadding * 2 + mainContentHeight,
                width, messageBoxHeight,
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