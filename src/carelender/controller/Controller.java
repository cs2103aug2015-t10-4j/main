package carelender.controller;

import carelender.model.Model;
import carelender.model.data.QueryDummy;
import carelender.model.data.QueryObject;
import carelender.view.GraphicalInterface;
import carelender.view.InputParser;

import javax.naming.ldap.Control;

/**
 * Does all the logic of the application
 */
public class Controller {

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
     * @param userInput
     */
    public static void processUserInput(String userInput) {
        QueryObject query = inputParser.parseCompleteInput(userInput);

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
}
