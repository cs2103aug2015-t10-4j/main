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
        commands = Model.getInstance().loadStringArray("automation.dat");
    }

    public String getNext() {
        if ( i >= commands.length ) {
            return null;
        }
        return commands[i++];
    }
}
