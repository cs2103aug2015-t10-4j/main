package carelender.view.parser;

import carelender.model.data.QueryBase;
import carelender.model.data.QueryDummy;
import jdk.internal.util.xml.impl.Input;

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

        newCommand = new Command("add");
        commandManager.addCommand(newCommand);

        newCommand = new Command("list");
        commandManager.addCommand(newCommand);

        newCommand = new Command("delete");
        commandManager.addCommand(newCommand);

        newCommand = new Command("help");
        commandManager.addCommand(newCommand);
    }

    public QueryBase parseCompleteInput ( String input ) {
        //TODO: Determine what kind of query this is
        QueryBase newQuery;
        //Dummy code
        newQuery = new QueryDummy(input);
        return newQuery;
    }
}
