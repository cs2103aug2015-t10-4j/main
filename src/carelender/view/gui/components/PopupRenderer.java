//@@author A0133907E
package carelender.view.gui.components;

import carelender.model.strings.AppColours;
import carelender.model.strings.FontLoader;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;

public class PopupRenderer extends CanvasRenderer {
	String message;
	private final static double arc = 10.0;
	private final static double tabHeightRatio = 1.0 / 10.0;
	private final static double fontSizeRatio = 1.0 / 40.0;
	private final static double textHeightOffsetRatio = 1.0 / 10.0;
	private final static double textHeightRatio = 9.0 / 10.0;
	private final static double textPad = 3.0;
	private final static double wordWidthRatio = 0.6;
	private final static double lineSpaceRatio = 0.05;

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
		gc.strokeRoundRect(x, y, width, height, arc, arc);
		gc.setFill(AppColours.popupHeaderBackground);
		gc.fillRoundRect(x, y, width, height * tabHeightRatio, arc, arc);
		
		double fontSize = width * fontSizeRatio;
		Font font = FontLoader.load( fontSize);
		TextRenderer message = new TextRenderer();
		message.setParams(gc, x, y + height * textHeightOffsetRatio, width, height * textHeightRatio, textPad, textPad, font, wordWidthRatio, lineSpaceRatio);
		message.addText(this.message);
		message.drawText(AppColours.popupBackground, AppColours.popupText);
	}
}