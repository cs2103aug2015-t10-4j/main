package carelender.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class TabRenderer extends CanvasRenderer {
	public TabRenderer() {
    }
	
	@Override
	public void draw(GraphicsContext gc, double x, double y, double width, double height) {
		super.draw(gc, x, y, width,height);

		double wordFstXRatio = 1.0/12.0;
		double wordSndXRatio = 7.0/18.0;
		double wordThdXRatio = 25.0/36.0;
		double wordYRatio = 5.0/6.0;
		double wordWidthRatio = 2.0/9.0;

		System.out.println("TabRenderer " + x + " " + y + " " + width + " " + height );
		
		gc.setFill(Color.web("#999"));
		gc.fillRect(x, y, width, height);
		
		double fontSize = width / 20.0; //Temporary
		Font font = Font.loadFont("file:res/monaco.ttf", fontSize);


		gc.setTextAlign(TextAlignment.CENTER);
		gc.setFill(Color.web("#555"));
		gc.setFont(font);

		gc.fillText("Week", width*wordFstXRatio, height*wordYRatio, width*wordWidthRatio);
		gc.fillText("Month", width*wordSndXRatio, height*wordYRatio, width*wordWidthRatio);
		gc.fillText("Setting", width*wordThdXRatio, height*wordYRatio, width*wordWidthRatio);
	}
}