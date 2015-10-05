package carelender.view.parser;

/**
 * Used by the Command class to store parameters
 */
public class CommandParameter {
    String keyword;

    public CommandParameter(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }
}
