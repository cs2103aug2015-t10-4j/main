package carelender.model.data;

import java.util.HashMap;

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
}
