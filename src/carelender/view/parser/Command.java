package carelender.view.parser;

import carelender.model.data.QueryType;

import java.util.ArrayList;

/**
 * Used by CommandManager to store a command
 */
public class Command {
    private String command;
    private String description;
    private QueryType type;
    ArrayList<CommandParameter> parameters;
    public Command(String name, QueryType type) {
        command = name;
        this.type = type;
        description = "";
        parameters = new ArrayList<>();
    }

    /**
     * Matches the parts of the command to the parameters
     * @param queryParts Parts of the query
     * @return boolean array with true at the indices of the matched param keywords
     */
    public boolean[] processParameters( String [] queryParts ) {
        boolean [] isParam = new boolean[queryParts.length];
        for ( int i = 0 ; i < queryParts.length; i++ ) {
            isParam[i] = matchParam(queryParts[i]);
        }
        return isParam;
    }

    /**
     * Checks if a parameter exists
     * @param param Parameter to check
     * @return true if it exists
     */
    public boolean matchParam ( String param ) {
        for ( CommandParameter thisParam : parameters ) {
            if ( thisParam.getKeyword().equalsIgnoreCase(param) ) return true;
        }
        return false;
    }
    public String getCommand() {
        return command;
    }

    public QueryType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
