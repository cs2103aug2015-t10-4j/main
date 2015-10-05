package carelender.model.data;

/**
 * Used for queries that cannot be parsed
 */
public class QueryError extends QueryBase {
    private String message;

    public QueryError(String message) {
        super(QueryType.ERROR);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
