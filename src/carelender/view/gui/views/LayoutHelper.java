//@@author A0133269A
package carelender.view.gui.views;

import carelender.model.strings.FontLoader;
import javafx.scene.text.Font;

/**
 * This class is to do all the layout calculations for the three main views
 * It exists to have a single place to store all the layout parameters since
 * they follow mostly the same layout
 */
public class LayoutHelper {
    private static final double WINDOW_PADDING = 8;
    private static final double TEXTBOX_INNER_PADDING = 8;
    private static final double FONT_RATIO = 1/60.0;
    private static final double TOP_BAR_RATIO = 0.13;
    private static Font font;
    private static double topBarHeight;
    private static double leftColumnX;
    private static double rightColumnX;
    private static double announcementBoxY;
    private static double rightColumnWidth;
    private static double leftColumnWidth;
    private static double mainContentHeight;
    private static double announcementHeight;
    private static double messageBoxHeight;
    private static double mainContentY;
    private static double messageBoxY;


    public static void setParams(double x, double y, double width, double height) {
        double fontSize = width * FONT_RATIO;
        font = FontLoader.load( fontSize );

        topBarHeight = height * TOP_BAR_RATIO;
        double remainderHeight = height - topBarHeight;

        announcementBoxY = topBarHeight + WINDOW_PADDING;

        leftColumnWidth = 0.5 * width - WINDOW_PADDING;
        rightColumnWidth = 0.5 * width;
        leftColumnX = x;
        rightColumnX = leftColumnX + leftColumnWidth + WINDOW_PADDING;

        announcementHeight = fontSize + TEXTBOX_INNER_PADDING * 2;
        messageBoxHeight = fontSize + TEXTBOX_INNER_PADDING * 2;
        mainContentHeight = remainderHeight - announcementHeight - messageBoxHeight - WINDOW_PADDING * 3;
        mainContentY = announcementBoxY + announcementHeight + WINDOW_PADDING;
        messageBoxY = mainContentY + mainContentHeight + WINDOW_PADDING;

    }

    public static Font getFont() {
        return font;
    }

    public static double getLeftColumnX() {
        return leftColumnX;
    }

    public static double getRightColumnX() {
        return rightColumnX;
    }

    public static double getAnnouncementBoxY() {
        return announcementBoxY;
    }

    public static double getMessageBoxHeight() {
        return messageBoxHeight;
    }

    public static double getAnnouncementHeight() {
        return announcementHeight;
    }

    public static double getMainContentHeight() {
        return mainContentHeight;
    }

    public static double getTopBarHeight() {
        return topBarHeight;
    }

    public static double getLeftColumnWidth() {
        return leftColumnWidth;
    }

    public static double getRightColumnWidth() {
        return rightColumnWidth;
    }

    public static double getTextboxInnerPadding() {
        return TEXTBOX_INNER_PADDING;
    }

    public static double getMainContentY() {
        return mainContentY;
    }

    public static double getMessageBoxY() {
        return messageBoxY;
    }
}
