package carelender.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class PopupRenderer extends CanvasRenderer {
	public PopupRenderer() {}

	@Override
	public void draw( GraphicsContext gc, double x, double y, double width, double height ) {
		gc.setFill(Color.web("Black"));
		gc.strokeRoundRect(x, y, width, height, 10.0, 10.0);
		gc.setFill(Color.web("BLUE"));
		gc.fillRoundRect(x, y, width, height/6.0, 10.0, 10.0);
		gc.setFill(Color.web("WHITE"));
		gc.fillRoundRect(x, y+height/6.0, width, height*5.0/6.0, 10.0, 10.0);
		gc.setFill(Color.web("BLACK"));
		gc.fillText("Something", x+width*2.0/3.0, y+height/3.0);
	}
}