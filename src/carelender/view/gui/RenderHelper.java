package carelender.view.gui;

import carelender.model.strings.AppColours;
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
     * @param gc GraphicsContext of canvas
     * @param x X position
     * @param y Y position
     * @param w Width
     * @param h Height
     * @param dropOffset Offset of drop shadow
     * @param background Colour of square
     * @param textColor Colour of text
     * @param text Text to show at bottom right
     * @param dailyEventNumbers Array of integers to show the dots
     */
    public static void calendarSquare ( GraphicsContext gc,  double x, double y, double w, double h, double dropOffset, Color background, Color textColor, String text, Font font, int[] dailyEventNumbers ) {
        gc.setFill(Color.web("#999"));
        gc.fillRect(x + dropOffset, y + dropOffset, w, h);

        gc.setFill(background);
        gc.fillRect(x, y, w, h);

        gc.setFill(textColor);
        gc.setTextAlign(TextAlignment.RIGHT);
        gc.setTextBaseline(VPos.BOTTOM);
        gc.setFont(font);
        gc.fillText(text, x + w - dropOffset * 0.5 , y + h - dropOffset * 0.5 );
        
        double x_offset;
        double y_offset;
        
        for (int i = 0; i < dailyEventNumbers.length; i++) {
        	int numArc = dailyEventNumbers[i];
        	
        	if (numArc > 3) {
        		numArc = 3;
        	}
        	for (int j=0; j<numArc; j++) {
        		gc.setFill(AppColours.primaryColour);
        		x_offset = x + w*((double) j)/10.0 + w*((double) j + 1.0)/20.0;
        		y_offset = y + h*((double) i)/10.0 + h*((double) i + 1.0)/20.0;
        		gc.fillRect(x_offset, y_offset, w/10.0, h/10.0);
        	}
        }
    }


}
