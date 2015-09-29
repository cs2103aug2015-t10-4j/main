package carelender.model.data;

/**
 * Dummy class for query
 */
public class QueryDummy extends QueryObject {
    private String data;
    public QueryDummy(String data) {
        super(QueryType.DUMMY);
        this.data = data;
    }

    public String getData() {
        return data;
    }
}
