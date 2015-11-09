//@@author A0133907E
package carelender.view.gui.views;

import carelender.model.strings.AppColours;
import carelender.model.strings.FontLoader;
import carelender.view.gui.components.CanvasRenderer;
import carelender.view.gui.components.TabRenderer;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;

public class SettingViewRenderer extends CanvasRenderer {
    TabRenderer tab;

    private static final double TOP_BAR_HEIGHT_RATIO = 0.13;
    private static final double FONTSIZE_HEIGHT_RATIO = 1.0 / 40.0;
    private static final double WIDTH_OFFSET_RATIO = 1.0 / 2.0;
    private static final double HEIGHT_OFFSET_RATIO = 1.0 / 2.0;
    private static final double FONTSIZE_CHANGE_RATIO = 0.8;
    private static final double FONTSIZE_OFFSET_RATIO = 1.5;

    ArrayList<String> options;
    public SettingViewRenderer() {
        tab = new TabRenderer();
        options = new ArrayList<>();

        options.add("Username");
        options.add("Default reminder time");
        options.add("Starting view");
    }

    @Override
    public void draw( GraphicsContext gc, double x, double y, double width, double height ) {
        super.draw(gc, 0, 0, width, height);

        double topBarHeight = height * TOP_BAR_HEIGHT_RATIO;
        double windowPadding = 8;
        double settingFieldY = windowPadding + topBarHeight;
        double wordPaddingH = 8;
		double settingHeight = 40;

        tab.draw(gc, 0, 0, width, topBarHeight, TabRenderer.SETTINGS_INDEX);

        gc.setTextAlign(TextAlignment.LEFT);

        double fontSize = width * FONTSIZE_HEIGHT_RATIO;
        Font font = FontLoader.load(fontSize);
        gc.setFont(font);
        gc.setFill(AppColours.autocompleteText);
		gc.setTextBaseline(VPos.TOP);
        gc.setTextAlign(TextAlignment.CENTER);

        gc.fillText("SETTINGS", width * WIDTH_OFFSET_RATIO, height * HEIGHT_OFFSET_RATIO - fontSize * FONTSIZE_OFFSET_RATIO);

        font = FontLoader.load(fontSize * FONTSIZE_CHANGE_RATIO);
        gc.setFont(font);

        gc.fillText("To access this page, please purchase the settings DLC", width * WIDTH_OFFSET_RATIO,  height * HEIGHT_OFFSET_RATIO);
        gc.fillText("Only $9.99 for a limited time!", width * WIDTH_OFFSET_RATIO,  height * HEIGHT_OFFSET_RATIO + fontSize);
    }
}