package carelender.model.data;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import carelender.controller.Controller;
import carelender.controller.Search;
import carelender.model.Model;

/**
 * Used for list queries
 */

public class QueryList extends QueryBase {
	private HashMap<SortParam, Comparator<Event>> sortComparators = new HashMap<>();
	private HashMap<SearchParam, Object> searchParamsList = new HashMap<>();
	
	private void defineComparators () {
		sortComparators.put(SortParam.NAME, ( Event eventAgainst, Event eventTo) -> {
				return eventAgainst.getName().compareTo(eventTo.getName());
			}
        );
        sortComparators.put(SortParam.DATE, (Event eventAgainst, Event eventTo) -> {
				if (eventAgainst.getEarliestDate().before(eventTo.getEarliestDate())) {
					return 1;
				} else if (eventAgainst.getEarliestDate().after(eventTo.getEarliestDate())) {
					return -1;
				}
				return 0;
			}
        );
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
		COMPLETE,
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
