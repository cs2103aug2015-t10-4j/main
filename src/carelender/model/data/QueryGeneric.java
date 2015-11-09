//@@author A0133269A
package carelender.model.data;

/**
 * Used for queries that do not require any additional data
 */
public class QueryGeneric extends QueryBase {
    public QueryGeneric(QueryType queryType) {
        super(queryType);
    }
    
    @Override
    public void controllerExecute() {
    }

    @Override
    public EventList searchExecute() {
        return null;
    }
}
