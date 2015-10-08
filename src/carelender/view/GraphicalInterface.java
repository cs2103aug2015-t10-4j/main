package carelender.view;

import carelender.controller.Controller;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GraphicalInterface implements Initializable {
    @FXML
    private TextArea outputText;
    @FXML
    private TextField inputText;

    private ArrayList<String> messageList;

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assert outputText != null : "fx:id=\"outputText\" was not injected: check your FXML file 'simple.fxml'.";
        assert inputText != null : "fx:id=\"inputText\" was not injected: check your FXML file 'simple.fxml'.";
        // initialize your logic here: all @FXML variables will have been injected

        Controller.initGraphicalInterface(this);
        
        Controller.printWelcomeMessage();
        final EventHandler<KeyEvent> keyEventHandler =
                new EventHandler<KeyEvent>() {
                    public void handle(final KeyEvent keyEvent) {
                        if (keyEvent.getEventType() == KeyEvent.KEY_PRESSED) {
                            if (keyEvent.getCode() == KeyCode.ENTER) {
                                String text = inputText.getText();
                                inputText.setText("");
                                Controller.processUserInput(text);
                            }
                        }
                    }
                };
        inputText.setOnKeyPressed( keyEventHandler );
    }
    
    public void setMessageList( ArrayList<String> messageList ) {
    	this.messageList = messageList;
    }

    public void clearMessageLog() {
        messageList.clear();
        refreshOutputField();
    }
    public void displayMessage( String message ) {
        messageList.add(message);
        refreshOutputField();
    }
    public void refreshOutputField() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < messageList.size(); i++ ) {
            stringBuilder.append(messageList.get(i));
            stringBuilder.append("\n");
        }
        outputText.setText(stringBuilder.toString());
        outputText.setScrollTop(Double.MAX_VALUE);
    }

}
