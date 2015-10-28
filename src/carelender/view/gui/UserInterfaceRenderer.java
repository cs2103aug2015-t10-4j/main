package carelender.view.gui;

import carelender.view.gui.components.AutocompleteRenderer;
import carelender.view.gui.components.CanvasRenderer;
import javafx.scene.canvas.GraphicsContext;

/**
 * Used to render the popup and normal layers
 */
public class UserInterfaceRenderer extends CanvasRenderer {
    private CanvasRenderer mainRenderer;
    private CanvasRenderer popupRenderer;
    AutocompleteRenderer autocompleteRenderer;
    public UserInterfaceRenderer() {
        autocompleteRenderer = new AutocompleteRenderer();
    }
    @Override
    public void draw(GraphicsContext gc, double x, double y, double width, double height) {
        super.draw(gc, x, y, width, height);
        if ( mainRenderer != null ) {
            mainRenderer.draw(gc, 0, 0, width, height);
        }
        if ( popupRenderer != null ) {
            popupRenderer.draw(gc, width / 3.0, height / 3.0, width / 3.0, height / 3.0);
        }

        autocompleteRenderer.draw(gc, 0, height, width, 0);
    }

    public void setMainRenderer(CanvasRenderer mainRenderer) {
        this.mainRenderer = mainRenderer;
    }

    public void setPopupRenderer(CanvasRenderer popupRenderer) {
        this.popupRenderer = popupRenderer;
    }

    public void setAutocompleteOptions ( String [] options ) {
        autocompleteRenderer.setAutocompleteOptions(options);
    }
}
