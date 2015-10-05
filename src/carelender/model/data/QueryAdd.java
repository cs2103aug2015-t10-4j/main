package carelender.model.data;

import java.util.Date;

/**
 * Used for add queries
 */
public class QueryAdd extends QueryBase {
    String name;
    Date time;

    public QueryAdd() {
        super(QueryType.ADD);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
