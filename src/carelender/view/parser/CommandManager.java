package carelender.view.parser;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Used by the InputParser to sift through the commands
 */
public class CommandManager {
    ArrayList <Command> commands;
    public CommandManager () {
        commands = new ArrayList<>();
    }

    /**
     * Adds a new command to the list of commands
     * @param command The new command
     */
    public void addCommand(Command command) {
        commands.add(command);
    }

    /**
     * Checks if a command exists
     * @param command Command to check against
     * @return Command that has been matched. Null if no match
     */
    public Command matchCommand ( String command ) {
        for ( Command commandObject : commands ) {
            if ( commandObject.getCommand().equalsIgnoreCase( command ) ) {
                return commandObject;
            }
        }
        return null;
    }
}
