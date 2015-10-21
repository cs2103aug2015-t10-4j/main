package carelender.view;

import javafx.scene.canvas.GraphicsContext;

import java.awt.*;

/**
 * Used to render the popup and normal layers
 */
public class UserInterfaceRenderer extends CanvasRenderer {
    private CanvasRenderer mainRenderer;
    private CanvasRenderer popupRenderer;
    @Override
    void draw(GraphicsContext gc, double x, double y, double width, double height) {
        super.draw(gc, x, y, width, height);
        if ( mainRenderer != null ) {
            mainRenderer.draw(gc, 0, 0, width, height);
        }
        if ( popupRenderer != null ) {
            popupRenderer.draw(gc, width / 3.0, height / 3.0, width / 3.0, height / 3.0);
        }
    }

    public void setMainRenderer(CanvasRenderer mainRenderer) {
        this.mainRenderer = mainRenderer;
    }

    public void setPopupRenderer(CanvasRenderer popupRenderer) {
        this.popupRenderer = popupRenderer;
    }
}
