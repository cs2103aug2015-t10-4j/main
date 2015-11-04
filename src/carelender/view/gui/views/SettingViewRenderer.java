package carelender.view.gui.views;

import carelender.model.strings.AppColours;
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

    ArrayList<String> options;
    public SettingViewRenderer() {
        tab = new TabRenderer();
        //User profile
            //User name
        //System setting
            //Main view
            //Alarm
        options = new ArrayList<>();

        options.add("Username");
        options.add("Default reminder time");
        options.add("Starting view");
    }

    @Override
    public void draw( GraphicsContext gc, double x, double y, double width, double height ) {
        super.draw(gc, 0, 0, width, height);

        double topBarHeight = height * 0.13;
        double windowPadding = 8;
        double settingFieldY = windowPadding + topBarHeight;
        double wordPaddingH = 8;
		double settingHeight = 40;

        tab.draw(gc, 0, 0, width, topBarHeight, 3);

        gc.setTextAlign(TextAlignment.LEFT);

        //gc.setFill(Color.web("BLUE"));
        //gc.fillRect(0, settingFieldY, width, height-settingFieldY);

        double fontSize = width / 40.0; //Temporary
        Font font = Font.loadFont("file:res/monaco.ttf", fontSize);
        gc.setFont(font);
        gc.setFill(AppColours.autocompleteText);
		gc.setTextBaseline(VPos.TOP);
        gc.setTextAlign(TextAlignment.CENTER);

        gc.fillText("SETTINGS", width / 2, height / 2 - fontSize * 1.5);

        font = Font.loadFont("file:res/monaco.ttf", fontSize * 0.8);
        gc.setFont(font);

        gc.fillText("To access this page, please purchase the settings DLC", width / 2,  height / 2 );
        gc.fillText("Only $9.99 for a limited time!", width / 2,  height / 2 + fontSize);

		/*gc.fillText("Settings", wordPaddingH, settingFieldY + wordPaddingH);
        for ( int i = 0 ; i < options.size(); i++ ) {
			gc.fillText(options.get(i), wordPaddingH, settingFieldY + wordPaddingH + settingHeight * (i+1));
		}*/


    }
}