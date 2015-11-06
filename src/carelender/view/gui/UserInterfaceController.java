package carelender.view.gui;

import carelender.controller.Controller;
import carelender.model.CommandAutomation;
import carelender.model.data.Event;
import carelender.model.data.EventList;
import carelender.model.data.QueryList;
import carelender.view.gui.components.EventInfoRenderer;
import carelender.view.gui.components.PopupRenderer;
import carelender.view.gui.views.FloatingViewRenderer;
import carelender.view.gui.views.MonthViewRenderer;
import carelender.view.gui.views.SettingViewRenderer;
import carelender.view.gui.views.TimelineViewRenderer;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;

import javax.naming.ldap.Control;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;

public class UserInterfaceController implements Initializable {
    @FXML
    private TextField inputText;

    @FXML
    private StackPane canvasPane;

    public ArrayList<String> messageList;
    ResizableCanvas canvas;

    private UIType uiType;


    private MonthViewRenderer monthViewRenderer;
    private TimelineViewRenderer timelineViewRenderer;
    private FloatingViewRenderer floatingViewRenderer;
    private SettingViewRenderer settingViewRenderer;


    private PopupRenderer popupRenderer;
    private EventInfoRenderer eventInfoRenderer;
    private UserInterfaceRenderer userInterfaceRenderer;
    private String firstOption;

    private String pendingAnnouncementMessage = null; //Used for the automation part

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assert inputText != null : "fx:id=\"inputText\" was not injected: check your FXML file 'userinterface.fxml'.";
        assert canvasPane != null : "fx:id=\"canvasPane\" was not injected: check your FXML file 'userinterface.fxml'.";
        // initialize your logic here: all @FXML variables will have been injected

        //Create canvases using code
        canvas = new ResizableCanvas();
        canvasPane.getChildren().add(canvas);

        popupRenderer = new PopupRenderer();
        messageList = new ArrayList<>();

        userInterfaceRenderer = new UserInterfaceRenderer();
        canvas.setRenderer(userInterfaceRenderer);

        // Bind canvas size to stack pane size.
        canvas.widthProperty().bind(
                canvasPane.widthProperty());
        canvas.heightProperty().bind(
                canvasPane.heightProperty());

        Controller.initUserInterfaceController(this);

        monthViewRenderer = new MonthViewRenderer();
        timelineViewRenderer = new TimelineViewRenderer();
        settingViewRenderer = new SettingViewRenderer();
        floatingViewRenderer = new FloatingViewRenderer();

        eventInfoRenderer = new EventInfoRenderer();


        uiType = Controller.getDefaultUIType();
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
                                    if ( pendingAnnouncementMessage != null ) {
                                        Controller.stopTimer();
                                        setAnnouncementMessage(pendingAnnouncementMessage);
                                        pendingAnnouncementMessage = null;
                                    }
                                    Controller.processCompleteInput(text);
                                    break;
                                case UP:
                                    Controller.processUpPress();
                                    break;
                                case DOWN:
                                    Controller.processDownPress();
                                    break;
                                case TAB:
                                    Controller.processTabPress();
                                    break;
                                case F1:
                                    setUI(UIType.TIMELINE);
                                    break;
                                case F2:
                                    setUI(UIType.CALENDAR);
                                    break;
                                case F3:
                                    setUI(UIType.FLOATING);
                                    break;
                                case F4:
                                    setUI(UIType.SETTING);
                                    break;

                                case F12:
                                    Controller.startTimer();
                                    break;
                                default:
                                    break;
                            }
                        } else if (keyEvent.getEventType() == KeyEvent.KEY_RELEASED) {
                            switch ( keyEvent.getCode() ) {
                                case ENTER:
                                case UP:
                                case DOWN:
                                    break;
                                case ALT:
                                    getAutomatedCommand(false);
                                    break;
                                case CONTROL:
                                    getAutomatedCommand(true);
                                    break;
                                case PAGE_UP:
                                    Controller.processPageUpPress();
                                    break;
                                case PAGE_DOWN:
                                    Controller.processPageDownPress();
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

        //Create initial list command
        Calendar calendar = Calendar.getInstance();
        QueryList list = new QueryList();
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        list.addSearchParam(QueryList.SearchParam.DATE_START, calendar.getTime());
        list.controllerExecute();
    }

    public void getAutomatedCommand(boolean prev) {
        String next;
        if ( prev ) {
            next = CommandAutomation.getInstance().getPrev();
        } else {
            next = CommandAutomation.getInstance().getNext();
        }
        if ( next == null ) {
            return;
        }
        String [] parts = next.split(" // ");
        if ( parts.length == 2 ) {
            pendingAnnouncementMessage = parts[1];
        }
        setUserInput(parts[0]);
        Controller.processIncompleteInput(inputText.getText());
    }

    /**
     * Called by UI when page down key is pressed
     */
    public void processPageDownPress() {
        switch ( uiType ) {
            case TIMELINE:
                timelineViewRenderer.getTaskRenderer().scrollUp();
                break;
            case CALENDAR:
                monthViewRenderer.getTaskRenderer().scrollUp();
                break;
            case FLOATING:
                floatingViewRenderer.getTaskRenderer().scrollUp();
                break;
        }
    }

    /**
     * Called by UI when page up key is pressed
     */
    public void processPageUpPress() {
        switch ( uiType ) {
            case TIMELINE:
                timelineViewRenderer.getTaskRenderer().scrollDown();
                break;
            case CALENDAR:
                monthViewRenderer.getTaskRenderer().scrollDown();
                break;
            case FLOATING:
                floatingViewRenderer.getTaskRenderer().scrollDown();
                break;
        }
    }

    /**
     * Processes the tab press, fills in the highlighted autocomplete item.
     */
    public void processTabPress() {
        if ( firstOption != null && firstOption.length() > 0 ) {
            setUserInput(firstOption + " ");
            Controller.processIncompleteInput(inputText.getText());
        }
    }

    public void setTaskList ( EventList events ) {
        monthViewRenderer.setTaskview(events);
        timelineViewRenderer.setTaskview(events);
        floatingViewRenderer.setTaskview();
        updateTaskList();
    }

    /**
     * Updates the list that the parser holds so it knows what ids to use
     */
    public void updateTaskList() {
        switch ( uiType ) {
            case TIMELINE:
                timelineViewRenderer.setTaskDisplayList();
                break;
            case CALENDAR:
                monthViewRenderer.setTaskDisplayList();
                break;
            case FLOATING:
                floatingViewRenderer.setTaskDisplayList();
                break;
        }
    }

    public void setWeekEventList ( EventList events ) {
        timelineViewRenderer.setWeekView(events);
    }


    public void setAutocompleteOptions( String[] autocompleteOptions, String firstOption ) {
        this.firstOption = firstOption;
        boolean renderFirstLineBold = false;
        if ( firstOption != null && firstOption.length() > 0 ) {
            renderFirstLineBold = true;
        }
        userInterfaceRenderer.setAutocompleteOptions(autocompleteOptions, renderFirstLineBold);
        refresh();
    }

    /**
     * Sets the message in the announcement box if any
     * @param message Message to show
     */
    public void setAnnouncementMessage ( String message ) {
        monthViewRenderer.setAnnouncementBoxText(message);
        timelineViewRenderer.setAnnouncementBoxText(message);
        floatingViewRenderer.setAnnouncementBoxText(message);
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
        this.inputText.positionCaret(inputText.length());
    }

    public void refreshOutputField() {
        System.out.println("Refreshing messages");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < messageList.size(); i++) {
            stringBuilder.append(messageList.get(i));
            stringBuilder.append("\n");
        }

        switch ( uiType ) {
            case CALENDAR:
                monthViewRenderer.setMessageBoxText(stringBuilder.toString());
                monthViewRenderer.refreshData();
                break;
            case TIMELINE:
                timelineViewRenderer.setMessageBoxText(stringBuilder.toString());
                timelineViewRenderer.refreshData();
                break;
            case FLOATING:
                floatingViewRenderer.setMessageBoxText(stringBuilder.toString());
                floatingViewRenderer.refreshData();
                break;
            case SETTING:
                break;
        }
        refresh();
    }

    public void refresh() {
        userInterfaceRenderer.redraw();
    }

    public void setUI(UIType type) {
        uiType = type;
        switch ( uiType ) {
            case CALENDAR:
                userInterfaceRenderer.setMainRenderer(monthViewRenderer);
                break;
            case TIMELINE:
                userInterfaceRenderer.setMainRenderer(timelineViewRenderer);
                break;
            case FLOATING:
                userInterfaceRenderer.setMainRenderer(floatingViewRenderer);
                break;
            case SETTING:
                userInterfaceRenderer.setMainRenderer(settingViewRenderer);
                break;
        }
        updateTaskList();
        refresh();
    }

    /**
     * Changes the UI back to month view if in settings
     * If not toggle between month and week
     */
    public void toggleUI() {
        if ( uiType == UIType.SETTING ) {
            setUI(UIType.CALENDAR);
        } else {
            if (uiType == UIType.CALENDAR) {
                setUI(UIType.TIMELINE);
            } else {
                setUI(UIType.CALENDAR);
            }
        }
    }

    public void displayPopup( String message ) {
        popupRenderer.setMessage(message);
        userInterfaceRenderer.setPopupRenderer(popupRenderer);
        refresh();
    }

    public void displayEventPopup( Event event ) {
        eventInfoRenderer.setEvent(event);
        userInterfaceRenderer.setPopupRenderer(eventInfoRenderer);
        refresh();
    }

    public void clearPopup(){
        userInterfaceRenderer.setPopupRenderer(null);
        refresh();
    }


    public enum UIType {
        CALENDAR, TIMELINE, FLOATING, SETTING
    }

}
