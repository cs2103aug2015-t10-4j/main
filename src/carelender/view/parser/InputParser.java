package carelender.view.parser;

import carelender.model.data.*;
import net.fortuna.ical4j.model.DateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

/**
 * Parses the user input
 */
public class InputParser {
    private static InputParser singleton = null;
    public static InputParser getInstance() {
        if ( singleton == null ) {
            singleton = new InputParser();
        }
        return singleton;
    }
    CommandManager commandManager;
    //List of displayed events
    private EventList displayedList;

    private InputParser () {
        commandManager = new CommandManager();
        defineDefaultCommands();
    }

    private void defineDefaultCommands() {
        Command newCommand;

        newCommand = new Command("add", QueryType.ADD);
        newCommand.setDescription("Adds a new event/task");
        newCommand.setUsage("add \"event name\" \"dates\" [cat \"category\"]");
        newCommand.addKeywords("category", "category,cat", CommandKeyword.DataPosition.AFTER);
        commandManager.addCommand(newCommand);

        newCommand = new Command("search", QueryType.LIST);
        newCommand.setDescription("Search for events/tasks");
        newCommand.setUsage("search \"event name\" \"date/daterange\" [cat \"category\"]");
        newCommand.addKeywords("category", "category,cat", CommandKeyword.DataPosition.AFTER);
        commandManager.addCommand(newCommand);

        newCommand = new Command("list", QueryType.LIST);
        newCommand.setDescription("List all your future events/tasks");
        newCommand.setUsage("list [past/today/tomorrow/future]");
        newCommand.addKeywords("range", "past,today,tomorrow,future", CommandKeyword.DataPosition.NONE);
        commandManager.addCommand(newCommand);

        
        newCommand = new Command("update", QueryType.UPDATE);
        newCommand.setDescription("Update events/tasks");
        newCommand.setUsage("update \"event name\" into \"new name\" [tomorrow/today/etc...] [morning/noon/etc...]");
        newCommand.addKeywords("delimiter", "into", CommandKeyword.DataPosition.NONE);
        newCommand.addKeywords("relativeday", "tomorrow,next,monday,tuesday,wednesday,thursday,friday,saturday,sunday", CommandKeyword.DataPosition.NONE);
        newCommand.addKeywords("timeofday", "later,tonight,afternoon,night,morning,evening,noon", CommandKeyword.DataPosition.NONE);
        commandManager.addCommand(newCommand);

        newCommand = new Command("delete", QueryType.DELETE );
        newCommand.setUsage("delete <id>");
        newCommand.setDescription("Deletes a specified event/task");
        commandManager.addCommand(newCommand);

        newCommand = new Command("help", QueryType.HELP);
        newCommand.setDescription("Shows the list of commands");
        commandManager.addCommand(newCommand);

        newCommand = new Command("?", QueryType.HELP);
        newCommand.setDescription("Shows the list of commands");
        commandManager.addCommand(newCommand);

        newCommand = new Command("clear", QueryType.CLEAR);
        newCommand.setDescription("Clears the screen");
        commandManager.addCommand(newCommand);

        newCommand = new Command("switch", QueryType.SWITCHUI);
        newCommand.setDescription("Switches the screen");
        commandManager.addCommand(newCommand);

        newCommand = new Command("settings", QueryType.SWITCHUI);
        newCommand.setDescription("Go to the settings page");
        commandManager.addCommand(newCommand);

        newCommand = new Command("setting", QueryType.SWITCHUI);
        newCommand.setDescription("Go to the settings page");
        commandManager.addCommand(newCommand);

        newCommand = new Command("date", QueryType.DATETEST);
        newCommand.setDescription("Does date parse testing");
        commandManager.addCommand(newCommand);

        newCommand = new Command("undo", QueryType.UNDO);
        newCommand.setDescription("Undoes the last command");
        commandManager.addCommand(newCommand);

        newCommand = new Command("exit", QueryType.EXIT);
        newCommand.setDescription("Closes the program");
        commandManager.addCommand(newCommand);

        newCommand = new Command("quit", QueryType.EXIT);
        newCommand.setDescription("Closes the program");
        commandManager.addCommand(newCommand);

        newCommand = new Command("close", QueryType.EXIT);
        newCommand.setDescription("Closes the program");
        commandManager.addCommand(newCommand);

        newCommand = new Command("dev1", QueryType.DEV1);
        newCommand.setDescription("Developer thing");
        commandManager.addCommand(newCommand);

        newCommand = new Command("dev2", QueryType.DEV2);
        newCommand.setDescription("Developer command");
        commandManager.addCommand(newCommand);
    }

    /**
     * Gets the list of helper options
     * @param userInput
     * @return
     */
    public String[] getAutocompleteOptions(String userInput) {
        ArrayList <String> options = new ArrayList<>();
        String [] queryParts = splitQuery(userInput);

        if ( (queryParts.length == 1 && userInput.endsWith(" ")) || queryParts.length > 1 ) {
            Command command = commandManager.matchCommand(queryParts[0]);
            CommandPart [] commandParts = command.processKeywords(queryParts);

            String usage = command.getUsage();
            if ( usage != null && usage.length() > 0 ) {
                options.add(usage);
            }

            if ( userInput.endsWith(" ") ) {
                //User is not midway through typing a word
                for ( CommandKeyword keyword : command.keywords  ) {
                    CommandPart commandPart = getCommandPart(keyword.getType(), commandParts);
                    if ( commandPart == null ) {
                        //This keyword doesn't exist in the string
                        options.add(userInput + keyword.getKeyword());
                    }
                }
            } else {
                String lastWord = queryParts[queryParts.length-1];
                queryParts[queryParts.length-1] = "";
                for ( CommandKeyword keyword : command.keywords  ) {
                    CommandPart commandPart = getCommandPart(keyword.getType(), commandParts);
                    if ( commandPart == null ) {
                        //This keyword doesn't exist in the string
                        if ( keyword.getKeyword().startsWith(lastWord) ) {
                            String inputWithoutLastWord = String.join(" ", queryParts);
                            options.add(inputWithoutLastWord + keyword.getKeyword());
                        }
                    }

                }
            }


            /*for ( CommandKeyword keyword : command.keywords  ) {
                CommandPart commandPart = getCommandPart(keyword.getKeyword(), commandParts);
                if ( commandPart == null ) {
                    //This keyword doesn't exist in the string
                    options.add();
                }
            }*/
            if ( options.size() == 0 ) {
                return null;
            } else {
                return options.toArray(new String[options.size()]);
            }
        } else if ( queryParts.length == 1 ) {
            //Still on first word
            for ( Command command: commandManager.commands ) {
                if ( command.getCommand().toLowerCase().startsWith(userInput.toLowerCase()) ) {
                    options.add(command.getCommand() + " - " + command.getDescription());
                }
            }
            if ( options.size() == 0 ) {
                return null;
            } else {
                return options.toArray(new String[options.size()]);
            }
        } else {
            return null;
        }

    }

    /**
     * Parses the complete user input
     * @param input
     * @return
     */
    public QueryBase parseCompleteInput ( String input ) {
        assert input.length() != 0 : "Cannot parse empty input";

        String [] queryParts = splitQuery(input);
        String commandString = queryParts[0];
        DateRange [] dateRanges = DateTimeParser.parseDateTime(input);
        input = DateTimeParser.replaceDateParts(input, "|DATE|");
        String [] queryPartsNoDate = splitQuery(input);

        Command matchedCommand = commandManager.matchCommand(commandString);

        if ( matchedCommand == null ) { //Error. Command not found.
            return new QueryError("[" + commandString + "] is not a valid command.");
        }

        CommandPart[] commandPartsNoDate = matchedCommand.processKeywords(queryPartsNoDate);
        CommandPart[] commandParts = matchedCommand.processKeywords(queryParts);

        QueryBase newQuery;
        switch (matchedCommand.getType()) {
            case ADD:
                newQuery = parseAddCommand(commandPartsNoDate, dateRanges);
                break;
            case DELETE:
                newQuery = parseDeleteCommand(commandParts);
                break;
            case UPDATE:
                newQuery = parseUpdateCommand(queryPartsNoDate, commandPartsNoDate, input);
                break;
            case LIST:
                if ( matchedCommand.getCommand().equalsIgnoreCase("search") ) {
                    newQuery = parseSearchCommand(dateRanges, commandPartsNoDate);
                } else { //List command
                    newQuery = parseListCommand(queryPartsNoDate, commandParts);
                }
                break;
            case HELP:
                newQuery = new QueryHelp();
                break;
            case SWITCHUI:
                if ( matchedCommand.getCommand().equalsIgnoreCase("switch") ) {
                    newQuery = new QuerySwitchUI(false);
                } else { //Go to settings
                    newQuery = new QuerySwitchUI(true);
                }
                break;
            case UNDO:
                newQuery = new QueryUndo();
                break;
            default:
                newQuery = new QueryGeneric(matchedCommand.getType());
                break;
        }
        return newQuery;
    }

    public QueryBase parseSearchCommand ( DateRange[] dateRanges, CommandPart [] commandParts ) {
        QueryList queryList = new QueryList();

        boolean pass = false;
        //Get name data
        if (commandParts.length >= 1 ) { //First item should be the name if it's not a keyword
            if ( !commandParts[0].isKeyword() ) {
                queryList.addSearchParam(QueryList.SearchParam.NAME_CONTAINS, commandParts[0].getQueryPart());
                pass = true;
            }
        }
        //Get date data
        if (dateRanges != null ) {
            if ( dateRanges.length == 1 ) {
                if ( dateRanges[0].hasTime() ) {
                    queryList.addSearchParam(QueryList.SearchParam.DATE_START, dateRanges[0].getStart());
                    queryList.addSearchParam(QueryList.SearchParam.DATE_END, dateRanges[0].getEnd());
                } else {
                    queryList.addSearchParam(QueryList.SearchParam.DATE_START, DateTimeParser.startOfDay(dateRanges[0].getStart()));
                    queryList.addSearchParam(QueryList.SearchParam.DATE_END, DateTimeParser.endOfDay(dateRanges[0].getEnd()));
                }
                pass = true;
            } else {
                return new QueryError("Please input at most one date range");
            }
        }

        //Get category data
        CommandPart commandPart = getCommandPart("category", commandParts);
        if ( commandPart != null ) {
            String category = commandPart.getKeywordData();
            if ( category != null ) {
                queryList.addSearchParam(QueryList.SearchParam.CATEGORY, category);
                pass = true;
            }
        }

        if ( !pass ) {
            return new QueryError("Please input something to search");
        }

        return queryList;
    }

    public QueryBase parseListCommand ( String[] queryParts, CommandPart [] commandParts ) {
        QueryList queryList = new QueryList();

        //Check if a relative day keyword exists
        String range = null;
        for ( CommandPart commandPart :commandParts ) {
            if ( commandPart.getKeywordType() != null &&
                    commandPart.getKeywordType().equalsIgnoreCase("range") ) {
                range = commandPart.getQueryPart();
            }
        }

        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);

        if ( range == null ) {
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            queryList.addSearchParam(QueryList.SearchParam.DATE_START, calendar.getTime());
        } else {
            switch (range) {
                case "past": //Displays events that have passed
                    queryList.addSearchParam(QueryList.SearchParam.DATE_END, now);
                    break;
                case "tomorrow": //Displays events tomorrow onwards
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                case "today": //Everything for today
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    queryList.addSearchParam(QueryList.SearchParam.DATE_START, calendar.getTime());

                    calendar.set(Calendar.HOUR_OF_DAY, 23);
                    calendar.set(Calendar.MINUTE, 59);
                    queryList.addSearchParam(QueryList.SearchParam.DATE_END, calendar.getTime());
                    break;
                case "future": //Displays events today onwards
                default:
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    queryList.addSearchParam(QueryList.SearchParam.DATE_START, calendar.getTime());
                    break;
            }
        }


        return queryList;

    }
    
    public QueryBase parseDeleteCommand ( CommandPart [] commandParts ) {
        if ( commandParts.length < 1 ) {
            return new QueryError("What do you want to delete?");
        }
        if ( displayedList == null ) {
            return new QueryError("Nothing displayed");
        }

        ArrayList<Integer> indexList = extractIndices(commandParts[0].getQueryPart());

        if ( indexList == null ) {
            return new QueryError("Error parsing indices");
        }
        QueryDelete queryDelete = new QueryDelete();
        for ( int i : indexList ) {
            Event event = displayedList.get(i);
            if ( event != null ) {
                queryDelete.addEvent(event);
            }
        }

        return queryDelete;
    }
    
    public QueryBase parseUpdateCommand ( String [] queryParts, CommandPart [] commandParts, String input ) {
        QueryUpdate queryUpdate = new QueryUpdate();

        String name = queryParts[1]; //First item is the name
        queryUpdate.addSearchParam(QueryList.SearchParam.NAME_EXACT, name);
        
        //TODO: THIS IS A HACK.
        String newName = queryParts[3];
        queryUpdate.addUpdateParam(QueryUpdate.UpdateParam.NAME, newName);
        
        //TODO: THIS IS A HACK AS WELL.
        if (queryParts.length <= 4) {
            return queryUpdate;
        }

        SimpleDateGroup [] dateGroups = DateTimeParser.parseDateTimeRaw(input);
        if ( dateGroups != null ) {
            if ( dateGroups.length > 1 && dateGroups.length % 2 != 0) {
                //Odd number of dates
                return new QueryError("Please input an even number of date time values", false);
            }

            DateRange[] dr = new DateRange[]{new DateRange(dateGroups[0].dates[0], dateGroups[0].hasTime)};
            queryUpdate.addUpdateParam(QueryUpdate.UpdateParam.DATE_RANGE, dr);

        }

        return queryUpdate;
    }

    public QueryBase parseAddCommand ( CommandPart [] commandParts, DateRange [] dateRanges ) {
        QueryAdd queryAdd = new QueryAdd();

        if ( commandParts.length == 0 ) {
            return new QueryError("Type something to add");
        }
        String name = commandParts[0].getQueryPart(); //First item should be the name
        queryAdd.setName(name);

        if ( dateRanges != null ) {
            queryAdd.setDateRange(dateRanges);
        }

        CommandPart commandPart = getCommandPart("category", commandParts);
        if ( commandPart != null ) {
            String category = commandPart.getKeywordData();
            if ( category != null ) {
                queryAdd.setCategory(category);
            }
        }

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
     * @param start Start index inclusive
     * @param end End index inclusive
     * @return Extracted string
     */
    private String extractString ( String [] queryParts, int start, int end ) {
        StringBuilder stringBuilder = new StringBuilder();
        String space = "";
        for ( int i = start; i <= end && i < queryParts.length; i++ ) {
            stringBuilder.append(space);
            stringBuilder.append(queryParts[i]);
            space = " ";
        }
        return stringBuilder.toString();
    }

    /**
     * Splits up the query into it's individual parts.
     * Also splits by |DATE| string
     * Takes into account "" strings.
     *   e.g. "Hello world" will be converted into one string excluding the "
     * @param input The user input
     * @return String array with each part in it's own entry
     */
    public String [] splitQuery ( String input ) {
        assert input.length() != 0 : "Cannot parse empty input";
        String[] queryParts = input.split("( |\\|DATE\\|)");
        ArrayList<String> processingList = new ArrayList<>(queryParts.length);
        int startString = 0;
        int endString;
        boolean isProcessingString = false;

        for (int i = 0; i < queryParts.length; i++) {
            String part = queryParts[i];
            if (!isProcessingString) {
                //Search for the start of a string
                if (part.startsWith("\"")) {
                    startString = i;
                    isProcessingString = true;
                } else {
                    processingList.add(part);
                }
            }

            if (isProcessingString) {
                //Search for the end of a string
                if (part.endsWith("\"") && part.length() > 1) {
                    endString = i;
                    isProcessingString = false;
                    String extracted = extractString(queryParts, startString, endString);
                    //Add to list and remove the "" wrapping characters.
                    processingList.add(extracted.substring(1, extracted.length() - 1));
                }
            }

            if (isProcessingString && i == queryParts.length - 1) {
                //Unmatched string close. Automatically close
                endString = i;
                isProcessingString = false;
                String extracted = extractString(queryParts, startString, endString);
                //Add to list and remove the initial " characters.
                processingList.add(extracted.substring(1, extracted.length()));
            }
        }
        for (int i = 0; i < queryParts.length; i++) {
            if (i >= processingList.size()) {
                queryParts[i] = "";
            } else {
                queryParts[i] = processingList.get(i);
            }
        }
        queryParts = removeEmptyEntries(queryParts);
        return queryParts;
    }

    /**
     * Gets a command part based on the type
     * @param type Type of keyword
     * @param commandParts commandPart array
     * @return null if it doesn't exist
     */
    private CommandPart getCommandPart ( String type, CommandPart [] commandParts ) {
        for ( CommandPart commandPart : commandParts ) {
            if ( type.equals(commandPart.getKeywordType())) {
                return commandPart;
            }
        }
        return null;
    }
    /**
     * Takes an index string and converts it into a list of indices
     * Example: "1,13,5-8" will give a list [1,5,6,7,8,13]
     * @param indexString String containing indices
     * @return null if there is an error in parsing
     */
    private ArrayList<Integer> extractIndices ( String indexString ) {
        //String errorMessage = "";
        boolean pass = true;
        ArrayList<Integer> indexList = new ArrayList<>();
        final String numberRegex = "^[0-9]+$";
        //Parse indices
        String[] indexPartArray = indexString.split(",");
        for ( String indexPart : indexPartArray ) {
            if ( indexPart.contains("-") ) { //Range
                String [] rangeArray = indexPart.split("-");
                if ( rangeArray.length != 2 ) {
                    //errorMessage += "Did not understand: [" + indexPart + "]\n";
                    pass = false;
                } else {
                    int start = -1;
                    int end = -1;
                    if ( !rangeArray[0].matches(numberRegex) ) {
                        //errorMessage += "Not an integer: [" + rangeArray[0] + "]\n";
                        pass = false;
                    } else {
                        start = Integer.parseInt(rangeArray[0]);
                    }
                    if ( !rangeArray[1].matches(numberRegex) ) {
                        //errorMessage += "Not an integer: [" + rangeArray[1] + "]\n";
                        pass = false;
                    } else {
                        end = Integer.parseInt(rangeArray[1]);
                    }

                    //Indices are valid
                    if ( start != -1 && end != -1 ) {
                        if ( start > end ) {
                            int t = start;
                            start = end;
                            end = t;
                        }
                        for ( int i = start; i < end; i++ ) {
                            indexList.add(i);
                        }
                    }
                }
            } else { //Single index
                int index = -1;
                if ( !indexPart.matches(numberRegex) ) {
                    //errorMessage += "Not an integer: [" + indexPart + "]\n";
                    pass = false;
                } else {
                    index = Integer.parseInt(indexPart);
                }

                if ( index != -1 ) {
                    indexList.add(index);
                }
            }
        }

        Collections.sort(indexList);

        if ( displayedList.size() > 0 ) {
            for (int i = 0; i < indexList.size(); i++) {
                int index = indexList.get(i) - 1;
                indexList.set(i, index);
                if (index < 0 || index >= displayedList.size()) {
                    //errorMessage += "Selection is out of bounds\n";
                    pass = false;
                    break;
                }
            }
        } else {
            //errorMessage += "Selection is out of bounds\n";
            pass = false;
        }

        if ( pass ) {
            return indexList;
        }
        return null;
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

    public void setDisplayedList(EventList displayedList) {
        this.displayedList = displayedList;
    }


}
