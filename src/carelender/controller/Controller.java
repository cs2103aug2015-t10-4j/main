package carelender.controller;

import carelender.model.Model;
import carelender.model.data.DateRange;
import carelender.model.data.EventList;
import carelender.model.data.EventObject;
import carelender.model.data.QueryAdd;
import carelender.model.data.QueryList;
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

    //TODO: TEMP LOCAL STORAGE FOR TASKS.
    private static EventList eventList = null;
    
    public static void initialize() {
        search = new Search();
        model = new Model();
        inputParser = new InputParser();
        
        //TODO: TEMP LOCAL STORAGE FOR TASKS.
        eventList = new EventList();
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
        
        //WZ: I added all these to accommodate adding of tasks into the model.
        QueryList checkClashesQuery = new QueryList();
        checkClashesQuery.addSearchParam(QueryList.PARAM.DATE_START, queryAdd.getTime());
        checkClashesQuery.addSearchParam(QueryList.PARAM.DATE_END, queryAdd.getTime());
        EventList clashingTasks = search.parseQuery(checkClashesQuery);
        
        //There is at least one task that clashes with the task to add.
        if (clashingTasks.size() > 0) {
        	displayMessage("Task clashes with:");
        	int count = 1;
        	for (EventObject event : clashingTasks) {
        		displayMessage(count + ". " + event.getName());
        		count++;
        	}
        } else { //Add the task to the Model.
			DateRange[] dateRange = new DateRange[]{new DateRange(queryAdd.getTime(),
																	queryAdd.getTime())};
        	Model.addEvent(new EventObject(0, queryAdd.getName(), dateRange));
        }
        //WZ: END
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
