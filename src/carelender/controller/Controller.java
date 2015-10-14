package carelender.controller;

import carelender.controller.callbacks.OnConfirmedCallback;
import carelender.controller.callbacks.OnEventSelectedCallback;
import carelender.controller.states.AppState;
import carelender.controller.states.BlockingStateController;
import carelender.controller.states.StateManager;
import carelender.model.AppSettings;
import carelender.model.Model;
import carelender.model.data.*;
import carelender.model.strings.FirstStartMessages;
import carelender.view.CalenderRenderer;
import carelender.view.CanvasRenderer;
import carelender.view.UserInterfaceController;
import carelender.view.parser.DateTimeParser;
import carelender.view.parser.InputParser;

import com.joestelmach.natty.*;
import com.joestelmach.natty.generated.DateParser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Does all the logic of the application
 */
public class Controller {
    private static boolean debugMode = true;

    private static UserInterfaceController userInterfaceController = null;
    private static Search search = null;
    private static InputParser inputParser = null;
    private static AppSettings appSettings = null;
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

    private static CalenderRenderer canvasRenderer;



    public static void initialize() throws Exception {
        search = new Search();
        appSettings = new AppSettings();
        inputParser = new InputParser();
        messageList = new ArrayList<>();
        commandList = new ArrayList<>();
        stateManager = new StateManager();
        blockingStateController = new BlockingStateController();
        
        //Initialize timer for reminder
        TimerTask reminder = new Reminder();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(reminder,500,1000);

        userName = null;
        currentCommand = 0;
    }

    public static void initUserInterfaceController(UserInterfaceController userInterfaceController) {
        Controller.userInterfaceController = userInterfaceController;
        Controller.userInterfaceController.setMessageList(messageList);
        canvasRenderer = new CalenderRenderer();
        Controller.userInterfaceController.setCanvas1Renderer(canvasRenderer);
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
     * Called by UI while user is typing
     * @param userInput the incomplete user input
     */
    public static void processIncompleteInput(String userInput) {
        incompleteInput = userInput;
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
     * Processes the user input.
     * Called by the UserInterfaceController class
     * @param userInput The user input string
     */
    public static void processUserInput(String userInput) {
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
                } else {
                    displayMessage(FirstStartMessages.askForNameAgain());
                    userName = null;
                }
            }
        };
        //TODO: Save user's name to memory

        userName = userInput;
        blockingStateController.startConfirmation(FirstStartMessages.confirmation(userName), confirmNameCallback);
    }


    private static void stateDefault(String userInput) {
        if ( userInput.length() == 0 ) return;
        QueryBase query = inputParser.parseCompleteInput(userInput);

        switch (query.getQueryType()) {
            case ERROR:
                processError((QueryError) query);
                break;
            case HELP:
                showHelp();
                break;
            case CLEAR:
                userInterfaceController.clearMessageLog();
                break;
            case ADD:
                processAdd((QueryAdd) query);
                break;
            case DELETE:
                processDelete((QueryDelete) query);
                break;
            case UPDATE:
                processUpdate((QueryUpdate) query);
                break;
            case LIST:
                processList( (QueryList) query);
                break;

            case DATETEST:
                DateRange[] dateRanges = DateTimeParser.parseDateTime(userInput);
                displayMessage( "User input: [" + userInput + "]" );
                displayMessage("Matched " + dateRanges.length + " dates");

                for(DateRange range:dateRanges) {
                    displayMessage("   " + range.toString());
                }
                break;

            case DEV1:
                canvasRenderer.increment();
                break;
            case DEV2:

                break;

            case SWITCHUI:
                processSwitchUI();
                break;
            default:
                userInterfaceController.displayMessage("Command accepted.");
                break;
        }
    }

    private static void processSwitchUI () {
        displayMessage("Switching UI");
    }

    private static void processDelete ( QueryDelete queryDelete ) {
        //TODO: Actually delete something
        EventList searchResults = search.parseQuery(queryDelete);

        final OnEventSelectedCallback deleteCallback = new OnEventSelectedCallback() {
            @Override
            public void onChosen(EventObject selected) {
                displayMessage ( "Deleting [" + selected.getInfo() + "]" );
            }
        };

        if ( searchResults.size() == 0 ) {
            displayMessage("There is no task called " + queryDelete.getName() );
        } else if ( searchResults.size() > 1 ) {
            String message = "There are multiple \""+queryDelete.getName()+"\" tasks, please choose the one to delete.";
            blockingStateController.startEventSelection(message, searchResults, deleteCallback);
        } else {
            deleteCallback.onChosen(searchResults.get(0));
        }
    }
    
    private static void processUpdate ( QueryUpdate queryUpdate ) {
        //TODO: Actually update something
        EventList searchResults = search.parseQuery(queryUpdate);
        for ( EventObject event : searchResults ) {
            //TODO: This will have to change if we want to do bulk updating.
            HashMap<QueryUpdate.UpdateParam, Object> paramList = queryUpdate.getUpdateParamsList();

            if ( paramList.containsKey(QueryUpdate.UpdateParam.NAME) ) {
                String fromName = (String)paramList.get(QueryUpdate.UpdateParam.NAME);
                event.setName(fromName);
            }

            if ( paramList.containsKey(QueryUpdate.UpdateParam.DATE_RANGE) ) {
                DateRange[] fromDateRange = (DateRange[])paramList.get(QueryUpdate.UpdateParam.DATE_RANGE);
                event.setDateRange(fromDateRange);
            }

            //Call Model updateEvent function
            //this.model.updateEvent ( event );
            System.out.println ( event.getName() );
        }
    }

    private static void processList ( QueryList queryList ) {
        EventList searchResults = search.parseQuery(queryList);
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("E dd MMM yyyy h:mma Z");
        displayMessage("Listing events");
        HashMap<QueryList.SearchParam, Object> paramList = queryList.getSearchParamsList();
        if ( paramList.containsKey(QueryList.SearchParam.DATE_START) ) {
            Date fromDate = (Date)paramList.get(QueryList.SearchParam.DATE_START);
            displayMessage("   from " + dateFormat.format(fromDate));
        }
        if ( paramList.containsKey(QueryList.SearchParam.DATE_END) ) {
            Date toDate = (Date)paramList.get(QueryList.SearchParam.DATE_END);
            displayMessage("   till " + dateFormat.format(toDate));
        }
        if ( paramList.containsKey(QueryList.SearchParam.NAME_CONTAINS) ) {
            String search = (String)paramList.get(QueryList.SearchParam.NAME_CONTAINS);
            displayMessage("   matching: " + search);
        }
        
        if (searchResults.size() > 0) {
            displayMessage(searchResults.toString());
        } else {
            displayMessage("No results");
        }
    }

    private static void processAdd(QueryAdd queryAdd ) {
        DateRange [] dateRanges = queryAdd.getDateRange();
        displayMessage("Adding new task: ["+queryAdd.getName()+"] at ");
        for ( DateRange dateRange : dateRanges ) {
            displayMessage( "    " + dateRange.toString() );
        }

        /*QueryList checkClashesQuery = new QueryList();
        checkClashesQuery.addSearchParam(QueryList.SearchParam.DATE_START, queryAdd.getTime());
        checkClashesQuery.addSearchParam(QueryList.SearchParam.DATE_END, queryAdd.getTime());
        
        EventList searchResults = search.parseQuery(checkClashesQuery);
        
        //There is at least one task that clashes with the task to add.
        if (searchResults.size() > 0) {
            displayMessage("Task clashes with:");
            displayMessage(searchResults.toString());
        } else { //Add the task to the Model.*/
            Model.getInstance().addEvent(queryAddToEventObject(queryAdd));
        //}
    }

    /**
     * Converts an add query to an event object
     * @param queryAdd
     * @return
     */
    private static EventObject queryAddToEventObject(QueryAdd queryAdd) {
        EventObject eventObj = new EventObject(0, queryAdd.getName(), queryAdd.getDateRange());
        return eventObj;
    }


    private static void processError(QueryError queryError) {
        userInterfaceController.displayMessage(queryError.getMessage());
        if ( queryError.isHelpDisplayed() ) {
            showHelp();
        }
    }

    private static void showHelp() {
        userInterfaceController.displayMessage("Available Commands:");
        userInterfaceController.displayMessage(inputParser.showCommandList());
    }

    public static void printWelcomeMessage() {
        if ( stateManager.isState(AppState.FIRSTSTART) ) {
            userInterfaceController.displayMessage("CareLender: Maybe the best task manager in the world.");
            userInterfaceController.displayMessage(FirstStartMessages.askForName());
        } else {
            userInterfaceController.displayMessage("Welcome back, <username>");
        }

    }

    /**
     * Prints a message to screen
     * @param message message to be displayed
     */
    public static void displayMessage ( String message ) {
        userInterfaceController.displayMessage(message);
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
}
