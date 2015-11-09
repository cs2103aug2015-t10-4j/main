//@@author A0133269A
package carelender.view.gui.components;

import carelender.model.strings.AppColours;
import carelender.model.strings.FontLoader;
import carelender.view.gui.CanvasRenderer;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * Renders the autocomplete box and automatically resizes the height according to the number of lines.
 */
public class AutocompleteRenderer extends CanvasRenderer {
    public static final double FONT_RATIO = 1/80.0;
    public static final double HINT_FONT_RATIO = 1/90.0;
    public static final double INNER_PADDING = 5;

    public static final String TAB_TO_AUTOCOMPLETE = "TAB to autocomplete";
    String [] autocompleteOptions = null;
    TextRenderer autoComplete = new TextRenderer();
    boolean renderFirstLineBold;

    @Override
    public void draw(GraphicsContext gc, double x, double y, double width, double height) {
        super.draw(gc, x, y, width,height);

        if ( autocompleteOptions != null ) {
            double fontSize = width * FONT_RATIO;


            double fieldHeight = autocompleteOptions.length * ( 1 + FontLoader.DEFAULT_LINE_HEIGHT_RATIO) * fontSize + INNER_PADDING * 2;

            if ( renderFirstLineBold ) {
                gc.setFont(FontLoader.load(width * HINT_FONT_RATIO));
                gc.setTextBaseline(VPos.BOTTOM);
                gc.setFill(AppColours.grey);
                gc.setStroke(AppColours.grey);
                gc.setTextAlign(TextAlignment.LEFT);
                gc.fillText(TAB_TO_AUTOCOMPLETE, x + INNER_PADDING * 2, y - fieldHeight - INNER_PADDING);
                gc.strokeText(TAB_TO_AUTOCOMPLETE, x + INNER_PADDING * 2, y - fieldHeight - INNER_PADDING);
            }

            Font font = FontLoader.load( fontSize );
            autoComplete.setParams(gc, x, y - fieldHeight, width, fieldHeight,
                    INNER_PADDING, INNER_PADDING, font, FontLoader.DEFAULT_LINE_HEIGHT_RATIO);

            for ( String autocompleteOption : autocompleteOptions ) {
                autoComplete.addTextEllipsis(autocompleteOption);
            }

            autoComplete.drawText(AppColours.autocompleteBackground, AppColours.autocompleteText, renderFirstLineBold?1:0);
        }
    }

    /**
     * Sets the lines of text for the autocomplete options
     * @param autocompleteOptions String array for all the options to show
     * @param renderFirstLineBold Flag to bold the first line
     */
    public void setAutocompleteOptions(String[] autocompleteOptions, boolean renderFirstLineBold) {
        this.renderFirstLineBold = renderFirstLineBold;
        this.autocompleteOptions = autocompleteOptions;
        redraw();
    }
}
