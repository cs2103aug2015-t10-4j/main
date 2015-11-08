//@@author A0133269A
package carelender.model.strings;

import javafx.scene.text.Font;

/**
 * Used to create the fonts throughout the application
 */
public class FontLoader {
    public static Font load(double size) {
        return Font.loadFont("file:res/monaco.ttf", size);
    }
    public static double FONT_WIDTH_RATIO = 0.6; //Ratio of the font width to height
    public static double DEFAULT_LINE_HEIGHT_RATIO = 0.05; //Ratio of the font width to height
}
