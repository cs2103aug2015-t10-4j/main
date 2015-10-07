package carelender.model.data;

/**
 * Used for delete queries
 */
public class QueryDelete extends QueryBase {
    private String name;
    public QueryDelete() {
        super(QueryType.DELETE);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
