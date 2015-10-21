package carelender.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class PopupRenderer extends CanvasRenderer {
	public PopupRenderer() {}

	@Override
	public void draw( GraphicsContext gc, double x, double y, double width, double height ) {
		System.out.println("Popup");
		gc.setFill(Color.web("Black"));
		gc.strokeRoundRect(x, y, width, height, 4, 4);
		gc.setFill(Color.web("BLUE"));
		gc.fillRoundRect(x+4, y+4, width/6, height/6, 4, 4);
		gc.setFill(Color.web("BLACK"));
		gc.fillText("Hello", width/2, height/2);
	}
}