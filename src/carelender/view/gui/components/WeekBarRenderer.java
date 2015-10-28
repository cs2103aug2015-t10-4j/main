package carelender.view.gui.components;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * Written by : Weizheng Lee 27/10/2015
 * This class contains static methods to help to render the calendar view
 */
public class WeekBarRenderer {
	public final static double MINUTES_IN_DAY = 1440;
	private final static double NO_RANGE_RATIO = 0.01;
	
	private GraphicsContext gc;

    private double totalWidth;
    private double actualWidth;
    private double height;

	private String text;

	private double startTime;
	private double endTime;

	public WeekBarRenderer () {
	}
	
	public void setParams ( GraphicsContext gc, double width, double height,
							double startTime, double endTime, String content) {
		this.totalWidth = width;
		this.height = height;
		
		this.startTime = startTime;
		this.endTime = endTime;
		
		this.text = content;
	}
    
	public void draw (GraphicsContext gc, String backgroundColour, String textColour,
						double xPosition, double yPosition, double width, double height ) {
		if (gc == null) {
			System.out.println("Error");
		} else {
			if ( this.endTime <= this.startTime ) {
				this.actualWidth = WeekBarRenderer.NO_RANGE_RATIO * width;
			} else {
				this.actualWidth = ((this.endTime - this.startTime) / WeekBarRenderer.MINUTES_IN_DAY) * width;
			}
			
			gc.setFill(Color.web(backgroundColour));
			gc.fillRect(xPosition + ((this.startTime / WeekBarRenderer.MINUTES_IN_DAY) * width),
						yPosition, this.actualWidth, height);
			
			Font font = Font.loadFont("file:res/monaco.ttf", height * 0.7);
			gc.setFill(Color.web("4ecdc4"));
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setFont(font);
            gc.setTextBaseline(VPos.TOP);

            gc.fillText ( this.text, xPosition + ((this.startTime  / WeekBarRenderer.MINUTES_IN_DAY) * width) + (this.actualWidth * 0.5), yPosition );
		}
	}
}
