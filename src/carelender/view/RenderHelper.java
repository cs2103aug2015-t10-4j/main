package carelender.view;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class contains static methods to help to render the calendar view
 */
public class RenderHelper {
    /**
     * Draws a calendar square with a drop shadow
     * @param gc
     * @param x
     * @param y
     * @param w
     * @param h
     * @param dropOffset
     * @param color
     * @param text
     * @param dailyEventNumbers 
     */
    public static void calendarSquare ( GraphicsContext gc,  double x, double y, double w, double h, double dropOffset, String color, String text, Font font, int[] dailyEventNumbers ) {
        gc.setFill(Color.web("#999"));
        gc.fillRect(x + dropOffset, y + dropOffset, w, h);

        gc.setFill(Color.web(color));
        gc.fillRect(x, y, w, h);

        gc.setFill(Color.web("4ecdc4"));
        gc.setTextAlign(TextAlignment.RIGHT);
        gc.setFont(font);
        gc.setTextBaseline(VPos.BOTTOM);
        gc.fillText(text, x + w - dropOffset * 0.5 , y + h - dropOffset * 0.5 );
        
        for (int i=0; i<dailyEventNumbers.length; i++) {
        	int numArc = dailyEventNumbers[i];
        	if (numArc > 3) {
        		numArc = 3;
        	}
        	for (int j=0; j<numArc; j++) {
        		System.out.println("I tried with i " + i + " j " + j);
        		gc.setFill(Color.web("4ecdc4"));
        		gc.arc(x + w*((double) j + 1)/40.0, y + h*((double) i + 1)/40.0, w/40, h/40, 0, 0);
        	}
        }
    }


}
