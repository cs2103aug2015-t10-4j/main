package carelender.model.data;

/**
 * Base class for all query objects
 */
public class QueryBase {
    private QueryType queryType;

    //Do not allow anyone to create a QueryBase
    protected QueryBase(QueryType queryType) {
        this.queryType = queryType;
    }

    public QueryType getQueryType() {
        return queryType;
    }
}
