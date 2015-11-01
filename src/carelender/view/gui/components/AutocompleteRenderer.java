package carelender.view.gui.components;

import carelender.view.gui.components.CanvasRenderer;
import carelender.view.gui.components.TextRenderer;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.io.Console;

/**
 * Renders the autocomplete box
 */
public class AutocompleteRenderer extends CanvasRenderer {

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
                gc.setFont(Font.loadFont("file:res/monaco.ttf", fontSize*0.9));
                gc.setTextBaseline(VPos.BOTTOM);
                gc.setFill(Color.web("#555"));
                gc.setStroke(Color.web("#555"));
                gc.setTextAlign(TextAlignment.LEFT);
                gc.fillText("TAB to autocomplete", x + 10, y - fieldHeight - 5);
                gc.strokeText("TAB to autocomplete", x + 10, y - fieldHeight - 5);
            }

            Font font = Font.loadFont("file:res/monaco.ttf", fontSize);
            autoComplete.setParams(gc, x, y - fieldHeight, width, fieldHeight,
                    innerPadding, innerPadding, font, 0.6, lineHeight);

            for ( String autocompleteOption : autocompleteOptions ) {
                autoComplete.addTextEllipsis(autocompleteOption);
            }

            autoComplete.drawText("#e8e8e8", "#000", renderFirstLineBold?1:0);
        }
    }

    public void setAutocompleteOptions(String[] autocompleteOptions, boolean renderFirstLineBold) {
        this.renderFirstLineBold = renderFirstLineBold;
        this.autocompleteOptions = autocompleteOptions;
        redraw();
    }
}
