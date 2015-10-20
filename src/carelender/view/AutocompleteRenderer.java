package carelender.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;

import java.text.ParseException;

/**
 * Renders the autocomplete box
 */
public class AutocompleteRenderer extends CanvasRenderer {

    String [] autocompleteOptions = null; //= {"test", "test2", "bubu", "lala", "test some very loing line of text lorem ipsum blah blahb labhbl ablhbl abhla hblahbl hba more text so much text text text text text"};
    @Override
    public void draw(GraphicsContext gc, double x, double y, double width, double height) {
        super.draw(gc, x, y, width,height);

        if ( autocompleteOptions != null ) {
            double fontSize = width / 60.0; //Temporary
            Font font = Font.loadFont("file:res/monaco.ttf", fontSize);

            double lineHeight = 0.05;
            double innerPadding = 5;
            double fieldHeight = autocompleteOptions.length * ( 1 + lineHeight ) * fontSize + innerPadding * 2;
            TextRenderer autoComplete = new TextRenderer(gc, x, y - fieldHeight, width, fieldHeight,
                    innerPadding, innerPadding, font, 0.6, lineHeight);

            for ( String autocompleteOption : autocompleteOptions ) {
                autoComplete.addTextEllipsis(autocompleteOption);
            }

            autoComplete.drawText("#e8e8e8");
        }
    }

    public void setAutocompleteOptions(String[] autocompleteOptions) {
        this.autocompleteOptions = autocompleteOptions;
        redraw();
    }
}
