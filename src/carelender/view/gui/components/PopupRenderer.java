//@@author A0133907E
package carelender.view.gui.components;

import carelender.model.strings.AppColours;
import carelender.model.strings.FontLoader;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;

public class PopupRenderer extends CanvasRenderer {
	String message;
	private final static double ROUNDED_RADIUS = 10.0;
	private final static double TAB_HEIGHT_RATIO = 1.0 / 10.0;
	private final static double FONT_SIZE_RATIO = 1.0 / 40.0;
	private final static double TEXT_HEIGHT_OFFSET_RATIO = 1.0 / 10.0;
	private final static double TEXT_HEIGHT_RATIO = 9.0 / 10.0;
	private final static double TEXT_PAD = 3.0;
	private final static double LINE_SPACE_RATIO = 0.05;

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
		gc.strokeRoundRect(x, y, width, height, ROUNDED_RADIUS, ROUNDED_RADIUS);
		gc.setFill(AppColours.popupHeaderBackground);
		gc.fillRoundRect(x, y, width, height * TAB_HEIGHT_RATIO, ROUNDED_RADIUS, ROUNDED_RADIUS);
		
		double fontSize = width * FONT_SIZE_RATIO;
		Font font = FontLoader.load( fontSize);
		TextRenderer message = new TextRenderer();
		message.setParams(gc, x, y + height * TEXT_HEIGHT_OFFSET_RATIO, width, height * TEXT_HEIGHT_RATIO, TEXT_PAD, TEXT_PAD, font, LINE_SPACE_RATIO);
		message.addText(this.message);
		message.drawText(AppColours.popupBackground, AppColours.popupText);
	}
}