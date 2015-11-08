//@@author A0133907E
package carelender.view.gui.components;

import carelender.model.strings.AppColours;
import carelender.model.strings.FontLoader;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;

public class PopupRenderer extends CanvasRenderer {
	private String message;
	private final static double arc = 10.0;
	private final static double tabHeightRatio = 1.0 / 10.0;
	private final static double fontSizeWidthRatio = 1.0 / 40.0;
	private final static double textYOffsetRatio = 1.0 / 10.0;
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
		
		double fontSize = width * fontSizeWidthRatio;
		Font font = FontLoader.load( fontSize);
		TextRenderer textRenderer = new TextRenderer();
		textRenderer.setParams(gc, x, y + height * textYOffsetRatio, width, height * textHeightRatio, textPad, textPad, font, wordWidthRatio, lineSpaceRatio);
		textRenderer.addText(this.message);
		textRenderer.drawText(AppColours.popupBackground, AppColours.popupText);
	}
}