package carelender.controller;

import carelender.model.Model;
import carelender.model.data.QueryDummy;
import carelender.model.data.QueryBase;
import carelender.view.GraphicalInterface;
import carelender.view.parser.InputParser;

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
                processDummy((QueryDummy)query);
                break;
            case ADD:
            case DELETE:
            case EDIT:
                break;
        }
    }

    private static void processDummy(QueryDummy query) {
        if ( query.getData().equals("clear") ) {
            graphicalInterface.clearMessageLog();
        } else if (query.getData().equals("help")) {
            graphicalInterface.displayMessage("Commands:\ndisplay [to display] - displays the text input\nclear - clears the screen\nhelp - shows this screen");
        } else if (query.getData().startsWith("display ")) {
            graphicalInterface.displayMessage(query.getData().substring(8));
        } else {
            graphicalInterface.displayMessage("Invalid command. Need help? Type help.");
        }
    }
    
    public static void printWelcomeMessage(){
    	graphicalInterface.displayMessage("CareLender: Maybe the best task manager in the world.");
    }

    /**
     * Prints a message to the screen, used for debugging purposes.
     * @param message message to be displayed
     */
    public static void printDebugMessage ( String message ) {
        if ( debugMode ) {
            graphicalInterface.displayMessage(message);
        }
        System.out.println(message);
    }
}
