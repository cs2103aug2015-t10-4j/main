package carelender.view.gui.components;

import carelender.model.strings.AppColours;
import carelender.model.strings.FontLoader;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class TabRenderer extends CanvasRenderer {
    final private String [] tabText = { "Timeline", "Calendar", "Floating", "Settings" };
    private static final double FONT_SIZE_RATIO = 1.0/23.0;
    private static final double TEXT_POS_RATIO = 1.0/4.0;
    private static final double TEXT_POS_X_OFFSET_RATIO = 0.5;
    private static final double TEXT_POS_Y_OFFSET_RATIO = 0.5;
    public TabRenderer() {
    }

    @Override
    public void draw(GraphicsContext gc, double x, double y, double width, double height) {
        draw(gc, x, y, width, height, -1);
    }

    public void draw(GraphicsContext gc, double x, double y, double width, double height, int tab) {
        super.draw(gc, x, y, width,height);

        double fontSize = width * FONT_SIZE_RATIO;
        double textPos = width * TEXT_POS_RATIO;
        Font font = FontLoader.load( fontSize);

        gc.setFill(AppColours.tabBackground);
        gc.fillRect(x, y, width, height);

        gc.setTextBaseline(VPos.CENTER);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(font);

        for (int i = 0 ; i < tabText.length; i++) {
            double xPos = x + (i) * textPos + textPos * TEXT_POS_X_OFFSET_RATIO;
            if (i == tab) {
                gc.setFill(AppColours.tabHighlight);
            } else {
                gc.setFill(AppColours.tabText);
            }
            gc.fillText(tabText[i], xPos, height * TEXT_POS_Y_OFFSET_RATIO);
        }
    }
}