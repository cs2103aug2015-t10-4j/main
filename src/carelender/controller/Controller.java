package carelender.controller;

import carelender.controller.callbacks.OnConfirmedCallback;
import carelender.controller.states.AppState;
import carelender.controller.states.BlockingStateController;
import carelender.controller.states.StateManager;
import carelender.model.AppSettings;
import carelender.model.AppSettings.SettingName;
import carelender.model.data.*;
import carelender.model.strings.FirstStartMessages;
import carelender.view.gui.UserInterfaceController;
import carelender.view.gui.UserInterfaceController.UIType;
import carelender.view.parser.DateTimeParser;
import carelender.view.parser.InputParser;

import java.util.*;

/**
 * Does all the logic of the application
 */
public class Controller {
    private static UserInterfaceController userInterfaceController = null;
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


    public static void initUserInterfaceController(UserInterfaceController userInterfaceController) {
        Controller.userInterfaceController = userInterfaceController;
        //Controller.userInterfaceController.setMessageList(messageList);
        /*canvasRenderer = new MonthViewRenderer();
        Controller.userInterfaceController.setCanvasRenderer(canvasRenderer);*/
    }


    /**
     * Called by UI when up key is pressed
     */
    public static void processUpPress() {
        if (currentCommand < commandList.size() - 1){
            currentCommand++;
        }
        showPreviousCommand();
    }

    /**
     * Called by UI when down key is pressed
     */
    public static void processDownPress() {
        if ( currentCommand >= 0 ) {
            currentCommand--;
        }
        showPreviousCommand();
    }
    
    /**
     * Called by UI when page down key is pressed
     */
    public static void processPageDownPress() {
        userInterfaceController.processPageDownPress();
    }

    /**
     * Called by UI when page up key is pressed
     */
    public static void processPageUpPress() {
    	userInterfaceController.processPageUpPress();
    }

    /**
     * Shows any previous command based on the currentCommand variable
     */
    private static void showPreviousCommand() {
        if ( currentCommand == -1 ) { //Index -1 is an empty command
            userInterfaceController.setUserInput(incompleteInput);
        } else {
            int commandIndex = commandList.size() - currentCommand - 1;
            if (commandIndex < 0 || commandIndex >= commandList.size()) {
                return;
            }
            userInterfaceController.setUserInput(commandList.get(commandIndex));
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

    /**
     * Called by UI while user is typing
     * @param userInput the incomplete user input
     */
    public static void processIncompleteInput(String userInput) {
        incompleteInput = userInput;
        if ( stateManager.getAppState() == AppState.DEFAULT && !blockingStateController.isBlocked() ) {
            StringBuilder stringBuilder = new StringBuilder();
            String [] autocompleteOptions = InputParser.getInstance().getAutocompleteOptions(userInput, stringBuilder);
            if ( autocompleteOptions != null ) {
                //System.out.println("Autocomplete size: " + autocompleteOptions.length);
            } else {
                //System.out.println("No match");
            }
            userInterfaceController.setAutocompleteOptions(autocompleteOptions, stringBuilder.toString());
        } else {
            userInterfaceController.setAutocompleteOptions(null, null);
        }

    }

    /**
     * Processes the user input.
     * Called by the UserInterfaceController class
     * @param userInput The user input string
     */
    public static void processCompleteInput(String userInput) {
        userInterfaceController.setAutocompleteOptions(null, null);
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

    private static void stateFirstStart ( String userInput ) {
    	final OnConfirmedCallback confirmNameCallback = new OnConfirmedCallback() {
            @Override
            public void onConfirmed(boolean confirmed) {
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

    /**
     * Converts an add query to an event object
     * @param queryAdd
     * @return
     */
    public static Event queryAddToEventObject(QueryAdd queryAdd) {
        Event eventObj = new Event(0, queryAdd.getName(), queryAdd.getDateRange(), queryAdd.getCategory());
        return eventObj;
    }



    public static void showHelp() {
        //userInterfaceController.displayMessage("Available Commands:");
        //userInterfaceController.displayMessage(InputParser.getInstance().showCommandList());
        blockingStateController.startPopup(InputParser.getInstance().showCommandList());
    }

    public static void printWelcomeMessage() {
        if ( stateManager.isState(AppState.FIRSTSTART) && userName == null ) {
            userInterfaceController.setAnnouncementMessage("Welcome to careLender. " + FirstStartMessages.askForName());
        } else {
            userInterfaceController.setAnnouncementMessage("Welcome back, " + userName);
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
        userInterfaceController.refreshOutputField();
    }

    /**
     * Refreshes the announcement box
     */
	private static void refreshAnnoucementBox() {
		String firstHint = "I'll be giving you some helpful information here!";
		Controller.displayAnnouncement(firstHint);
	}
    
    /**
     * Prints a message to screen
     * @param message message to be displayed
     */
    public static void displayMessage ( String message ) {
        userInterfaceController.displayMessage(message);
    }

    public static void displayAnnouncement ( String message ) {
        userInterfaceController.setAnnouncementMessage(message);
    }

    public static void displayTasks (EventList events) {
        userInterfaceController.setTaskList(events);
    }


    public static void clearMessages () {
        userInterfaceController.clearMessageLog();
    }

    public static void setDisplayedList(EventList displayedList) {
        InputParser.getInstance().setDisplayedList(displayedList);
        userInterfaceController.setWeekEventList(displayedList);
    }

    public static BlockingStateController getBlockingStateController() {
        return blockingStateController;
    }

    public static UserInterfaceController getGUI() {
    	return userInterfaceController;
    }

    public static void processTabPress() {
        userInterfaceController.processTabPress();
    }

	public static UIType getDefaultUIType() {
		return defaultUIType;
	}
}
