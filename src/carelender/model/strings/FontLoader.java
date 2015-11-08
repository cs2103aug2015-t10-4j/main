package carelender.model.strings;

import javafx.scene.text.Font;

/**
 * Used to create the fonts throughout the application
 */
public class FontLoader {
    public static Font load(double size) {
        return Font.loadFont("file:res/monaco.ttf", size);
    }
}
