//@@author A0125566B
package carelender.view.gui.components;

import carelender.model.strings.AppColours;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;

/**
 * This class contains methods to render wrapping text.
 */
public class TextRenderer {

	private ArrayList<String> textLines;
    private GraphicsContext gc;
    private Font font;
    
    private double xPosition;
    private double yPosition;

    private double width;
    private double height;

    private double xPadding;
    private double yPadding;

    private double charWidth;
    private double charHeight;
    private double lineSpace;

    private double charsPerLine;


    public TextRenderer() {
        this.gc = null;
		this.textLines = new ArrayList<>();
}

    public void setPosition ( double x, double y ) {
    	this.xPosition = x;
    	this.yPosition = y;
    }
    
    public void setParams ( GraphicsContext gc, double x, double y,
                            double w, double h, double xPad, double yPad,
                            Font font, double widthRatio, double lineSpaceRatio ) {
        this.gc = gc;

        this.xPosition = x;
        this.yPosition = y;

        this.width = w;
        this.height = h;

        this.xPadding = xPad;
        this.yPadding = yPad;

        this.font = font;
        this.charHeight = this.font.getSize();
        this.charWidth = this.font.getSize() * widthRatio;
        this.lineSpace = this.font.getSize() * lineSpaceRatio;

        this.charsPerLine = Math.floor ((this.width - (this.xPadding * 2)) / this.charWidth);
		clearText();
		gc.setTextAlign(TextAlignment.LEFT);
    }

    //@@author A0133269A
	/**
	 * Add text to the message box in a single line with ellipsis
	 * @param textLine Line of text to add. Breaklines will be removed
	 */
    public void addTextEllipsis ( String textLine ) {
		if ( textLine == null ) {
			return;
		}
    	if (this.gc == null) {
    		System.out.println("Error");
    	} else {
			textLine = textLine.replace("\n", " ");
	        double freeCharsOnLine = this.charsPerLine - 4 ;// Size of "... "
	        String [] wordsToAdd = textLine.split(" ");
	        StringBuilder stringBuilder = new StringBuilder();
	        boolean outOfSpace = false;
	        for ( String wordToAdd : wordsToAdd ) {
	            freeCharsOnLine -= wordToAdd.length() + 1; //+1 is for space character
	            if ( freeCharsOnLine >= 0 ) {
					stringBuilder.append(wordToAdd);
					stringBuilder.append(" ");
				} else {
	                outOfSpace = true;
					break;
				}
	        }
	        if ( outOfSpace) {
	            stringBuilder.append("...");
	        }
			this.textLines.add(stringBuilder.toString());
    	}
    }

 	//@@author A0125566B
    public void addText ( String textToAdd ) {
		if ( textToAdd == null ) {
			return;
		}
    	if (this.gc == null) {
    		System.out.println("Error");
    	} else {
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
	                } else if ( wordToAdd.length() <= freeCharsOnLine ) {
	                    lineToAppendTo += (wordToAdd + " ");
	                    freeCharsOnLine = this.charsPerLine - lineToAppendTo.length();
	                    this.textLines.set(this.textLines.size() - 1, lineToAppendTo);
	                } else {
	                    freeCharsOnLine = this.charsPerLine;
	                    lineToAppendTo = wordToAdd + " ";
	                    this.textLines.add(lineToAppendTo);
	                }
	            }
	        }
    	}
    }

    public void clearText() {
    	if (this.gc == null) {
    		System.out.println("Error");
    	} else {
    		this.textLines.clear();
    	}
    }

    public void drawText ( GraphicsContext gc, String background, String text ) {
    	if (gc == null) {
    		System.out.println("Error drawing text.");
    	} else {
	        double xCurrent = this.xPosition + this.xPadding;
	        double yCurrent = this.yPosition + (this.yPadding + this.charHeight);
	
	        gc.setFill(Color.web(background));
	        gc.fillRect(this.xPosition, this.yPosition, this.width, this.height);
	
	        for ( String lineToDraw : this.textLines ) {
	
	            gc.setFill(Color.web(text));
	            gc.setTextAlign(TextAlignment.LEFT);
	            gc.setFont(this.font);
	            gc.setTextBaseline(VPos.BOTTOM);
	
	            gc.fillText ( lineToDraw, xCurrent, yCurrent );
	            yCurrent += ( this.lineSpace + this.charHeight );
	        }
    	}
    }

	/**
	 * Draws the text on screen with the parameters that have been set
	 * @param background Color object representing the background colour
	 * @param text Color object representing the text colour
	 * @param boldFirst Number of lines to bold
	 */
	public void drawText ( Color background, Color text, int boldFirst ) {
		if (this.gc == null) {
			System.out.println("Error");
		} else {
			double xCurrent = this.xPosition + this.xPadding;
			double yCurrent = this.yPosition + (this.yPadding + this.charHeight);

			gc.setFill(background);
			gc.fillRect(this.xPosition, this.yPosition, this.width, this.height);

			this.gc.setFill(text);
			for ( String lineToDraw : this.textLines ) {

				this.gc.setStroke(text);
				this.gc.setFill(text);
				this.gc.setTextAlign(TextAlignment.LEFT);
				this.gc.setFont(this.font);
				this.gc.setTextBaseline(VPos.BOTTOM);

				this.gc.fillText ( lineToDraw, xCurrent, yCurrent );
				if ( boldFirst > 0 ) {
					this.gc.strokeText( lineToDraw, xCurrent, yCurrent );
					boldFirst--;
				}
				yCurrent += ( this.lineSpace + this.charHeight );
			}
		}
	}
	/**
	 * Draws the text on screen with the parameters that have been set
	 * @param background Color object representing the background colour
	 * @param text Color object representing the text colour
	 */
    public void drawText ( Color background, Color text ) {
		if (this.gc == null) {
			System.out.println("Error");
		} else {
			drawText(background, text, 0);
		}
    }
	/**
	 * Draws the text on screen with the parameters that have been set
	 * @param background Color object representing the background colour
	 */
    public void drawText (Color background) {
    	if (this.gc == null) {
    		System.out.println("Error");
    	} else {
    		drawText(background, AppColours.black);
    	}
    }

	/**
	 * Draws the text on screen with the parameters that have been set
	 */
    public void drawText () {
    	if (this.gc == null) {
    		System.out.println("Error");
    	} else {
    		drawText(AppColours.primaryColour);
    	}
    }
    
    public double getTextHeight () {
    	return (this.textLines.size() * this.charHeight)
    			+ (((this.textLines.size() - 1) <= 0 ? 0 : (this.textLines.size() - 1)) * this.lineSpace)
    			+ (this.yPadding * 2);
    }
    
    private String separateEndline ( String toParse ) {
		if ( toParse == null ) {
			return "";
		}
    	if (this.gc == null) {
    		System.out.println("Error");
    		return "";
    	} else {
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
}
