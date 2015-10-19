package carelender.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Created by JiaXun on 14/10/2015.
 */
public class CalenderRenderer extends CanvasRenderer {
    int squaresToDraw; //Temp, testing purposes only
    public CalenderRenderer() {
        squaresToDraw = 14;
    }

    double sidePadding;
    double calCellWidth;
    double calCellHeight;
    double calCellSpacing;
    double calCellShadowOffset;

    double scaledWidth, scaledHeight;
    double offsetX, offsetY;

    @Override
    public void draw(GraphicsContext gc, double width, double height) {
        super.draw(gc,width,height);
        calculateScaledDimensions(width, height);
        calulateCellProperties();

        Font font = Font.loadFont("file:res/monaco.ttf", calCellHeight / 3.0);
        for (int i = 0; i < squaresToDraw; i++ ) {
            double actualX = i%7 * ( calCellWidth + calCellSpacing ) + sidePadding;
            double actualY = (i/7) * ( calCellHeight + calCellSpacing ) + sidePadding;
            actualX += offsetX;
            actualY += offsetY;
            RenderHelper.calendarSquare(gc, actualX, actualY,
                                        calCellWidth, calCellHeight,
                                        calCellShadowOffset, "F99", (i+1)+"", font);
        }
        
        TextRenderer textTest = new TextRenderer (gc, sidePadding + offsetX, sidePadding + offsetY,
                    scaledWidth * 0.6 , scaledHeight * 0.6 , 10, 10,
                font, calCellHeight / 3.0, calCellHeight / 6.0, 0 );
        textTest.addText("This is a test string for like, stuff and stuff.\n");
        textTest.addText("Give me the thing that I love.\n");
        textTest.addText("Do I really wrap? Is this how a burrito feels like. The twice fried beans, the painted faces.\n");
        
        textTest.drawText();
    }

    private void calculateScaledDimensions(double width, double height) {
        double aspect = 16.0/9.0;
        double squareHeight = height * aspect;

        if ( width > squareHeight ) { //Height is the constraint
            scaledWidth = height * aspect;
            scaledHeight = height;
            System.out.println("Height constraint");
        } else { //Width is the constraint
            scaledWidth = width;
            scaledHeight = width / aspect;
            System.out.println("Width constraint");
        }

        System.out.println("Width : " + scaledWidth + "/" + width);
        System.out.println("Height: " + scaledHeight + "/" + height);

        offsetX = (width - scaledWidth) * 0.5;
        offsetY = (height - scaledHeight) * 0.5;
    }

    private void calulateCellProperties() {
        sidePadding = scaledWidth * 0.05;
        double usableWidth = scaledWidth * 0.95; // Give 2.5% padding on each size
        calCellWidth = usableWidth / 7.0;
        calCellSpacing = calCellWidth * 0.1; //Make spacing 10% of each cell size
        calCellWidth -= calCellSpacing;
        calCellHeight = calCellWidth ;
        calCellShadowOffset = calCellSpacing * 0.7;
    }

    private void redCross() {
        gc.setStroke(Color.RED);

        gc.clearRect(0, 0, width, height);
        gc.strokeLine(0, 0, width, height);
        gc.strokeLine(0, height, width, 0);
    }

    public void increment() {
        squaresToDraw++;
        
        redraw();
    }
}
