//@@author A0133269A
package carelender.model.data;

/**
 * Base class for all query objects
 */
public abstract class QueryBase {
    public String userInput;
    private QueryType queryType;

    //Do not allow anyone to create a QueryBase
    protected QueryBase(QueryType queryType) {
        this.queryType = queryType;
    }
    
    public QueryType getQueryType() {
        return queryType;
    }
    
    public abstract void controllerExecute();
    public abstract EventList searchExecute();
}
