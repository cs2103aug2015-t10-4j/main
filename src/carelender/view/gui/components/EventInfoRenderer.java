package carelender.view.gui.components;

import carelender.model.data.Event;
import carelender.model.strings.AppColours;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * Renders the information of an event using the taskbar renderer class
 */
public class EventInfoRenderer extends CanvasRenderer {
    Event event;
    TaskBarRenderer taskBarRenderer;
    public final float headerRatio = 0.06f;
    public EventInfoRenderer() {
        taskBarRenderer = new TaskBarRenderer();
        taskBarRenderer.setParams(10,10, 0.3);
    }

    public void setEvent(Event event) {
        this.event = event;
        taskBarRenderer.setContent(event);
    }
    @Override
    public void draw( GraphicsContext gc, double x, double y, double width, double height) {
        gc.setFill(AppColours.tasklistRowBackground);
        gc.fillRect(x, y, width, height);



        gc.setFill(AppColours.primaryColour);
        gc.fillRect(x, y, width, height * headerRatio);

        gc.setFill(AppColours.black);
        Font font = Font.loadFont("file:res/monaco.ttf", height * headerRatio * .8);
        gc.setFont(font);
        gc.setTextBaseline(VPos.TOP);
        gc.setTextAlign(TextAlignment.LEFT);
        gc.fillText("Event Details", x, y);

        taskBarRenderer.draw(gc, x, y + height * headerRatio, width, width / 10, AppColours.tasklistRowBackground, AppColours.primaryColour, false);
    }
}
