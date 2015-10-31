package carelender.model;

/**
 * Class to automate commands for testing or the presentation
 */
public class CommandAutomation {
    private static CommandAutomation singleton = null;
    public static CommandAutomation getInstance() {
        if ( singleton == null ) {
            singleton = new CommandAutomation();
        }
        return singleton;
    }

    int i = 0;
    String [] commands;
    private CommandAutomation() {
        commands = new String[4];
        commands[0] = "Test1";
        commands[1] = "Test2";
        commands[2] = "Test3";
        commands[3] = "Test4";
    }

    public String getNext() {
        if ( i >= commands.length ) {
            return null;
        }
        return commands[i++];
    }
}
