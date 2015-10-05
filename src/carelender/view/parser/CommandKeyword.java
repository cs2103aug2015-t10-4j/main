package carelender.view.parser;

/**
 * Used by the Command class to store keywords
 */
public class CommandKeyword {
    String keyword;
    String type;

    public CommandKeyword(String keyword, String type) {
        this.keyword = keyword;
        this.type = type;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getType() {
        return type;
    }
}
