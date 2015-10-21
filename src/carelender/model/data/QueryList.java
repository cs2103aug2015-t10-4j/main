package carelender.model.data;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

import carelender.controller.Controller;
import carelender.controller.Search;
import carelender.model.Model;
import carelender.view.TaskRenderer;

/**
 * Used for list queries
 */

public class QueryList extends QueryBase {
	
	private HashMap<SortParam, Comparator<Event>> sortComparators = new HashMap<SortParam, Comparator<Event>>();
	private HashMap<SearchParam, Object> searchParamsList = new HashMap<SearchParam, Object>();
	
	private void defineComparators () {
		sortComparators.put(SortParam.NAME, new Comparator<Event>() {
			@Override
			public int compare(Event eventAgainst, Event eventTo) {
				// TODO Auto-generated method stub
				return eventAgainst.getName().compareTo(eventTo.getName());
			}
        });
        sortComparators.put(SortParam.DATE, new Comparator<Event>() {
			@Override
			public int compare(Event eventAgainst, Event eventTo) {
				// TODO Auto-generated method stub
				if (eventAgainst.getEarliestDate().before(eventTo.getEarliestDate())) {
					return 1;
				} else if (eventAgainst.getEarliestDate().after(eventTo.getEarliestDate())) {
					return -1;
				}
				return 0;
			}
        });
	}
	
	public QueryList() {
        super(QueryType.LIST);
        this.defineComparators();
    }
	
	public QueryList (QueryType type) {
        super(type);
        this.defineComparators();
	}
	
	public void addSearchParam (SearchParam key, Object value) {
		this.searchParamsList.put(key, value);
	}
	
	public HashMap<SearchParam, Object> getSearchParamsList () {
		return this.searchParamsList;
	}
	
	public enum SearchParam {
		DATE_START,
		DATE_END,
		NAME_CONTAINS,
		NAME_EXACT,
		CATEGORY,
		LIMIT,
		SORT
	};
	
	public enum SortParam {
		NAME,
		DATE
	};
	
	@Override
	public void controllerExecute() {
		EventList searchResults = searchExecute();
		
		if ( this.searchParamsList.containsKey(SearchParam.SORT) ) {
			SortParam sortType = (SortParam)this.searchParamsList.get(SearchParam.SORT);
			Collections.sort(searchResults, this.sortComparators.get(sortType));
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("E dd MMM yyyy h:mma Z");
		Controller.displayMessage("Listing events");
		
		if ( this.searchParamsList.containsKey(SearchParam.DATE_START) ) {
			Date fromDate = (Date)this.searchParamsList.get(SearchParam.DATE_START);
			Controller.displayMessage("   from " + dateFormat.format(fromDate));
		}
		if ( this.searchParamsList.containsKey(SearchParam.DATE_END) ) {
			Date toDate = (Date)this.searchParamsList.get(SearchParam.DATE_END);
			Controller.displayMessage("   till " + dateFormat.format(toDate));
		}
		if ( this.searchParamsList.containsKey(SearchParam.NAME_CONTAINS) ) {
			String search = (String)this.searchParamsList.get(SearchParam.NAME_CONTAINS);
			Controller.displayMessage("   matching: " + search);
		}

		if (searchResults.size() > 0) {
			Controller.displayMessage(searchResults.toString());
		} else {
			Controller.displayMessage("No results");
		}

		Controller.setDisplayedList(searchResults);
		Controller.displayTasks(searchResults);
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
