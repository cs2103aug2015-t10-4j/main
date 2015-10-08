package carelender.controller;

import carelender.model.Model;
import carelender.model.data.*;
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
    
    private static ArrayList<String> messageList;

    public static void initialize() {
        model = new Model();
        search = new Search(model);
        inputParser = new InputParser();
        messageList = new ArrayList<>();
    }
    public static void initGraphicalInterface(GraphicalInterface graphicalInterface) {
        Controller.graphicalInterface = graphicalInterface;
        Controller.graphicalInterface.setMessageList(messageList);
    }



    /**
     * Processes the user input.
     * Called by the GraphicalInterface class
     * @param userInput The user input string
     */
    public static void processUserInput(String userInput) {
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
    	
    	int count = 1;
    	for (EventObject event : searchResults) {
    		//this.model.deleteEvent(event);
    		System.out.println(event.getName());
    	}
    	
        displayMessage("Deleting [" + queryDelete.getName() + "]");
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
        	int count = 1;
        	for (EventObject event : searchResults) {
        		displayMessage(count + ". " + event.getName());
        		count++;
        	}
        }
    }

    private static void processAdd(QueryAdd queryAdd ) {
        String dateString = new SimpleDateFormat("E dd MMM yyyy h:mma Z").format(queryAdd.getTime());
        displayMessage("Adding new task: ["+queryAdd.getName()+"] at " + dateString);

        //WZ: I added all these to accommodate adding of tasks into the model.
        QueryList checkClashesQuery = new QueryList();
        checkClashesQuery.addSearchParam(QueryList.SearchParam.DATE_START, queryAdd.getTime());
        checkClashesQuery.addSearchParam(QueryList.SearchParam.DATE_END, queryAdd.getTime());
        
        EventList searchResults = search.parseQuery(checkClashesQuery);
        
        //There is at least one task that clashes with the task to add.
        if (searchResults.size() > 0) {
        	displayMessage("Task clashes with:");
        	int count = 1;
        	for (EventObject event : searchResults) {
        		displayMessage(count + ". " + event.getName());
        		count++;
        	}
        } else { //Add the task to the Model.
        	model.addEvent(queryAdd);
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
