package carelender.model.data;

import java.util.Date;

import carelender.controller.Controller;
import carelender.model.Model;

/**
 * Used for add queries
 */
public class QueryAdd extends QueryBase {
    String name;
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
        if ( dateRanges == null ) {
            Controller.displayMessage("Adding new task: ["+name+"]");
        } else {
            Controller.displayMessage("Adding new task: ["+name+"] at ");
            for ( DateRange dateRange : dateRanges ) {
                Controller.displayMessage( "    " + dateRange.toString() );
            }
        }


        /*QueryList checkClashesQuery = new QueryList();
        checkClashesQuery.addSearchParam(QueryList.SearchParam.DATE_START, queryAdd.getTime());
        checkClashesQuery.addSearchParam(QueryList.SearchParam.DATE_END, queryAdd.getTime());
        
        EventList searchResults = search.parseQuery(checkClashesQuery);
        
        //There is at least one task that clashes with the task to add.
        if (searchResults.size() > 0) {
            displayMessage("Task clashes with:");
            displayMessage(searchResults.toString());
        } else { //Add the task to the Model.*/
            Model.getInstance().addEvent(Controller.queryAddToEventObject(this));
        //}
	}

	@Override
	public EventList searchExecute() {
		return null;
	}
    
    
}
