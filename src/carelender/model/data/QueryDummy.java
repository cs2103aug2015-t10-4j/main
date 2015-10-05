package carelender.model.data;

/**
 * Dummy class for query
 */
public class QueryDummy extends QueryBase {
    private String data;
    public QueryDummy(String data) {
        super(QueryType.DUMMY);
        this.data = data;
    }

    public String getData() {
        return data;
    }
}
