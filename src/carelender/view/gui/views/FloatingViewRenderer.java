package carelender.view.gui.views;

import carelender.model.data.EventList;
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

    private EventList listResults;
    private TaskRenderer tasks;

    String messageText;
    private TabRenderer tab;

    public FloatingViewRenderer() {
        tasks = new TaskRenderer();
        tasks.setParams(10, 10, 0.7, 0.1, 0.2, 0.1);

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


        tab.draw(gc, 0, 0, width, topBarHeight, 2 );

        /* Todo
         * replace magic numbers;
         * create specific class for these renderers;
         */

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

        calender.draw(gc, rightColumnX, announcementBoxY + calendarHeight + windowPadding , rightColumnWidth, taskviewHeight);
        tasks.draw(gc, leftColumnX, messageBoxY, leftColumnWidth, messageBoxH);


    }

    public void setMessageBoxText(String text) {
        messageText = text;
    }

    public void setTaskview(EventList tasks) {
        this.listResults = tasks;
        this.tasks.clearEvents();
        this.tasks.addEvents(this.listResults);
        redraw();
    }



    public void refreshData() {
        calender.refreshEventList();
    }
}