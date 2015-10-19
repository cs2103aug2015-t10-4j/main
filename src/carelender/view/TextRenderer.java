package carelender.view;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Written by : Weizheng Lee 18/10/2015
 * This class contains methods to render wrapping text.
 */
public class TextRenderer {
	
	private GraphicsContext gc;
	
	private double xPosition;
	private double yPosition;
	
	private double width;
	private double height;
	
	private double xPadding;
	private double yPadding;
	
	private Font font;
	private double charWidth;
	private double charHeight;
	private double lineSpace;
	
	private ArrayList<String> textLines;
	private double charsPerLine;
	
	public TextRenderer ( GraphicsContext gc, double x, double y,
							double w, double h, double xPad, double yPad,
							Font font, double charH, double charW, double lineSpace ) {
		this.gc = gc;
		
		this.xPosition = x;
		this.yPosition = y;
		
		this.width = w;
		this.height = h;
		
		this.xPadding = xPad;
		this.yPadding = yPad;
		
		this.font = font;
		this.charHeight = this.font.getSize();
		this.charWidth = this.font.getSize() * 0.5;
		this.lineSpace = lineSpace;
		
		this.charsPerLine = Math.floor ((this.width - (this.xPadding * 2)) / this.charWidth);
		this.textLines = new ArrayList<String>();
	}
	
	public void addText ( String textToAdd ) {
		double freeCharsOnLine = this.charsPerLine;
		String lineToAppendTo = "";
		
		String [] wordsToAdd = this.separateEndline(textToAdd).split(" ");
		//Check if there are already lines of text to append textToAdd to.
		if ( !this.textLines.isEmpty() ) {
			//Get the last line rendered to check if words can be appended on.
			lineToAppendTo = this.textLines.get(this.textLines.size() - 1);
			freeCharsOnLine -= lineToAppendTo.length();
		} else {
			this.textLines.add("");
		}
		
		for ( String wordToAdd : wordsToAdd ) {
			if ( wordToAdd.length() > 0 ) {
				if ( wordToAdd.equals("\n") ) {
					freeCharsOnLine = this.charsPerLine;
					lineToAppendTo = "";
					this.textLines.add(lineToAppendTo);
				} else if ( wordToAdd.length() < freeCharsOnLine ) {
					lineToAppendTo += wordToAdd + " ";
					freeCharsOnLine -= wordToAdd.length();
					this.textLines.set(this.textLines.size() - 1, lineToAppendTo);
				} else {
					freeCharsOnLine = this.charsPerLine;
					lineToAppendTo = wordToAdd + " ";
					this.textLines.add(lineToAppendTo);
				}
			}
		}
	}
	
	public void drawText () {
		double xCurrent = this.xPosition + this.xPadding;
		double yCurrent = this.yPosition + (this.yPadding + this.charHeight);
		
		gc.setFill(Color.web("#979"));
        gc.fillRect(this.xPosition, this.yPosition, this.width, this.height);
        
		for ( String lineToDraw : this.textLines ) {
			
			this.gc.setFill(Color.web("#000"));
			this.gc.setTextAlign(TextAlignment.LEFT);
			this.gc.setFont(this.font);
			this.gc.setTextBaseline(VPos.BOTTOM);
			
			this.gc.fillText ( lineToDraw, xCurrent, yCurrent );
			yCurrent += ( this.lineSpace + this.charHeight );
		}
	}
	
	private String separateEndline ( String toParse ) {
		String toAppend = "";
		String toReturn = "";
		String [] stringSegment = toParse.split("\n", -1);
		for ( String segment : stringSegment ) {
			toReturn += (toAppend + segment);
			toAppend = " " + "\n" + " ";
		}
		return toReturn.equals("") ? toParse : toReturn;
	}
}
