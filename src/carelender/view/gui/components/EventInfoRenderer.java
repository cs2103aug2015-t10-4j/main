package carelender.view.gui.components;

import carelender.model.data.Event;
import carelender.model.strings.AppColours;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;

/**
 * Renders the information of an event using the taskbar renderer class
 */
public class EventInfoRenderer extends CanvasRenderer {
    Event event;
    TaskBarRenderer taskBarRenderer;
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
        gc.setFill( AppColours.tasklistRowBackground);
        gc.fillRect(x, y, width, height);

        taskBarRenderer.draw(gc, x, y, width, width/10, AppColours.tasklistRowBackground, AppColours.primaryColour, false);
    }
}
