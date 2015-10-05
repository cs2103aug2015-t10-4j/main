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
    ArrayList<CommandKeyword> keywords;
    public Command(String name, QueryType type) {
        command = name.trim();
        this.type = type;
        description = "";
        keywords = new ArrayList<>();
    }

    /**
     * Adds a possible keyword for this command
     *
     * @param type Keyword type
     * @param keyword New keyword
     */
    public void addKeyword(String type, String keyword) {
        keyword = keyword.trim();
        if ( keyword.length() == 0 ) return;
        if ( matchKeyword(keyword) != null ) return;
        keywords.add(new CommandKeyword(keyword, type) );
    }

    /**
     * Adds a list of possible keywords for this command
     *
     * @param type Keyword type
     * @param keywords Comma delimited keyword list
     */
    public void addKeywords(String type, String keywords) {
        String [] keywordArray = keywords.split(",");
        for ( String key: keywordArray ) {
            addKeyword(type,key);
        }
    }
    /**
     * Matches the parts of the command to the keywords
     * @param queryParts Parts of the query
     * @return boolean array with true at the indices of the matched param keywords
     */
    public CommandPart[] processKeywords(String[] queryParts) {
        CommandPart[] commandParts = new CommandPart[queryParts.length];
        for ( int i = 0 ; i < queryParts.length; i++ ) {
            CommandKeyword keyword = matchKeyword(queryParts[i]);
            commandParts[i] = new CommandPart(  queryParts[i],
                                                keyword!=null,
                                                keyword==null?null:keyword.getType());
        }
        return commandParts;
    }

    /**
     * Checks if a keyword exists
     * @param keyword Parameter to check
     * @return null if not exists
     */
    public CommandKeyword matchKeyword(String keyword) {
        for ( CommandKeyword thisKeyword : keywords) {
            if ( thisKeyword.getKeyword().equalsIgnoreCase(keyword) ) return thisKeyword;
        }
        return null;
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
        this.description = description.trim();
    }
}
