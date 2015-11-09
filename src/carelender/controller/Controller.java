//@@author A0133269A
package carelender.controller;

import carelender.controller.callbacks.OnConfirmedCallback;
import carelender.controller.states.AppState;
import carelender.controller.states.BlockingStateController;
import carelender.controller.states.StateManager;
import carelender.model.AppSettings;
import carelender.model.AppSettings.SettingName;
import carelender.model.data.*;
import carelender.model.strings.FirstStartMessages;
import carelender.view.gui.UIController;
import carelender.view.gui.UIController.UIType;
import carelender.view.parser.DateTimeParser;
import carelender.view.parser.InputParser;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.*;

/**
 * Does all the logic of the application
 */
public class Controller {
    private static UIController UIController = null;
    private static StateManager stateManager;
    private static BlockingStateController blockingStateController;

    //Stores the messages to the user
    //private static ArrayList<String> messageList;
    //Stores the user's inputs
    private static ArrayList<String> commandList;
    private static int currentCommand;
    private static String incompleteInput;

    //Application state
    private static String userName;
    private static UIType defaultUIType;

    private static QueryList currentListQuery;
    private static TimerTask reminder;
    private static Timer timer;
    private static boolean isTimerRunning;

    public static void initialize() throws Exception {
        //messageList = new ArrayList<>();
        commandList = new ArrayList<>();
        stateManager = new StateManager();
        blockingStateController = new BlockingStateController();
        
        //Initialize timer for reminder

        isTimerRunning = false;
        userName = null;
        if(AppSettings.getInstance().getStringSetting(SettingName.USERNAME) != null){
        	userName = AppSettings.getInstance().getStringSetting(SettingName.USERNAME);
        	startTimer();
        	System.out.println("Username: " + userName);
        } else {
            if ( !isTimerRunning ) {
                isTimerRunning = true;
                reminder = new ReminderCaller();
                timer = new Timer();
                timer.scheduleAtFixedRate(reminder, 100000, 5000);
            }
        }
        
        defaultUIType = AppSettings.getInstance().getUITypeSetting(SettingName.DEFAULT_UITYPE);
        System.out.println("Default UIType: " + defaultUIType);
        if (defaultUIType == null) {
            defaultUIType = UIType.TIMELINE;
        }
        
        currentCommand = 0;
    }
    //@@author A0133907E
    public static void stopTimer() {
        if ( isTimerRunning ) {
            timer.cancel();
            isTimerRunning = false;
        }
    }
    public static void startTimer() {
        stopTimer();
        if ( !isTimerRunning ) {
            reminder = new ReminderCaller();
            timer = new Timer();
            timer.scheduleAtFixedRate(reminder, 5000, 5000);
            isTimerRunning = true;
        }
    }

    //@@author A0133269A
    public static void initUserInterfaceController(UIController UIController) {
        Controller.UIController = UIController;

    }

    /**
     * The function that handles the key event from the UIController
     * @param keyEvent KeyEvent from JavaFX
     */
    public static void handleKeyEvent( final KeyEvent keyEvent ) {
        if (keyEvent.getEventType() == KeyEvent.KEY_PRESSED) {
            handleKeyPress(keyEvent.getCode(), keyEvent.isControlDown() || keyEvent.isShortcutDown());
        } else if (keyEvent.getEventType() == KeyEvent.KEY_RELEASED) {
            handleKeyRelease(keyEvent.getCode());
        }
    }

    /**
     * Called by UI while user is typing
     * @param userInput the incomplete user input
     */
    public static void processIncompleteInput(String userInput) {
        incompleteInput = userInput;
        if ( stateManager.getAppState() == AppState.DEFAULT && !blockingStateController.isBlocked() ) {
            StringBuilder stringBuilder = new StringBuilder();
            String [] autocompleteOptions = InputParser.getInstance().getAutocompleteOptions(userInput, stringBuilder);
            UIController.setAutocompleteOptions(autocompleteOptions, stringBuilder.toString());
        } else {
            UIController.setAutocompleteOptions(null, null);
        }

    }

    public static void showHelp() {
        blockingStateController.startPopup(InputParser.getInstance().showCommandList());
    }

    public static void printWelcomeMessage() {
        if ( stateManager.isState(AppState.FIRSTSTART) && userName == null ) {
            UIController.setAnnouncementMessage(FirstStartMessages.firstStart() + FirstStartMessages.askForName());
        } else {
            UIController.setAnnouncementMessage(FirstStartMessages.welcomeBack(userName));
            stateManager.changeState(AppState.DEFAULT);
        }
    }

    /**
     * Refreshes the list of events.
     * It is called after every query the user inputs
     */
    public static void refreshDisplay () {
        if ( currentListQuery == null) {
            currentListQuery = new QueryList();
            currentListQuery.addSearchParam(QueryList.SearchParam.DATE_START, DateTimeParser.getDate(0));
        }

        currentListQuery.controllerExecute();
        UIController.refreshOutputField();
    }

    /**
     * Processes the user input.
     * Called by the UIController class
     * @param userInput The user input string
     */
    public static void processCompleteInput(String userInput) {
        UIController.setAutocompleteOptions(null, null);
        userInput = userInput.trim();
        saveUserCommand(userInput);

        boolean blocked = blockingStateController.processBlockingState(userInput);

        if ( !blocked ) {
            switch ( stateManager.getAppState() ) {
                case FIRSTSTART:
                    stateFirstStart(userInput);
                    break;

                default:
                    stateDefault(userInput);
                    break;
            }
        }
    }

    /**
     * Prints a message to screen
     * @param message message to be displayed
     */
    public static void displayMessage ( String message ) {
        UIController.displayMessage(message);
    }

    public static void displayAnnouncement ( String message ) {
        UIController.setAnnouncementMessage(message);
    }

    public static void displayTasks (EventList events) {
        UIController.setTaskList(events);
    }


    public static void clearMessages () {
        UIController.clearMessageLog();
    }

    public static void setDisplayedList(EventList displayedList) {
        InputParser.getInstance().setDisplayedList(displayedList);
        UIController.setWeekEventList(displayedList);
    }

    public static BlockingStateController getBlockingStateController() {
        return blockingStateController;
    }


    public static UIType getDefaultUIType() {
        return defaultUIType;
    }

    public static UIController getUI() {
        return UIController;
    }

    /**
     * Handles the key press event
     * @param code JavaFX KeyCode
     * @param modifier Is control or command pressed
     */
    private static void handleKeyPress(KeyCode code, boolean modifier) {
        switch ( code ) {
            case ENTER:
                String text = UIController.getInputboxText();
                UIController.setInputboxText("");
                if ( UIController.getPendingAnnouncementMessage() != null ) {
                    stopTimer();
                    UIController.setAnnouncementMessage(UIController.getPendingAnnouncementMessage());
                    UIController.setPendingAnnouncementMessage(null);
                }
                processCompleteInput(text);
                break;
            case UP:
                if ( modifier ) {
                    UIController.taskViewScrollUp();
                } else {
                    processUpPress();
                }
                break;
            case DOWN:
                if ( modifier ) {
                    UIController.taskViewScrollDown();
                } else {
                    processDownPress();
                }
                break;
            case LEFT:
                if ( modifier ) {
                    UIController.timelineScrollLeft();
                }
                break;
            case RIGHT:
                if ( modifier ) {
                    UIController.timelineScrollRight();
                }
                break;
            case TAB:
                UIController.autocompleteSuggestion();
                break;
            case F1:
                UIController.setUI(UIType.TIMELINE);
                break;
            case F2:
                UIController.setUI(UIType.CALENDAR);
                break;
            case F3:
                UIController.setUI(UIType.FLOATING);
                break;
            case F4:
                UIController.setUI(UIType.SETTING);
                break;
            case PAGE_UP:
                UIController.taskViewScrollUp();
                break;
            case PAGE_DOWN:
                UIController.taskViewScrollDown();
                break;
            case HOME:
                UIController.timelineScrollLeft();
                break;
            case END:
                UIController.timelineScrollRight();
                break;
            case F12:
                startTimer();
                break;
            default:
                break;
        }
    }

    /**
     * Handles the key release event
     * @param code JavaFX KeyCode
     */
    private static void handleKeyRelease ( KeyCode code ) {
        switch ( code ) {
            case ENTER:
            case UP:
            case DOWN:
                break;
            case ALT:
                UIController.getAutomatedCommand(false);
                break;
            case CONTROL:
                UIController.getAutomatedCommand(true);
                break;
            default:
                processIncompleteInput(UIController.getInputboxText());
                break;
        }
    }

    /**
     * Called by UI when up key is pressed
     */
    private static void processUpPress() {
        if (currentCommand < commandList.size() - 1){
            currentCommand++;
        }
        showPreviousCommand();
    }

    /**
     * Called by UI when down key is pressed
     */
    private static void processDownPress() {
        if ( currentCommand >= 0 ) {
            currentCommand--;
        }
        showPreviousCommand();
    }

    /**
     * Shows any previous command based on the currentCommand variable
     */
    private static void showPreviousCommand() {
        if ( currentCommand == -1 ) { //Index -1 is an empty command
            UIController.setUserInput(incompleteInput);
        } else {
            int commandIndex = commandList.size() - currentCommand - 1;
            if (commandIndex < 0 || commandIndex >= commandList.size()) {
                return;
            }
            UIController.setUserInput(commandList.get(commandIndex));
        }
    }

    /**
     * Saves the user's command. Removes duplicates and empty commands
     * @param userInput User input to save
     */
    private static void saveUserCommand ( String userInput ) {
        if ( userInput.length() > 0 ) {
            int index = commandList.indexOf(userInput);
            if ( index >= 0 ) commandList.remove(index);
            commandList.add(userInput);
            currentCommand = -1;
        }
    }



    private static void stateFirstStart ( String userInput ) {
    	final OnConfirmedCallback confirmNameCallback = (boolean confirmed) -> {
                System.out.println("Confirmed: " + confirmed);
                if ( confirmed ) {
                    displayMessage(FirstStartMessages.confirmed(userName));
                    stateManager.changeState(AppState.DEFAULT);
                    AppSettings.getInstance().setStringSetting(SettingName.USERNAME, userInput);
                    refreshAnnoucementBox();
                    refreshDisplay();
                } else {
                    displayMessage(FirstStartMessages.askForNameAgain());
                    userName = null;
                }
            };
        userName = userInput;
        blockingStateController.startConfirmation(FirstStartMessages.confirmation(userName), confirmNameCallback);
    }

    private static void stateDefault(String userInput) {
        if ( userInput.length() == 0 ) {
            return;
        }
        Controller.clearMessages();
        QueryBase query = InputParser.getInstance().parseCompleteInput(userInput);
        query.userInput = userInput;
        
        switch (query.getQueryType()) {
            case DATETEST:
                DateRange[] dateRanges = DateTimeParser.parseDateTime(userInput);
                displayMessage( "User input: [" + userInput + "]" );
                displayMessage("Matched " + dateRanges.length + " dates");

                for(DateRange range:dateRanges) {
                    displayMessage("   " + range.toString());
                }
                break;
            case LIST:
                currentListQuery = (QueryList)query;
                break;
            case EXIT:
                Controller.stopTimer();
                System.exit(0);
                break;
            default:
                query.controllerExecute();
                break;
        }
        refreshDisplay();
    }


    //@@author A0133907E
    /**
     * Refreshes the announcement box
     */
	private static void refreshAnnoucementBox() {
		String firstHint = "I'll be giving you some helpful information here!";
		Controller.displayAnnouncement(firstHint);
	}
    

}
