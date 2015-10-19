package carelender.view.parser;

/**
 * Used by the parser to understand the parts of the command
 */
public class CommandPart {
    private String queryPart; //The original text that was from the user
    private boolean isKeyword; //Is this a keyword?
    private String keywordType; //Type of keyword this is
    private String keywordData; //Data associated with this keyword

    public CommandPart(String queryPart, boolean isKeyword, String keywordType, String keywordData) {
        this.queryPart = queryPart;
        this.isKeyword = isKeyword;
        this.keywordType = keywordType;
        this.keywordData = keywordData;
    }

    public String getQueryPart() {
        return queryPart;
    }

    public boolean isKeyword() {
        return isKeyword;
    }

    public String getKeywordType() {
        return keywordType;
    }

    public String getKeywordData() {
        return keywordData;
    }
}
