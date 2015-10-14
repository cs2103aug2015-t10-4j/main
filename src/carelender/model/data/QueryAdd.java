package carelender.model.data;

import java.util.Date;

/**
 * Used for add queries
 */
public class QueryAdd extends QueryBase {
    String name;
    DateRange [] dateRange;

    public QueryAdd() {
        super(QueryType.ADD);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DateRange[] getDateRange() {
        return dateRange;
    }

    public void setDateRange(DateRange [] dateRange) {
        this.dateRange = dateRange;
    }
    public void setDateRange(DateRange dateRange) {
        this.dateRange = new DateRange[1];
        this.dateRange[0] = dateRange;
    }
}
