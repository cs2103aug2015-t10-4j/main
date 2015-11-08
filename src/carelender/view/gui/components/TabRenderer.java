package carelender.view.gui.components;

import carelender.model.strings.AppColours;
import carelender.model.strings.FontLoader;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class TabRenderer extends CanvasRenderer {
    final private String [] tabText = { "Timeline", "Calendar", "Floating", "Settings" };
    public TabRenderer() {

    }


    @Override
    public void draw(GraphicsContext gc, double x, double y, double width, double height) {
        draw(gc, x, y, width, height, -1);
    }

    public void draw(GraphicsContext gc, double x, double y, double width, double height, int tab) {
        super.draw(gc, x, y, width,height);

        double fontSizeRatio = 1.0/23.0;
        double fontSize = width * fontSizeRatio; //Temporary
        double textPos = width / 4.0;

        Font font = FontLoader.load( fontSize);

        gc.setFill(AppColours.tabBackground);
        gc.fillRect(x, y, width, height);

        gc.setTextBaseline(VPos.CENTER);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(font);

        for ( int i = 0 ; i < tabText.length; i++ ) {
            double xPos = x + (i) * textPos + textPos * 0.5;
            if ( i == tab ) {
                gc.setFill(AppColours.tabHighlight);
            } else {
                gc.setFill(AppColours.tabText);
            }
            gc.fillText(tabText[i], xPos, height * 0.5 );

        }
    }
}