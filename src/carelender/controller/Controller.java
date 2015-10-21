package carelender.controller;

import carelender.controller.callbacks.OnConfirmedCallback;
import carelender.controller.states.AppState;
import carelender.controller.states.BlockingStateController;
import carelender.controller.states.StateManager;
import carelender.model.AppSettings;
import carelender.model.AppSettings.SettingName;
import carelender.model.data.*;
import carelender.model.strings.FirstStartMessages;
import carelender.view.MonthViewRenderer;
import carelender.view.UserInterfaceController;
import carelender.view.parser.DateTimeParser;
import carelender.view.parser.InputParser;
import jdk.internal.util.xml.impl.Input;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Does all the logic of the application
 */
public class Controller {
    private static boolean debugMode = true;

    private static UserInterfaceController userInterfaceController = null;
    private static StateManager stateManager;
    private static BlockingStateController blockingStateController;

    //Stores the messages to the user
    private static ArrayList<String> messageList;
    //Stores the user's inputs
    private static ArrayList<String> commandList;
    private static int currentCommand;
    private static String incompleteInput;

    //Application state
    private static String userName;

    private static QueryList currentListQuery;





    public static void initialize() throws Exception {
        messageList = new ArrayList<>();
        commandList = new ArrayList<>();
        stateManager = new StateManager();
        blockingStateController = new BlockingStateController();
        
        //Initialize timer for reminder
        TimerTask reminder = new Reminder();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(reminder,500,1000);
        
        userName = null;
        if(AppSettings.getInstance().getStringSetting(SettingName.USERNAME) != null){
        	userName = AppSettings.getInstance().getStringSetting(SettingName.USERNAME);
        	System.out.println("Username: " + userName);
        }
        currentCommand = 0;
    }


    public static void initUserInterfaceController(UserInterfaceController userInterfaceController) {
        Controller.userInterfaceController = userInterfaceController;
        Controller.userInterfaceController.setMessageList(messageList);
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
            String [] autocompleteOptions = InputParser.getInstance().getAutocompleteOptions(userInput);
            if ( autocompleteOptions != null ) {
                //System.out.println("Autocomplete size: " + autocompleteOptions.length);
            } else {
                //System.out.println("No match");
            }
            userInterfaceController.setAutocompleteOptions(autocompleteOptions);
        } else {
            userInterfaceController.setAutocompleteOptions(null);
        }

    }

    /**
     * Processes the user input.
     * Called by the UserInterfaceController class
     * @param userInput The user input string
     */
    public static void processCompleteInput(String userInput) {
        userInterfaceController.setAutocompleteOptions(null);
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
            case DEV1:
                //canvasRenderer.increment();
                break;
            case DEV2:
                break;
            case LIST:
                currentListQuery = (QueryList)query;
                refreshDisplay();
                break;
            case EXIT:
                System.exit(0);
                break;
            default:
                query.controllerExecute();
                refreshDisplay();
                break;
        }
    }

    /**
     * Converts an add query to an event object
     * @param queryAdd
     * @return
     */
    public static Event queryAddToEventObject(QueryAdd queryAdd) {
        Event eventObj = new Event(0, queryAdd.getName(), queryAdd.getDateRange());
        return eventObj;
    }



    public static void showHelp() {
        userInterfaceController.displayMessage("Available Commands:");
        userInterfaceController.displayMessage(InputParser.getInstance().showCommandList());
    }

    public static void printWelcomeMessage() {
        if ( stateManager.isState(AppState.FIRSTSTART) && userName == null ) {
            userInterfaceController.displayMessage("CareLender: Maybe the best task manager in the world.");
            userInterfaceController.displayMessage(FirstStartMessages.askForName());
        } else {
            userInterfaceController.displayMessage("Welcome back, " + userName);
            stateManager.changeState(AppState.DEFAULT);
        }
    }

    /**
     * Refreshes the list of events.
     * It is called after every query the user inputs
     */
    private static void refreshDisplay () {
        if ( currentListQuery == null) {
            currentListQuery = new QueryList();
            currentListQuery.addSearchParam(QueryList.SearchParam.DATE_START, DateTimeParser.getDate(0));
        }

        currentListQuery.controllerExecute();
    }

    /**
     * Prints a message to screen
     * @param message message to be displayed
     */
    public static void displayMessage ( String message ) {
        userInterfaceController.displayMessage(message);
    }

    public static void clearMessages ( ) {
        userInterfaceController.clearMessageLog();
    }
    /**
     * Prints a message to the screen only in debug mode.
     * @param message message to be displayed
     */
    public static void printDebugMessage ( String message ) {
        if ( debugMode ) {
            userInterfaceController.displayMessage(message);
        }
        System.out.println(message);
    }

    public static void setDisplayedList(EventList displayedList) {
        InputParser.getInstance().setDisplayedList(displayedList);
    }

    public static BlockingStateController getBlockingStateController() {
        return blockingStateController;
    }

    public static UserInterfaceController getGUI() {
    	return userInterfaceController;
    }
}
