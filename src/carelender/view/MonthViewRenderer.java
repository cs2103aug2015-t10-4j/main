package carelender.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class MonthViewRenderer extends CanvasRenderer {
	//CalenderRenderer
	//MessageboxRenderer
	//SelectionPopupRenderer

	TextRenderer messageBox = new TextRenderer();
	TextRenderer announcementBox = new TextRenderer();
    AutocompleteRenderer autocompleteRenderer;
    CalenderRenderer calender = new CalenderRenderer();
	String messageText;
	public MonthViewRenderer() {
        autocompleteRenderer = new AutocompleteRenderer();
	}
	
	@Override
	public void draw( GraphicsContext gc, double x, double y, double width, double height ) {
		super.draw(gc, 0, 0, width, height);
		
		//Todo: 20 -> meaningful expression
		double fontSize = width / 60.0; //Temporary
		Font font = Font.loadFont("file:res/monaco.ttf", fontSize);

		double windowPadding = 8;
		double textboxInnerPadding = 8;
		double topBarHeight = height * 0.13;
		double remainderHeight = height - topBarHeight;
		double announcementBoxH = remainderHeight * 0.3 - windowPadding;
		double messageBoxH = remainderHeight * 0.7 - windowPadding;

		double announcementBoxY = topBarHeight + windowPadding;
		double messageBoxY = announcementBoxY + announcementBoxH + windowPadding;


		TabRenderer tab = new TabRenderer(); //TODO: Move this out of draw
		tab.draw(gc, 0, 0, width, topBarHeight);



		/* Todo
		 * replace magic numbers;
		 * create specific class for these renderers;
		 */

        //TODO: Move all new TextRenderers out of draw
		announcementBox.setParams(gc, 0, announcementBoxY,
								width*4/10, announcementBoxH, textboxInnerPadding, textboxInnerPadding,
								font, 0.6, 0.05);
		announcementBox.addText("This is a announcementRenderer.\n");
		announcementBox.drawText();
		
        messageBox.setParams(gc, 0, messageBoxY,
        					width*4/10, messageBoxH, textboxInnerPadding, textboxInnerPadding,
							font, 0.6, 0.05);
        messageBox.addText(messageText);
        messageBox.drawText();
        
        calender.draw(gc, width*2/5, height*3/10, width*3/5, height*7/10);

        autocompleteRenderer.draw(gc, 0, height, width, 0);
	}

	public void setMessageBoxText(String text) {
		messageText = text;
		redraw();
	}
    public void setAutocompleteOptions ( String [] options ) {
        autocompleteRenderer.setAutocompleteOptions(options);
        redraw();
    }
}