package carelender.view.parser;

/**
 * Used by the parser to understand the parts of the command
 */
public class CommandPart {
    private String queryPart;
    private boolean isKeyword;
    private String keywordType;

    public CommandPart(String queryPart, boolean isKeyword, String keywordType) {
        this.queryPart = queryPart;
        this.isKeyword = isKeyword;
        this.keywordType = keywordType;
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
}
