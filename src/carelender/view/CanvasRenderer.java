package carelender.view;

import javafx.scene.canvas.GraphicsContext;

/**
 * This is the renderer class that the canvas class will use to draw.
 * It has to be extended and the child class should override draw to do the drawing
 */
public abstract class CanvasRenderer {
    GraphicsContext gc = null;
    double width = 0;
    double height = 0;

    public CanvasRenderer() {}
    void draw ( GraphicsContext gc, double width, double height ) {
        this.gc = gc;
        this.width = width;
        this.height = height;
        gc.clearRect(0, 0, width, height);
    }
    void redraw() {
        if ( gc == null ) {
            return;
        }
        draw(gc,width,height);
    }
}
