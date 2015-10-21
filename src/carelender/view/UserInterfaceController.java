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

    private UIType uiType;

    private PopupRenderer popupRenderer;
    private MonthViewRenderer monthViewRenderer;
    private WeekViewRenderer weekViewRenderer;
    private SettingViewRenderer settingViewRenderer;

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

        monthViewRenderer = new MonthViewRenderer();
        weekViewRenderer = new WeekViewRenderer();
        settingViewRenderer = new SettingViewRenderer();
        popupRenderer = new PopupRenderer("Hello");

        uiType = UIType.MONTH;
        this.setUI(uiType);
        
        Controller.printWelcomeMessage();
        final EventHandler<KeyEvent> keyEventHandler =
                new EventHandler<KeyEvent>() {
                    public void handle(final KeyEvent keyEvent) {
                        if (keyEvent.getEventType() == KeyEvent.KEY_PRESSED) {
                            switch ( keyEvent.getCode() ) {
                                case ENTER:
                                    String text = inputText.getText();
                                    inputText.setText("");
                                    Controller.processCompleteInput(text);
                                    break;
                                case UP:
                                    Controller.processUpPress();
                                    break;
                                case DOWN:
                                    Controller.processDownPress();
                                    break;
                            }
                        } else if (keyEvent.getEventType() == KeyEvent.KEY_RELEASED) {
                            switch ( keyEvent.getCode() ) {
                                case ENTER:
                                case UP:
                                case DOWN:
                                    break;
                                default:
                                    Controller.processIncompleteInput(inputText.getText());
                                    break;
                            }
                        }
                    }
                };
        inputText.setOnKeyPressed( keyEventHandler );
        inputText.setOnKeyReleased( keyEventHandler );
    }

    
    public void setMessageList( ArrayList<String> messageList ) {
        this.messageList = messageList;
    }

    public void setAutocompleteOptions( String[] autocompleteOptions ) {
        switch ( uiType ) {
            case MONTH:
                monthViewRenderer.setAutocompleteOptions(autocompleteOptions);
                break;
            case WEEK:

                break;

            case SETTING:

                break;
        }
    }

    public void clearMessageLog() {
        messageList.clear();
        refreshOutputField();
    }
    public void displayMessage( String message ) {
        messageList.add(message);
        refreshOutputField();
    }

    public void setUserInput ( String inputText ) {
        this.inputText.setText(inputText);
    }

    public void refreshOutputField() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < messageList.size(); i++) {
            stringBuilder.append(messageList.get(i));
            stringBuilder.append("\n");
        }

        switch ( uiType ) {
            case MONTH:
                monthViewRenderer.setMessageBoxText(stringBuilder.toString());
                break;
            case WEEK:

                break;

            case SETTING:

                break;
        }

    }

    public void setUI(UIType type) {
    	uiType = type;
        switch ( uiType ) {
            case MONTH:
                canvas.setMainRenderer(monthViewRenderer);
                break;
            case WEEK:
                canvas.setMainRenderer(weekViewRenderer);
                break;
            case SETTING:
                canvas.setMainRenderer(settingViewRenderer);
                break;
        }
    }

    /**
     * Changes the UI back to month view if in settings
     * If not toggle between month and week
     */
    public void toggleUI() {
        if ( uiType == UIType.SETTING ) {
            setUI(UIType.MONTH);
        } else {
            if (uiType == UIType.MONTH ) {
                setUI(UIType.WEEK);
            } else {
                setUI(UIType.MONTH);
            }
        }
    }
    
    public void displayPopup(CanvasRenderer popupRenderer){
    	canvas.setPopupRenderer(popupRenderer);
    }
    
    public void cleanPopup(){
    	canvas.setPopupRenderer(null);
    }

    public enum UIType {
        MONTH, WEEK, SETTING
    }

}
