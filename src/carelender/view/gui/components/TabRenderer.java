//@@author A0133907E
package carelender.view.gui.components;

import carelender.model.strings.AppColours;
import carelender.model.strings.FontLoader;
import carelender.view.gui.CanvasRenderer;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class TabRenderer extends CanvasRenderer {
    public static final int TIMELINE_INDEX = 0;
    public static final int CALENDER_INDEX = 1;
    public static final int FLOATING_INDEX = 2;
    public static final int SETTINGS_INDEX = 3;

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

    /**
     * Draws the tab view, but with an extra parameter that highlights the tab index
     * @param gc Graphics Context to draw to
     * @param x X position of drawable area
     * @param y Y position of drawable area
     * @param width Width of drawable area
     * @param height Height of drawable area
     * @param tab Tab index to highlight
     */
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