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
     * @param c The new command
     */
    public void addCommand(Command c) {
        commands.add(c);
    }

    /**
     * Checks if a command exists
     * @param command Command to check against
     * @return true if command exists
     */
    public boolean matchCommand ( String command ) {
        Iterator<Command> itr = commands.iterator();
        while ( itr.hasNext() ) {
            Command thisCommand = itr.next();
            if ( thisCommand.getCommand().equals( command ) ) {
                return true;
            }
        }
        return false;
    }
}
