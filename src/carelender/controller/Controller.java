package carelender.controller;

import carelender.model.Model;
import carelender.model.data.QueryAdd;
import carelender.model.data.QueryDummy;
import carelender.model.data.QueryBase;
import carelender.model.data.QueryError;
import carelender.view.GraphicalInterface;
import carelender.view.parser.InputParser;

import java.text.SimpleDateFormat;

/**
 * Does all the logic of the application
 */
public class Controller {
    private static boolean debugMode = true;

    private static GraphicalInterface graphicalInterface = null;
    private static Search search = null;
    private static Model model = null;
    private static InputParser inputParser = null;

    public static void initialize() {
        search = new Search();
        model = new Model();
        inputParser = new InputParser();
    }
    public static void initGraphicalInterface(GraphicalInterface graphicalInterface) {
        Controller.graphicalInterface = graphicalInterface;
    }



    /**
     * Processes the user input.
     * Called by the GraphicalInterface class
     * @param userInput The user input string
     */
    public static void processUserInput(String userInput) {
        QueryBase query = inputParser.parseCompleteInput(userInput);

        switch (query.getQueryType()) {
            case DUMMY:
                processDummy((QueryDummy) query);
                break;
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
                processAdd( (QueryAdd) query);
                break;
            default:
                graphicalInterface.displayMessage("Command accepted.");
                break;
        }
    }

    private static void processAdd(QueryAdd queryAdd ) {
        String dateString = new SimpleDateFormat("E dd MMM yyyy h:mma Z").format(queryAdd.getTime());
        displayMessage("Adding new task: ["+queryAdd.getName()+"] at " + dateString);
    }
    private static void processError(QueryError queryError) {
        graphicalInterface.displayMessage(queryError.getMessage());
        showHelp();
    }

    private static void showHelp() {
        graphicalInterface.displayMessage("Available Commands:");
        graphicalInterface.displayMessage(inputParser.showCommandList());
    }
    private static void processDummy(QueryDummy queryDummy) {
        if ( queryDummy.getData().equals("clear") ) {
            graphicalInterface.clearMessageLog();
        } else if (queryDummy.getData().equals("help")) {
            graphicalInterface.displayMessage("Commands:\ndisplay [to display] - displays the text input\nclear - clears the screen\nhelp - shows this screen");
        } else if (queryDummy.getData().startsWith("display ")) {
            graphicalInterface.displayMessage(queryDummy.getData().substring(8));
        } else {
            graphicalInterface.displayMessage("Invalid command. Need help? Type help.");
        }
    }
    
    public static void printWelcomeMessage(){
    	graphicalInterface.displayMessage("CareLender: Maybe the best task manager in the world.");
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
}
