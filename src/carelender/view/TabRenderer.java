package carelender.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class TabRenderer extends CanvasRenderer {
	public TabRenderer() {
    }
	
	@Override
	public void draw(GraphicsContext gc, double x, double y, double width, double height) {
		super.draw(gc, x, y, width,height);

		//Three textRender; Week, Month, Setting
		//Two darker color
		//One bright color
		//Choose font size; maybe bold
		//Calculate height and width
		//Background color
		
		gc.setFill(Color.web("#999"));
		gc.fillRect(x, y, width, height);
		
		double fontSize = width / 20.0; //Temporary
		Font font = Font.loadFont("file:res/monaco.ttf", fontSize);
		
		gc.setFill(Color.web("#555"));
		gc.setFont(font);
		gc.fillText("Week", width/12, height*5/6, width*2/9);
		gc.fillText("Month", width*7/18, height*5/6, width*2/9);
		gc.fillText("Setting", width*25/36, height*5/6, width*2/9);
	}
}