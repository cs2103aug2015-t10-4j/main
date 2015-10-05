package carelender.view.parser;

import java.util.ArrayList;

/**
 * Used by CommandManager to store a command
 */
public class Command {
    private String command;
    ArrayList<CommandParameter> parameters;
    public Command(String name) {
        command = name;
        parameters = new ArrayList<>();
    }

    public String getCommand() {
        return command;
    }
}
