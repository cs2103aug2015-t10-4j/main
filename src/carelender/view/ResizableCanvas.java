package carelender.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Canvas to extend the resizable component of
 */
class ResizableCanvas extends Canvas {
    CanvasRenderer mainRenderer;
    CanvasRenderer popupRenderer;
    public ResizableCanvas() {
        // Redraw canvas when size changes.
        widthProperty().addListener(evt -> draw());
        heightProperty().addListener(evt -> draw());
    }

    private void draw() {
        double width = getWidth();
        double height = getHeight();
        if ( width == 0 || height == 0 ) {
            return;
        }
        GraphicsContext gc = getGraphicsContext2D();
        if ( mainRenderer == null ) {
            //Draw a red cross
            gc.setStroke(Color.RED);

            gc.clearRect(0, 0, width, height);
            gc.strokeLine(0, 0, width, height);
            gc.strokeLine(0, height, width, 0);
        } else {
            mainRenderer.draw(gc, 0, 0, width, height);
            popupRenderer.draw(gc, width/3.0, height/3.0, width/3.0, height/3.0);
        }

        //System.out.println(width + ", " + height);
    }

    public void setMainRenderer(CanvasRenderer renderer) {
        this.mainRenderer = renderer;
        draw();
    }

    public void setPopupRenderer(CanvasRenderer renderer) {
        this.popupRenderer = renderer;
        draw();
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