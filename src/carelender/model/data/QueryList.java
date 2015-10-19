package carelender.model.data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import carelender.controller.Controller;
import carelender.controller.Search;
import carelender.model.Model;

/**
 * Used for list queries
 */

public class QueryList extends QueryBase {
	
	private HashMap<SearchParam, Object> searchParamsList = new HashMap<SearchParam, Object>();
	
	public QueryList() {
        super(QueryType.LIST);
    }
	
	public QueryList (QueryType type) {
        super(type);
    }
	
	public void addSearchParam (SearchParam key, Object value) {
		this.searchParamsList.put(key, value);
	}
	
	public HashMap<SearchParam, Object> getSearchParamsList () {
		return searchParamsList;
	}
	
	public enum SearchParam {
		DATE_START,
		DATE_END,
		NAME_CONTAINS,
		NAME_EXACT,
		TYPE
	};
	
	@Override
	public void controllerExecute() {
		EventList searchResults = searchExecute();

		SimpleDateFormat dateFormat = new SimpleDateFormat("E dd MMM yyyy h:mma Z");
		Controller.displayMessage("Listing events");
		HashMap<QueryList.SearchParam, Object> paramList = getSearchParamsList();
		if ( paramList.containsKey(QueryList.SearchParam.DATE_START) ) {
			Date fromDate = (Date)paramList.get(QueryList.SearchParam.DATE_START);
			Controller.displayMessage("   from " + dateFormat.format(fromDate));
		}
		if ( paramList.containsKey(QueryList.SearchParam.DATE_END) ) {
			Date toDate = (Date)paramList.get(QueryList.SearchParam.DATE_END);
			Controller.displayMessage("   till " + dateFormat.format(toDate));
		}
		if ( paramList.containsKey(QueryList.SearchParam.NAME_CONTAINS) ) {
			String search = (String)paramList.get(QueryList.SearchParam.NAME_CONTAINS);
			Controller.displayMessage("   matching: " + search);
		}

		if (searchResults.size() > 0) {
			Controller.displayMessage(searchResults.toString());
		} else {
			Controller.displayMessage("No results");
		}

		Controller.setDisplayedList(searchResults);
	}

	@Override
	public EventList searchExecute() {
		EventList returnList = new EventList();
		
		//TODO: Replace the null parameter in retrieveEvent to something that makes sense.
		if (Model.getInstance().retrieveEvent() != null) {
			for (EventObject event : Model.getInstance().retrieveEvent()) {
				if (Search.eventMatchesParams(event, getSearchParamsList())) {
					returnList.add(event.copy());
				}
			}
		}
		return returnList;
	}
}
