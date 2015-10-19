package carelender.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class MonthViewRenderer extends CanvasRenderer {
	//CalenderRenderer
	//MessageboxRenderer
	//SelectionPopupRenderer

	TextRenderer messageBox;
	String messageText;
	public MonthViewRenderer() {
	}
	
	@Override
	public void draw( GraphicsContext gc, double width, double height ) {
		super.draw(gc, width, height);
		
		//Todo: 20 -> meaningful expression
		double fontSize = width / 60.0; //Temporary
		Font font = Font.loadFont("file:res/monaco.ttf", fontSize);

		/* Todo
		 * replace magic numbers;
		 * create specific class for these renderers;
		 */
		TextRenderer announcementBox = new TextRenderer (gc, 0, height/7,
														width*4/10, height*3/10, 0, 0,
														font, 0.6, fontSize*0.05);
		announcementBox.addText("This is a announcementRenderer.\n");
		announcementBox.drawText();
		
        messageBox = new TextRenderer (gc, 0, height/7 + height*3/10,
        											width*4/10, height*5/10, 0, 0,
													font, 0.6, fontSize*0.05);
        messageBox.addText(messageText);
        messageBox.drawText();
	}

	public void setMessageBoxText(String text) {
		messageText = text;
		redraw();
	}
}