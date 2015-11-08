//@@author A0125566B
package carelender.view.gui.components;

import carelender.model.strings.FontLoader;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * This class contains static methods to help to render the timeline view
 */
public class TimelineBarRenderer {
	public static final double MINUTES_IN_DAY = 1440;
	
	private static final double FADEOUT_ALPHA = 0.5;
	private static final double DEFAULT_ALPHA = 1;
	private static final double BAR_BORDER_SIZE = 5;
	private static final double FONT_SIZE_RATIO = 0.7;
	private static final double NO_RANGE_RATIO = 0.01;

    private double actualHeight;
    private double startTime;
	private double endTime;
	
	private String text;

	public TimelineBarRenderer() {
		this.actualHeight = 0;
	    this.startTime = 0;
		this.endTime = 0;
		
		this.text = "";
	}
	
	/**
	 * Set dimensions for timeline bar.
	 * 
	 * @param startTime
	 * @param endTime
	 * @param content
	 * 		Content to be displayed within the bar.
	 */
	public void setParams ( double startTime, double endTime, String content) {
		this.startTime = startTime;
		this.endTime = endTime;
		
		this.text = content;
	}
    
	public void draw (GraphicsContext gc, Color backgroundColour, Color textColour,
						double x, double y, double width, double height) {
		if (gc == null) {
			System.out.println("Error");
		} else {
			if ( this.endTime <= this.startTime ) {
				this.actualHeight = NO_RANGE_RATIO * height;
			} else {
				this.actualHeight = ((this.endTime - this.startTime) / MINUTES_IN_DAY) * height;
			}
			
			renderBar(gc, x, y, width, height, backgroundColour, textColour);
		}
	}
	
	/**
	 * Render the timeline bar.
	 * Text content is loaded in the center of the bar.
	 * 
	 * @param gc
	 * @param xCurrent
	 * @param yCurrent
	 * @param width
	 * @param height
	 * @param backgroundColour
	 * @param textColour
	 */
	private void renderBar (GraphicsContext gc, double xCurrent, double yCurrent,
							double width, double height,
							Color backgroundColour, Color textColour) {
		gc.setFill(backgroundColour);
		gc.setGlobalAlpha(FADEOUT_ALPHA);
		gc.fillRect(xCurrent, yCurrent + ((startTime / MINUTES_IN_DAY) * height),
					width, actualHeight);
		gc.fillRect(xCurrent + (BAR_BORDER_SIZE * 0.5), yCurrent + ((startTime / MINUTES_IN_DAY) * height) + (BAR_BORDER_SIZE * 0.5),
				width - BAR_BORDER_SIZE, actualHeight - BAR_BORDER_SIZE);
		gc.setGlobalAlpha(DEFAULT_ALPHA);
		
		Font font = FontLoader.load( width * FONT_SIZE_RATIO);
		gc.setFill(textColour);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(font);
        gc.setTextBaseline(VPos.CENTER);

        gc.fillText (text, xCurrent + (width * 0.5),
        			yCurrent + ((startTime  / MINUTES_IN_DAY) * height) + (actualHeight * 0.5));
	}
}
