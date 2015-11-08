//@@author A0133269A
package carelender.view.gui.components;

import carelender.model.strings.AppColours;
import carelender.model.strings.FontLoader;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * Renders the autocomplete box and automatically resizes the height according to the number of lines.
 */
public class AutocompleteRenderer extends CanvasRenderer {
    public static final String TAB_TO_AUTOCOMPLETE = "TAB to autocomplete";
    String [] autocompleteOptions = null; // = {"test", "test2", "bubu", "lala", "test some very loing line of text lorem ipsum blah blahb labhbl ablhbl abhla hblahbl hba more text so much text text text text text"};
    TextRenderer autoComplete = new TextRenderer();
    boolean renderFirstLineBold;

    @Override
    public void draw(GraphicsContext gc, double x, double y, double width, double height) {
        super.draw(gc, x, y, width,height);

        if ( autocompleteOptions != null ) {
            double fontSize = width / 60.0; //Temporary

            double lineHeight = 0.05;
            double innerPadding = 5;
            double fieldHeight = autocompleteOptions.length * ( 1 + lineHeight ) * fontSize + innerPadding * 2;

            if ( renderFirstLineBold ) {
                gc.setFont(FontLoader.load( fontSize*0.9));
                gc.setTextBaseline(VPos.BOTTOM);
                gc.setFill(AppColours.grey);
                gc.setStroke(AppColours.grey);
                gc.setTextAlign(TextAlignment.LEFT);
                gc.fillText(TAB_TO_AUTOCOMPLETE, x + 10, y - fieldHeight - 5);
                gc.strokeText(TAB_TO_AUTOCOMPLETE, x + 10, y - fieldHeight - 5);
            }

            Font font = FontLoader.load( fontSize);
            autoComplete.setParams(gc, x, y - fieldHeight, width, fieldHeight,
                    innerPadding, innerPadding, font, 0.6, lineHeight);

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
