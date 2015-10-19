package carelender.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Canvas to extend the resizable component of
 */
class ResizableCanvas extends Canvas {
    CanvasRenderer renderer;
    public ResizableCanvas() {
        // Redraw canvas when size changes.
        widthProperty().addListener(evt -> draw());
        heightProperty().addListener(evt -> draw());
    }

    private void draw() {
        double width = getWidth();
        double height = getHeight();
        GraphicsContext gc = getGraphicsContext2D();
        if ( renderer == null ) {
            //Draw a red cross
            gc.setStroke(Color.RED);

            gc.clearRect(0, 0, width, height);
            gc.strokeLine(0, 0, width, height);
            gc.strokeLine(0, height, width, 0);
        } else {
            renderer.draw(gc, 0, 0, width, height);
        }

        //System.out.println(width + ", " + height);
    }

    public void setRenderer(CanvasRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public double prefWidth(double height) {
        return height;
        //return getWidth();
    }

    @Override
    public double prefHeight(double width) {
        return width;
        //return getHeight();
    }
}