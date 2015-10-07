package carelender.view.parser;

import carelender.model.data.*;

import java.util.Calendar;
import java.util.Date;

/**
 * Parses the user input
 */
public class InputParser {
    CommandManager commandManager;

    public InputParser () {
        commandManager = new CommandManager();
        defineDefaultCommands();
    }

    private void defineDefaultCommands() {
        Command newCommand;

        newCommand = new Command("add", QueryType.ADD);
        newCommand.setDescription("Adds a new event/task");
        newCommand.addKeywords("delimiter", "in,at,from,to,on");
        newCommand.addKeywords("relativeday", "tomorrow,next,monday,tuesday,wednesday,thursday,friday,saturday,sunday");
        newCommand.addKeywords("timeofday", "later,tonight,afternoon,night,morning,evening,noon" );
        commandManager.addCommand(newCommand);

        newCommand = new Command("list", QueryType.LIST);
        newCommand.setDescription("List all your future events/tasks");
        commandManager.addCommand(newCommand);

        newCommand = new Command("delete", QueryType.DELETE );
        newCommand.setDescription("Deletes a specified event/task");
        commandManager.addCommand(newCommand);

        newCommand = new Command("help", QueryType.HELP);
        newCommand.setDescription("Shows the list of commands");
        commandManager.addCommand(newCommand);

        newCommand = new Command("clear", QueryType.CLEAR);
        newCommand.setDescription("Clears the screen");
        commandManager.addCommand(newCommand);
    }

    public QueryBase parseCompleteInput ( String input ) {
        String [] queryParts = input.split(" ");
        queryParts = removeEmptyEntries(queryParts);

        String commandString = queryParts[0];

        Command matchedCommand = commandManager.matchCommand(commandString);

        if ( matchedCommand == null ) { //Error. Command not found.
            return new QueryError("[" + commandString + "] is not a valid command.");
        }

        CommandPart[] commandParts = matchedCommand.processKeywords(queryParts);

        QueryBase newQuery;
        switch (matchedCommand.getType()) {
            case ADD:
                newQuery = parseAddCommand(queryParts, commandParts);
                break;
            case EDIT:
                newQuery = new QueryEdit();
                break;
            case DELETE:
                newQuery = new QueryDelete();
                break;
            case HELP:
                newQuery = new QueryHelp();
                break;
            default:
                newQuery = new QueryGeneric(matchedCommand.getType());
                break;
        }
        return newQuery;
    }

    public QueryBase parseAddCommand ( String [] queryParts, CommandPart [] commandParts ) {
        QueryAdd queryAdd = new QueryAdd();

        int startIndex = 1; //Start at 1 because the first index is the command
        int endIndex = 1;
        //Find the end of the name
        while (endIndex < commandParts.length - 1) {
            endIndex++;
            if ( commandParts[endIndex].isKeyword() ) {
                break;
            }
        }
        String name = extractString(queryParts, startIndex, endIndex );
        queryAdd.setName(name);

        //Check if a relative day keyword exists
        String relativeDay = null;
        for ( CommandPart commandPart :commandParts ) {
            if ( commandPart.getKeywordType() != null &&
                 commandPart.getKeywordType().equalsIgnoreCase("relativeday") ) {
                relativeDay = commandPart.getQueryPart();
            }
        }

        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);


        if ( relativeDay != null ) {
            switch ( relativeDay ) {
                case "tomorrow":
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    break;
                case "monday":
                    calendar = nextDayOfWeek(Calendar.MONDAY);
                    break;
                case "tuesday":
                    calendar = nextDayOfWeek(Calendar.TUESDAY);
                    break;
                case "wednesday":
                    calendar = nextDayOfWeek(Calendar.WEDNESDAY);
                    break;
                case "thursday":
                    calendar = nextDayOfWeek(Calendar.THURSDAY);
                    break;
                case "friday":
                    calendar = nextDayOfWeek(Calendar.FRIDAY);
                    break;
                case "saturday":
                    calendar = nextDayOfWeek(Calendar.SATURDAY);
                    break;
                case "sunday":
                    calendar = nextDayOfWeek(Calendar.SUNDAY);
                    break;
            }
        }
        
        //WZ: I added these so they aren't random values floating around spoiling my equals.
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        //WZ: END
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);

        //Check if a time of day keyword exists
        String timeOfDay = null;
        for ( CommandPart commandPart :commandParts ) {
            if ( commandPart.getKeywordType() != null &&
                    commandPart.getKeywordType().equalsIgnoreCase("timeofday") ) {
                timeOfDay = commandPart.getQueryPart();
            }
        }

        if ( timeOfDay != null ) {
            switch ( timeOfDay ) {
                case "later":
                    calendar.add(Calendar.HOUR, 3);
                    break;
                case "tonight":
                case "night":
                    calendar.set(Calendar.HOUR_OF_DAY, 20);
                    break;
                case "afternoon":
                    calendar.set(Calendar.HOUR_OF_DAY, 14);
                    break;
                case "noon":
                    calendar.set(Calendar.HOUR_OF_DAY, 12);
                    break;
                case "morning":
                    calendar.set(Calendar.HOUR_OF_DAY, 9);
                    break;
                case "evening":
                    calendar.set(Calendar.HOUR_OF_DAY, 18);
                    break;
            }
        }


        Date newTime = calendar.getTime();
        queryAdd.setTime(newTime);

        return queryAdd;
    }

    /**
     * Gets the date of the next day of the week
     * @param dow
     * @return
     */
    public static Calendar nextDayOfWeek(int dow) {
        Calendar date = Calendar.getInstance();
        int diff = dow - date.get(Calendar.DAY_OF_WEEK);
        if (!(diff > 0)) {
            diff += 7;
        }
        date.add(Calendar.DAY_OF_MONTH, diff);
        return date;
    }

    /**
     * Takes the parts of the query and extracts the parts bounded by the indices
     * Returns the string joined by a " "
     * @param queryParts Parts of the query
     * @param start Start index
     * @param end End index
     * @return Extracted string
     */
    private String extractString ( String [] queryParts, int start, int end ) {
        StringBuilder stringBuilder = new StringBuilder();
        String space = "";
        for ( int i = start; i < end && i < queryParts.length; i++ ) {
            stringBuilder.append(space);
            stringBuilder.append(queryParts[i]);
            space = " ";
        }
        return stringBuilder.toString();
    }

    public String showCommandList() {
        StringBuilder stringBuilder = new StringBuilder();
        String newLine = "  ";
        for ( Command command : commandManager.commands ) {
            stringBuilder.append(newLine);
            newLine = "\n  ";
            stringBuilder.append(command.getCommand());
            stringBuilder.append(": ");
            stringBuilder.append(command.getDescription());
        }
        return stringBuilder.toString();
    }

    /**
     * Removes any entries of a string array that are of length 0
     * @param queryParts String array
     * @return array with empty entries removed
     */
    private String [] removeEmptyEntries ( String [] queryParts ) {
        int emptyCount = 0;
        for ( String queryPart : queryParts ) {
            if ( queryPart.length() == 0 ) emptyCount++;
        }

        String [] trimmedArray = new String[queryParts.length - emptyCount];
        int trimIndex = 0;
        for ( String queryPart : queryParts ) {
            if ( queryPart.length() != 0 ) {
                trimmedArray[trimIndex] = queryPart;
                trimIndex++;
            }
        }
        return trimmedArray;
    }
}
