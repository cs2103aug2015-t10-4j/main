package carelender.view;

import carelender.controller.Controller;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class UserInterfaceController implements Initializable {
    @FXML
    private TextField inputText;

    @FXML
    private StackPane canvasPane;

    private ArrayList<String> messageList;
    ResizableCanvas canvas;

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assert inputText != null : "fx:id=\"inputText\" was not injected: check your FXML file 'userinterface.fxml'.";
        assert canvasPane != null : "fx:id=\"canvasPane\" was not injected: check your FXML file 'userinterface.fxml'.";
        // initialize your logic here: all @FXML variables will have been injected

        //Create canvases using code
        canvas = new ResizableCanvas();
        canvasPane.getChildren().add(canvas);

        // Bind canvas size to stack pane size.
        canvas.widthProperty().bind(
                canvasPane.widthProperty());
        canvas.heightProperty().bind(
                canvasPane.heightProperty());

        Controller.initUserInterfaceController(this);

        Controller.printWelcomeMessage();
        final EventHandler<KeyEvent> keyEventHandler =
                new EventHandler<KeyEvent>() {
                    public void handle(final KeyEvent keyEvent) {
                        if (keyEvent.getEventType() == KeyEvent.KEY_PRESSED) {
                            switch ( keyEvent.getCode() ) {
                                case ENTER:
                                    String text = inputText.getText();
                                    inputText.setText("");
                                    Controller.processUserInput(text);
                                    break;
                                case UP:
                                    Controller.processUpPress();
                                    break;
                                case DOWN:
                                    Controller.processDownPress();
                                    break;
                                default:
                                    Controller.processIncompleteInput(inputText.getText());
                                    break;
                            }

                        }
                    }
                };
        inputText.setOnKeyPressed( keyEventHandler );
    }

    public void setCanvasRenderer ( CanvasRenderer renderer ) {
        canvas.setRenderer(renderer);
    }
    
    public void setMessageList( ArrayList<String> messageList ) {
    	this.messageList = messageList;
    }

    public void clearMessageLog() {
        messageList.clear();
        //refreshOutputField();
    }
    public void displayMessage( String message ) {
        messageList.add(message);
        //refreshOutputField();
    }

    public void setUserInput ( String inputText ) {
        this.inputText.setText(inputText);
    }
    /*
    public void refreshOutputField() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < messageList.size(); i++ ) {
            stringBuilder.append(messageList.get(i));
            stringBuilder.append("\n");
        }
    }
    */
}
