package carelender.model.data;

/**
 * Base class for all query objects
 */
public class QueryObject {
    private QueryType queryType;

    //Do not allow anyone to create a QueryObject
    protected QueryObject(QueryType queryType) {
        this.queryType = queryType;
    }

    public QueryType getQueryType() {
        return queryType;
    }

    public enum QueryType {
        DUMMY, ADD, DELETE, EDIT
    }
}
