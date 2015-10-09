package carelender.controller;

import carelender.controller.callbacks.OnConfirmedCallback;
import carelender.controller.callbacks.OnEventSelectedCallback;
import carelender.controller.states.AppState;
import carelender.controller.states.InputRequestState;
import carelender.model.AppSettings;
import carelender.model.Model;
import carelender.model.data.*;
import carelender.model.strings.FirstStartMessages;
import carelender.view.GraphicalInterface;
import carelender.view.parser.InputParser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Does all the logic of the application
 */
public class Controller {
    private static boolean debugMode = true;

    private static GraphicalInterface graphicalInterface = null;
    private static Search search = null;
    private static Model model = null;
    private static InputParser inputParser = null;
    private static AppSettings appSettings = null;

    //Stores the messages to the user
    private static ArrayList<String> messageList;
    //Stores the user's inputs
    private static ArrayList<String> commandList;
    private static int currentCommand;
    private static String incompleteInput;

    //Application state
    private static AppState appState;
    private static AppState prevState;
    private static InputRequestState inputRequestState;
    private static OnEventSelectedCallback onEventSelectedCallback;
    private static OnConfirmedCallback onConfirmedCallback;

    private static String userName;

    //Temp variables
    private static EventList selectionList; //Used for selecting multiple objects

    public static void initialize() {
        model = new Model();
        search = new Search(model);
        appSettings = new AppSettings();
        inputParser = new InputParser();
        messageList = new ArrayList<>();
        commandList = new ArrayList<>();
        appState = AppState.FIRSTSTART;
        inputRequestState = InputRequestState.NONE;
        prevState = null;
        userName = null;
        currentCommand = 0;
        selectionList = null;
        onEventSelectedCallback = null;
        onConfirmedCallback = null;
    }

    public static void initGraphicalInterface(GraphicalInterface graphicalInterface) {
        Controller.graphicalInterface = graphicalInterface;
        Controller.graphicalInterface.setMessageList(messageList);
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
            graphicalInterface.setUserInput(incompleteInput);
        } else {
            int commandIndex = commandList.size() - currentCommand - 1;
            if (commandIndex < 0 || commandIndex >= commandList.size()) {
                return;
            }
            graphicalInterface.setUserInput(commandList.get(commandIndex));
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
     * Called by the GraphicalInterface class
     * @param userInput The user input string
     */
    public static void processUserInput(String userInput) {
        userInput = userInput.trim();
        saveUserCommand(userInput);
        switch ( inputRequestState ) {
            case EVENTSELECTION:
                stateChoosing(userInput);
                break;

            case CONFIRMING:
                stateConfirming(userInput);
                break;

            default:
                switch ( appState ) {
                    case FIRSTSTART:
                        stateFirstStart(userInput);
                        break;

                    default:
                        stateDefault(userInput);
                        break;
                }
                break;
        }

        System.out.println("Current state: " + appState.toString() + " " + inputRequestState.toString());
    }


    private static void stateFirstStart ( String userInput ) {
        final OnConfirmedCallback confirmNameCallback = new OnConfirmedCallback() {
            @Override
            public void onConfirmed(boolean confirmed) {
                System.out.println("Confirmed: " + confirmed);
                if ( confirmed ) {
                    displayMessage(FirstStartMessages.confirmed(userName));
                    changeState(AppState.DEFAULT);
                } else {
                    displayMessage(FirstStartMessages.askForNameAgain());
                    userName = null;
                }
            }
        };
        //TODO: Save user's name to memory

        userName = userInput;
        startConfirmation(FirstStartMessages.confirmation(userName), confirmNameCallback);
    }

    /**
     * This state is used when the user is required to choose from a list of events
     * @param userInput User's input
     */
    private static void stateChoosing ( String userInput ) {
        try {
            int chosen = Integer.parseInt( userInput );
            if ( chosen < 1 || chosen > selectionList.size() ) {
                String message = "You've chosen an invalid number, please enter a number between 1 and " + selectionList.size();
                displayMessage(message);
            } else {
                EventObject selectedObject = selectionList.get(chosen-1);
                inputRequestState = InputRequestState.NONE;
                if ( onEventSelectedCallback != null ) {
                    onEventSelectedCallback.onChosen(selectedObject);
                }
            }
        } catch ( NumberFormatException e ) {
            displayMessage("Please input a number");
        }
    }

    private static void stateConfirming(String userInput) {
        userInput = userInput.trim().toLowerCase();
        if ( userInput.startsWith("y") ) {
            inputRequestState = InputRequestState.NONE;
            if ( onConfirmedCallback != null ) {
                onConfirmedCallback.onConfirmed(true);
            }
        } else if ( userInput.startsWith("n") ) {
            inputRequestState = InputRequestState.NONE;
            if ( onConfirmedCallback != null ) {
                onConfirmedCallback.onConfirmed(false);
            }
        } else {
            displayMessage(FirstStartMessages.invalidInput());
        }
    }

    private static void stateDefault(String userInput) {
        if ( userInput.length() == 0 ) return;
        QueryBase query = inputParser.parseCompleteInput(userInput);

        switch (query.getQueryType()) {
            /*case DUMMY:
                processDummy((QueryDummy) query);
                break;*/
            case ERROR:
                processError((QueryError) query);
                break;
            case HELP:
                showHelp();
                break;
            case CLEAR:
                graphicalInterface.clearMessageLog();
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

            case SWITCHUI:
                processSwitchUI();
                break;
            default:
                graphicalInterface.displayMessage("Command accepted.");
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
            selectionList = searchResults;
            String message = "There are multiple \""+queryDelete.getName()+"\" tasks, please choose the one to delete.";

            startEventSelection(message, searchResults, deleteCallback);
        } else {
            deleteCallback.onChosen(selectionList.get(0));
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
        String dateString = new SimpleDateFormat("E dd MMM yyyy h:mma Z").format(queryAdd.getTime());
        displayMessage("Adding new task: ["+queryAdd.getName()+"] at " + dateString);

        QueryList checkClashesQuery = new QueryList();
        checkClashesQuery.addSearchParam(QueryList.SearchParam.DATE_START, queryAdd.getTime());
        checkClashesQuery.addSearchParam(QueryList.SearchParam.DATE_END, queryAdd.getTime());
        
        EventList searchResults = search.parseQuery(checkClashesQuery);
        
        //There is at least one task that clashes with the task to add.
        if (searchResults.size() > 0) {
            displayMessage("Task clashes with:");
            displayMessage(searchResults.toString());
        } else { //Add the task to the Model.
            model.addEvent(queryAddToEventObject(queryAdd));
        }
    }

    /**
     * Converts an add query to an event object
     * @param queryAdd
     * @return
     */
    private static EventObject queryAddToEventObject(QueryAdd queryAdd) {
        DateRange dateRange = new DateRange(queryAdd.getTime());
        DateRange[] dateRangeArray = new DateRange[1];
        dateRangeArray[0] = dateRange;
        EventObject eventObj = new EventObject(0, queryAdd.getName(), dateRangeArray);
        return eventObj;
    }


    private static void processError(QueryError queryError) {
        graphicalInterface.displayMessage(queryError.getMessage());
        showHelp();
    }

    private static void showHelp() {
        graphicalInterface.displayMessage("Available Commands:");
        graphicalInterface.displayMessage(inputParser.showCommandList());
    }

    public static void printWelcomeMessage() {
        if ( appState == AppState.FIRSTSTART ) {
            graphicalInterface.displayMessage("CareLender: Maybe the best task manager in the world.");
            graphicalInterface.displayMessage(FirstStartMessages.askForName());
        } else {
            graphicalInterface.displayMessage("Welcome back, <username>");
        }

    }

    /**
     * Prints a message to screen
     * @param message message to be displayed
     */
    public static void displayMessage ( String message ) {
        graphicalInterface.displayMessage(message);
    }
    /**
     * Prints a message to the screen only in debug mode.
     * @param message message to be displayed
     */
    public static void printDebugMessage ( String message ) {
        if ( debugMode ) {
            graphicalInterface.displayMessage(message);
        }
        System.out.println(message);
    }

    /**
     * Set the application into a choosing mode
     * @param message Message to show to user before displaying the choices
     * @param choices List of events to choose from
     * @param callback Code to be run after the choosing is complete
     */
    public static void startEventSelection(String message, EventList choices, OnEventSelectedCallback callback) {
        inputRequestState = InputRequestState.EVENTSELECTION;
        selectionList = choices;
        onEventSelectedCallback = callback;
        displayMessage(message);
        displayMessage(selectionList.toString());
    }

    /**
     * Set the application into a confirmation mode
     * @param message Message to show user
     */
    public static void startConfirmation ( String message, OnConfirmedCallback callback ) {
        inputRequestState = InputRequestState.CONFIRMING;
        onConfirmedCallback = callback;

        displayMessage(message);
    }



    public static void changeState ( AppState newState ) {
        if ( appState == newState ) return;
        prevState = appState;
        appState = newState;
        System.out.println("State changed: " + newState.toString());
    }


    /*private static void processDummy(QueryDummy queryDummy) {
        if ( queryDummy.getData().equals("clear") ) {
            graphicalInterface.clearMessageLog();
        } else if (queryDummy.getData().equals("help")) {
            graphicalInterface.displayMessage("Commands:\ndisplay [to display] - displays the text input\nclear - clears the screen\nhelp - shows this screen");
        } else if (queryDummy.getData().startsWith("display ")) {
            graphicalInterface.displayMessage(queryDummy.getData().substring(8));
        } else {
            graphicalInterface.displayMessage("Invalid command. Need help? Type help.");
        }
    }*/
}
