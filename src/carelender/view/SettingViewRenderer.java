package carelender.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class SettingViewRenderer extends CanvasRenderer {
	TabRenderer tab;
	
	public SettingViewRenderer() {
        tab = new TabRenderer();
		//User profile
			//User name
		//System setting
			//Main view
			//Alarm
	}
	
	@Override
	public void draw( GraphicsContext gc, double x, double y, double width, double height ) {
		super.draw(gc, 0, 0, width, height);
		
		double topBarHeight = height * 0.13;
		double windowPadding = 8;
		double settingFieldY = windowPadding + topBarHeight;
		double wordPaddingH = 8;

		tab.draw(gc, 0, 0, width, topBarHeight, 3);

        gc.setTextAlign(TextAlignment.LEFT);

		gc.setFill(Color.web("BLUE"));
		gc.fillRect(0, settingFieldY, width, height-settingFieldY);
		
		double fontSize = width / 40.0; //Temporary
		Font font = Font.loadFont("file:res/monaco.ttf", fontSize);
		gc.setFont(font);
		gc.setFill(Color.web("YELLOW"));
		gc.fillText("Settings", wordPaddingH, settingFieldY + wordPaddingH*4);
	}
}