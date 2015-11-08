package carelender.view.gui.components;

import carelender.model.strings.AppColours;
import carelender.model.strings.FontLoader;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * Written by : Weizheng Lee 27/10/2015
 * This class contains static methods to help to render the timeline view
 */
public class TimelineBarRenderer {
	public final static double MINUTES_IN_DAY = 1440;
	private final static double NO_RANGE_RATIO = 0.01;

    private double actualHeight;

	private String text;

	private double startTime;
	private double endTime;

	public TimelineBarRenderer() {
	}
	
	public void setParams ( double startTime, double endTime, String content) {
		this.startTime = startTime;
		this.endTime = endTime;
		
		this.text = content;
	}
    
	public void draw (GraphicsContext gc, Color backgroundColour, Color textColour,
						double xPosition, double yPosition, double width, double height ) {
		if (gc == null) {
			System.out.println("Error");
		} else {
			if ( this.endTime <= this.startTime ) {
				this.actualHeight = TimelineBarRenderer.NO_RANGE_RATIO * height;
			} else {
				this.actualHeight = ((this.endTime - this.startTime) / TimelineBarRenderer.MINUTES_IN_DAY) * height;
			}
			
			gc.setFill(backgroundColour);
			gc.setGlobalAlpha(0.5);
			gc.fillRect(xPosition, yPosition + ((this.startTime / TimelineBarRenderer.MINUTES_IN_DAY) * height),
						width, this.actualHeight);
			gc.fillRect(xPosition + 2.5, yPosition + ((this.startTime / TimelineBarRenderer.MINUTES_IN_DAY) * height) + 2.5,
					width - 5, this.actualHeight - 5);
			gc.setGlobalAlpha(1);
			
			Font font = FontLoader.load( width * 0.7);
			gc.setFill(textColour);
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setFont(font);
            gc.setTextBaseline(VPos.CENTER);

            gc.fillText (this.text, xPosition + (width * 0.5),
            			yPosition + ((this.startTime  / TimelineBarRenderer.MINUTES_IN_DAY) * height) + (this.actualHeight * 0.5));
		}
	}
}
