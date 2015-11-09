//@@author A0133269A
package carelender.view.gui;

import carelender.view.gui.components.AutocompleteRenderer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Used to render the popup and normal layers
 */
public class UIRenderer extends CanvasRenderer {
    private CanvasRenderer mainRenderer;
    private CanvasRenderer popupRenderer;
    AutocompleteRenderer autocompleteRenderer;
    public UIRenderer() {
        autocompleteRenderer = new AutocompleteRenderer();
    }

    @Override
    public void draw(GraphicsContext gc, double x, double y, double width, double height) {
        super.draw(gc, x, y, width, height);
        if ( mainRenderer != null ) {
            mainRenderer.draw(gc, 0, 0, width, height);
        }
        if ( popupRenderer != null ) {
            gc.setGlobalAlpha(0.4);
            gc.setFill(Color.web("#000"));
            gc.fillRect(x,y,width,height);
            gc.setGlobalAlpha(1);
            popupRenderer.draw(gc, width * 0.1, height * 0.1, width * 0.8, height * 0.8);
        }
        autocompleteRenderer.draw(gc, 0, height, width, 0);
    }

    public void setMainRenderer(CanvasRenderer mainRenderer) {
        this.mainRenderer = mainRenderer;
    }

    public void setPopupRenderer(CanvasRenderer popupRenderer) {
        this.popupRenderer = popupRenderer;
    }

    public void setAutocompleteOptions ( String [] options, boolean renderFirstLineBold ) {
        autocompleteRenderer.setAutocompleteOptions(options, renderFirstLineBold);
    }
}
