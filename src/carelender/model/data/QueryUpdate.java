package carelender.model.data;

import java.awt.*;
import java.util.Date;
import java.util.HashMap;

import carelender.controller.Search;
import carelender.model.Model;
import carelender.model.data.QueryList.SearchParam;

/**
 * Used for add queries
 */
public class QueryUpdate extends QueryList {
    HashMap<UpdateParam, Object> updateParamsList = new HashMap<UpdateParam, Object>();;

    public QueryUpdate() {
        super(QueryType.UPDATE);
    }
    
    public void addUpdateParam (UpdateParam key, Object value) {
		this.updateParamsList.put(key, value);
	}
	
	public HashMap<UpdateParam, Object> getUpdateParamsList () {
		return updateParamsList;
	}
    
    public enum UpdateParam {
		NAME,
		DATE_RANGE
	};

	@Override
	public void controllerExecute() {
		EventList searchResults = searchExecute();
		for ( Event event : searchResults ) {
			//TODO: This will have to change if we want to do bulk updating.
			HashMap<QueryUpdate.UpdateParam, Object> paramList = getUpdateParamsList();

			if ( paramList.containsKey(QueryUpdate.UpdateParam.NAME) ) {
				String fromName = (String)paramList.get(QueryUpdate.UpdateParam.NAME);
				event.setName(fromName);
			}

			if ( paramList.containsKey(QueryUpdate.UpdateParam.DATE_RANGE) ) {
				DateRange[] fromDateRange = (DateRange[])paramList.get(QueryUpdate.UpdateParam.DATE_RANGE);
				event.setDateRange(fromDateRange);
			}

			//Call Model updateEvent function
			//this.model.updateEvent ( event );
			Model.getInstance().updateEvent(event);
			System.out.println ( event.getName() );
		}
	}

	@Override
	public EventList searchExecute() {
		EventList returnList = new EventList();
		//TODO: Replace the null parameter in retrieveEvent to something that makes sense.
		if (Model.getInstance().retrieveEvent() != null) {
			for (Event event : Model.getInstance().retrieveEvent()) {
				if (Search.eventMatchesParams(event, getSearchParamsList())) {
					returnList.add(event.copy());
				}
			}
		}
		return returnList;
	}
}
