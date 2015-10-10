package carelender.model.data;

/**
 * Used for queries that cannot be parsed
 */
public class QueryError extends QueryBase {
    private String message;
    private boolean isHelpDisplayed;

    public QueryError(String message) {
        super(QueryType.ERROR);
        this.message = message;
    }
    public QueryError(String message, boolean isHelpDisplayed) {
        super(QueryType.ERROR);
        this.message = message;
        this.isHelpDisplayed = isHelpDisplayed;
    }

    public String getMessage() {
        return message;
    }

    public boolean isHelpDisplayed() {
        return isHelpDisplayed;
    }
}
