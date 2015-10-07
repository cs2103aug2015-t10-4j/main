package carelender.model.data;

import java.util.HashMap;

/**
 * Used for list queries
 */

public class QueryList extends QueryBase {
	
	private HashMap<SearchParam, Object> paramsList = new HashMap<SearchParam, Object>();
	
	public QueryList() {
        super(QueryType.LIST);
    }
	
	public void addSearchParam (SearchParam key, Object value) {
		this.paramsList.put(key, value);
	}
	
	public HashMap<SearchParam, Object> getParamsList () {
		return paramsList;
	}
	
	public enum SearchParam {
		DATE_START,
		DATE_END,
		NAME_CONTAINS,
		NAME_EXACT,
		TYPE
	};
}
