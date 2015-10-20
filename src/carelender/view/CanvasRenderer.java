package carelender.view;

import javafx.scene.canvas.GraphicsContext;

/**
 * This is the renderer class that the canvas class will use to draw.
 * It has to be extended and the child class should override draw to do the drawing
 */
public abstract class CanvasRenderer {
    GraphicsContext gc = null;
    double x = 0;
    double y = 0;
    double width = 0;
    double height = 0;

    public CanvasRenderer() {}

    void draw ( GraphicsContext gc, double x, double y, double width, double height ) {
        this.gc = gc;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        gc.clearRect(x, y, width, height);
    }
    void redraw() {
        if ( gc == null ) {
            return;
        }
        draw(gc, x, y, width,height);
    }
}
