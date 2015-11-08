//@@author A0125566B
package carelender.view.gui.components;

import carelender.model.data.DateRange;
import carelender.model.data.Event;
import carelender.model.strings.AppColours;
import carelender.model.strings.FontLoader;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.Date;

/**
 * This class contains methods to render each task bar in the task view.
 */
public class TaskBarRenderer {
	private static final double FADEOUT_ALPHA = 0.3;
	private static final double DEFAULT_ALPHA = 1;
	
	private static final double NAME_WIDTH_RATIO = 0.8;
	private static final double NAME_Y_PADDING = 0;
	private static final double NAME_X_PADDING = 0;
	private static final double NAME_LINE_PADDING_RATIO = 0;
	private static final double NAME_FONT_WIDTH_RATIO = 0.6;
	
	private static final double CATEGORY_WIDTH_RATIO = 0.2;
	
	private static final double DATERENDERER_X_PADDING_RATIO = 0.1;
	private static final double DATERENDERER_Y_PADDING_RATIO = 0.1;
	private static final double DATERENDERER_TIMETEXT_WIDTH_RATIO = 0.2;
	private static final double DATERENDERER_TIMETEXT_HEIGHT_RATIO = 0.4;
	private static final double DATERENDERER_DATETEXT_HEIGHT_RATIO = 0.7;
	private static final double DATERENDERER_CONNECTOR_WIDTH_RATIO = 0.3;
	
	private ArrayList<DateRangeRenderer> dateRangeRendererList;
    private TextRenderer nameText;
    private Event event;
    
    private double xPadding;
    private double yPadding;
    private double nameTextRatio;
    
    private boolean strikeout;

    public TaskBarRenderer() {
        this.nameText = new TextRenderer();
        
        this.dateRangeRendererList = new ArrayList<>();
        this.strikeout = false;
    }

    /**
     * Set the content of the task bar according to the given event.
     * 
     * @param event
     * 		Event the task bar is to display information about.
     */
    public void setContent (Event event) {
    	this.event = event;
    	this.strikeout = event.getCompleted();
    	
        dateRangeRendererList.clear();
        //Get the current time.
        Date currentDate = new Date();
        DateRange [] dateRanges = event.getDateRange();
        if ( dateRanges != null ) {
			for (DateRange date : dateRanges) {
				DateRangeRenderer dateRangeRenderer = new DateRangeRenderer();
				//Check if the date range is still relevant as of current time.
				if (date.getStart().after(currentDate) || date.getEnd().after(currentDate)) {
					dateRangeRenderer.setParams(DATERENDERER_X_PADDING_RATIO, DATERENDERER_Y_PADDING_RATIO,
												DATERENDERER_TIMETEXT_WIDTH_RATIO, DATERENDERER_TIMETEXT_HEIGHT_RATIO,
												DATERENDERER_DATETEXT_HEIGHT_RATIO, DATERENDERER_CONNECTOR_WIDTH_RATIO, date);
					dateRangeRendererList.add(dateRangeRenderer);
				} else {
					dateRangeRenderer.setParams(DATERENDERER_X_PADDING_RATIO, DATERENDERER_Y_PADDING_RATIO,
												DATERENDERER_TIMETEXT_WIDTH_RATIO, DATERENDERER_TIMETEXT_HEIGHT_RATIO,
												DATERENDERER_DATETEXT_HEIGHT_RATIO, DATERENDERER_CONNECTOR_WIDTH_RATIO, date);
					//Strike out the date range if it has already passed.
					dateRangeRenderer.strikeout();
					dateRangeRendererList.add(dateRangeRenderer);
				}
			}
		}
    }

    /**
     * Set the parameters required for drawing the task bar.
     * Ratios are with respect to the width of the task view.
     * 
     * @param xPad
     * @param yPad
     * @param nameTextRatio
     */
    public void setParams (double xPad, double yPad, double nameTextRatio) {
        this.xPadding = xPad;
        this.yPadding = yPad;

        this.nameTextRatio = nameTextRatio;
    }
    
    public void strikeout () {
    	this.strikeout = !this.strikeout;
    }

    /**
     * Calculate the height of the task bar.
     * The actual height of the task bar is calculated with respect to the given height.
     * DateRangeRenderer heights are calculated and summed.
     * Padding is included.
     * 
     * @param height
     * 		The initial height of the task bar.
     * @return
     * 		The height of the task bar with date ranges included.
     */
    public double getHeight (double height) {
    	double toReturnHeight = 0;
    	//Add the height of the name of the task bar.
    	toReturnHeight += (height * nameTextRatio);
    	toReturnHeight += yPadding;
    	//Add the height of each date range displayed.
    	for (DateRangeRenderer dateRangeRenderer : dateRangeRendererList) {
    		//Add height of the time display within the DateRangeRenderer.
    		toReturnHeight += (dateRangeRenderer.getTimeTextHeightRatio() * height);
    		//Add height of the date display within the DateRangeRenderer.
    		toReturnHeight += dateRangeRenderer.getDateTextHeightRatio() * (dateRangeRenderer.getTimeTextHeightRatio() * height);
    		toReturnHeight += yPadding;
    	}
    	toReturnHeight += yPadding;
        return toReturnHeight;
    }
    
    /**
     * Render the task bar.
     * 
     * @param gc
     * @param x
     * @param y
     * @param width
     * @param height
     * @param backgroundColour
     * @param textColour
     * @param ellipsize
     * 		Set the type of text renderer to ellipsis or wrap overflow.
     */
    public void draw (GraphicsContext gc, double x, double y,
    					double width, double height,
    					Color backgroundColour, Color textColour, boolean ellipsize) {
        if (gc == null) {
            System.out.println("Error");
        } else {
        	double xCurrent = x;
            double yCurrent = y;
            
        	Font font = FontLoader.load(height * nameTextRatio);
        	
        	if ( strikeout ) {
        		gc.setGlobalAlpha(FADEOUT_ALPHA);
            } else {
            	gc.setGlobalAlpha(DEFAULT_ALPHA);
            }
        	
            gc.setFill(backgroundColour);
            gc.fillRect(xCurrent, yCurrent, width, getHeight(height));
            
            xCurrent += xPadding;
            yCurrent += yPadding;
            
            renderName(gc, xCurrent, yCurrent, width, height,
            			backgroundColour, textColour, font, ellipsize);
            
            if (event.getCategory() != null) {
            	renderCategory(gc, xCurrent, yCurrent, width, height,
            					font, event.getCategory());
            }

            yCurrent += (nameText.getTextHeight() + yPadding);
            for (DateRangeRenderer dateRangeRenderer : dateRangeRendererList) {
            	dateRangeRenderer.draw(gc, xCurrent, yCurrent, width, height,
            							AppColours.panelBackground, backgroundColour);
            	
            	yCurrent += dateRangeRenderer.getTimeTextHeightRatio() * height;
            	yCurrent += dateRangeRenderer.getDateTextHeightRatio() * (dateRangeRenderer.getTimeTextHeightRatio() * height);
            	yCurrent += yPadding;
            }
            gc.setGlobalAlpha(DEFAULT_ALPHA);
        }
    }
    
    /**
     * Render the name of the task bar.
     * 
     * @param gc
     * @param xCurrent
     * @param yCurrent
     * @param width
     * @param height
     * @param backgroundColour
     * @param textColour
     * @param font
     * @param ellipsize
     * 		Set the type of text renderer to ellipsis or wrap overflow.
     */
    private void renderName (GraphicsContext gc, double xCurrent, double yCurrent,
    							double width, double height,
    							Color backgroundColour, Color textColour,
    							Font font, boolean ellipsize ) {
    	nameText.setParams(gc, xCurrent, yCurrent,
			                width * NAME_WIDTH_RATIO, font.getSize(),
			                NAME_X_PADDING, NAME_Y_PADDING, font, NAME_LINE_PADDING_RATIO);
		nameText.clearText();
		if (ellipsize) {
			nameText.addTextEllipsis(event.getName());
		} else {
			nameText.addText(event.getName());
		}
		
		nameText.drawText(backgroundColour, textColour);
    }
    
    /**
     * Render the category display on the task bar.
     * Colour of the category is determined by the hashCode of the string.
     * Modulo of hashCode taken against the number of category colours for wrap-around.
     * 
     * @param gc
     * @param xCurrent
     * @param yCurrent
     * @param width
     * @param height
     * @param font
     * @param category
     */
    private void renderCategory (GraphicsContext gc, double xCurrent, double yCurrent,
    								double width, double height, Font font, String category) {
    	int colourCode = Math.abs(category.hashCode());
    	colourCode %= AppColours.NUM_CATEGORIES;
    	
        gc.setFill(AppColours.category[colourCode]);
        gc.fillRect(xCurrent + (width * NAME_WIDTH_RATIO) - (width * CATEGORY_WIDTH_RATIO * 0.5),
        			yCurrent, (width * CATEGORY_WIDTH_RATIO), font.getSize());
    
        gc.setFill(AppColours.panelBackground);
        gc.setTextAlign(TextAlignment.CENTER);
       	gc.setFont(font);
    	gc.setTextBaseline(VPos.CENTER);
    	
    	gc.fillText(category, xCurrent + (width * NAME_WIDTH_RATIO),
    				yCurrent + (font.getSize() * 0.5));
    }
}
