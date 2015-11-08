package carelender.model.data;

import carelender.controller.Controller;
import carelender.model.Model;
import carelender.model.strings.QueryFeedback;

/**
 * Used for add queries
 */
public class QueryAdd extends QueryBase {
    String name;
    String category;
    DateRange [] dateRanges;

    public QueryAdd() {
        super(QueryType.ADD);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public DateRange[] getDateRange() {
        return dateRanges;
    }

    public void setDateRange(DateRange [] dateRange) {
        this.dateRanges = dateRange;
    }
    public void setDateRange(DateRange dateRange) {
        this.dateRanges = new DateRange[1];
        this.dateRanges[0] = dateRange;
    }

	@Override
	public void controllerExecute() {
        Controller.displayMessage(QueryFeedback.addTask(name));
        Model.getInstance().addEvent(convertToEventObject());
	}

	@Override
	public EventList searchExecute() {
		return null;
	}

    /**
     * Converts this query object into an event object
     * @return Event object
     */
    public Event convertToEventObject() {
        return new Event(0, getName(), getDateRange(), getCategory());
    }
    
    
}
