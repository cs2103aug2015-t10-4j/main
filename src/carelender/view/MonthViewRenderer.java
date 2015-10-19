package carelender.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class MonthViewRenderer extends CanvasRenderer {
	//CalenderRenderer
	//MessageboxRenderer
	//SelectionPopupRenderer
	
	public MonthViewRenderer() {
	}
	
	@Override
	public void draw( GraphicsContext gc, double width, double height ) {
		super.draw(gc, width, height);
		
		//Todo: 20 -> meaningful expression
		Font font = new Font("Arial", 20);

		/* Todo
		 * replace magic numbers;
		 * create specific class for these renderers;
		 */
		TextRenderer announcementBox = new TextRenderer (gc, 0, height/4,
														width*3/10, height*3/10, 0, 0,
														font, 20, 12, 10);
		announcementBox.addText("This is a announcementRenderer.\n");
		announcementBox.drawText();
		
        TextRenderer messageBox = new TextRenderer (gc, 0, height*3/5,
        											width*3/10, height*2/5, 0, 0,
													font, 20, 12, 10);
        messageBox.addText("This is a messageBoxRenderer.\n");
        messageBox.drawText();
	}
}