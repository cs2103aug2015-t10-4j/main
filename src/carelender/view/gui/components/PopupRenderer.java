package carelender.view.gui.components;

import carelender.model.strings.AppColours;
import carelender.view.gui.components.CanvasRenderer;
import carelender.view.gui.components.TextRenderer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class PopupRenderer extends CanvasRenderer {
	String message;
	
	public PopupRenderer(String message) {
		this.message = message;
	}

	public PopupRenderer() {
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public void draw( GraphicsContext gc, double x, double y, double width, double height ) {
		gc.setFill(AppColours.popupBackground);
		gc.strokeRoundRect(x, y, width, height, 10.0, 10.0);
		gc.setFill(AppColours.popupHeaderBackground);
		gc.fillRoundRect(x, y, width, height/10.0, 10.0, 10.0);
		
		double fontSize = width / 40.0; //Temporary
		Font font = Font.loadFont("file:res/monaco.ttf", fontSize);
		TextRenderer message = new TextRenderer();
		message.setParams(gc, x, y+height/10.0, width, height*9.0/10.0, 3, 3, font, 0.6, 0.05);
		message.addText(this.message);
		message.drawText(AppColours.popupBackground, AppColours.popupText);
	}
}