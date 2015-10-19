package carelender.view.parser;

import carelender.model.data.QueryType;

import java.util.ArrayList;

/**
 * Used by CommandManager to store a command
 */
public class Command {
    private String command;
    private String description;
    private String usage;
    
    

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
     * @param dataPosition Place to search for the data related to this keyword
     */
    public void addKeyword(String type, String keyword, CommandKeyword.DataPosition dataPosition) {
        keyword = keyword.trim();
        if ( keyword.length() == 0 ) return;
        if ( matchKeyword(keyword) != null ) return;
        keywords.add(new CommandKeyword(keyword, type, dataPosition) );
    }

    /**
     * Adds a list of possible keywords for this command
     *
     * @param type Keyword type
     * @param keywords Comma delimited keyword list
     * @param dataPosition Place to search for the data related to this keyword
     */
    public void addKeywords(String type, String keywords, CommandKeyword.DataPosition dataPosition) {
        String [] keywordArray = keywords.split(",");
        for ( String key: keywordArray ) {
            addKeyword(type,key, dataPosition);
        }
    }
    /**
     * Matches the parts of the command to the keywords
     * @param queryParts Parts of the query
     * @return boolean array with true at the indices of the matched param keywords
     */
    public CommandPart[] processKeywords(String[] queryParts) {
        queryParts = concatenateQueryParts(queryParts);
        CommandPart[] commandParts = new CommandPart[queryParts.length];
        for ( int i = 0 ; i < queryParts.length; i++ ) { //Start from
            CommandKeyword keyword = matchKeyword(queryParts[i]);
            String keywordData = null;
            if ( keyword != null ) {
                if ( keyword.getDataPosition() == CommandKeyword.DataPosition.AFTER ) {
                    if ( i < queryParts.length - 1 ) {
                        keywordData = queryParts[i + 1];
                    }
                } else if (keyword.getDataPosition() == CommandKeyword.DataPosition.BEFORE) {
                    if ( i > 0 ) {
                        keywordData = queryParts[i - 1];
                    }
                }
            }
            commandParts[i] = new CommandPart(  queryParts[i],
                                                keyword!=null,
                                                keyword==null?null:keyword.getType(),
                                                keywordData
                                                );
        }
        return commandParts;
    }

    /**
     * Merges the parts of a string that are surrounded by keywords
     * Does not include the command itself.
     * @return Merged string array
     */
    public String [] concatenateQueryParts(String[] queryParts) {
        ArrayList <String> parts = new ArrayList<>();
        int startingIndex = 1;
        int i = 1;
        while ( true ) {
            if ( i > queryParts.length ) break;
            if ( i == queryParts.length || matchKeyword(queryParts[i]) != null) {
                StringBuilder stringBuilder = new StringBuilder();
                for ( int j = startingIndex; j < i; j++ ) {
                    stringBuilder.append(queryParts[j]);
                    stringBuilder.append(" ");
                }
                parts.add(stringBuilder.toString().trim());
                if ( i < queryParts.length ) {
                    parts.add(queryParts[i]);
                }
                startingIndex = i + 1;
            }
            i++;
        }

        return parts.toArray(new String[parts.size()]);
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
    
    public String getUsage() {
		return usage;
	}

	public void setUsage(String usage) {
		this.usage = usage;
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
