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
		
		TextRenderer weekTab = new TextRenderer (gc, 0, 0,
												width/4, height, 0, 0,
												new Font("Bold", 18), 0.6, 18*0.05);
		weekTab.addText("Week");
		weekTab.drawText();

		TextRenderer monthTab = new TextRenderer (gc, width/3, 0,
												width/4, height, 0, 0,
												new Font("Bold", 18), 0.6, 18*0.05);
		monthTab.addText("Month");
		monthTab.drawText();
		
		TextRenderer settingTab = new TextRenderer (gc, width*2/3, 0,
												width/4, height, 0, 0,
												new Font("Bold", 18), 0.6, 18*0.05);
		settingTab.addText("Setting");
		settingTab.drawText();
	}
}