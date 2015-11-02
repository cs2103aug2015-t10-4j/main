package carelender.view.gui.components;

import carelender.model.strings.AppColours;
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
	
	private GraphicsContext gc;

    private double totalWidth;
    private double actualWidth;
    private double height;

	private String text;

	private double startTime;
	private double endTime;

	public TimelineBarRenderer() {
	}
	
	public void setParams ( GraphicsContext gc, double width, double height,
							double startTime, double endTime, String content) {
		this.totalWidth = width;
		this.height = height;
		
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
				this.actualWidth = TimelineBarRenderer.NO_RANGE_RATIO * width;
			} else {
				this.actualWidth = ((this.endTime - this.startTime) / TimelineBarRenderer.MINUTES_IN_DAY) * width;
			}
			
			gc.setFill(backgroundColour);
			gc.fillRect(xPosition + ((this.startTime / TimelineBarRenderer.MINUTES_IN_DAY) * width),
						yPosition, this.actualWidth, height);
			
			Font font = Font.loadFont("file:res/monaco.ttf", height * 0.7);
			gc.setFill(AppColours.primaryColour);
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setFont(font);
            gc.setTextBaseline(VPos.TOP);

            gc.fillText ( this.text, xPosition + ((this.startTime  / TimelineBarRenderer.MINUTES_IN_DAY) * width) + (this.actualWidth * 0.5), yPosition );
		}
	}
}
