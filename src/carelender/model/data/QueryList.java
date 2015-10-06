package carelender.model.data;

import java.util.HashMap;

/**
 * Used for list queries
 */

public class QueryList extends QueryBase {
	
	private HashMap<PARAM, Object> paramsList = new HashMap<PARAM, Object>();
	
	public QueryList() {
        super(QueryType.LIST);
    }
	
	public void addSearchParam (PARAM key, Object value) {
		this.paramsList.put(key, value);
	}
	
	public HashMap<PARAM, Object> getParamsList () {
		return paramsList;
	}
	
	public enum PARAM {
		DATE_START,
		DATE_END,
		NAME_CONTAINS,
		NAME_EXACT,
		TYPE
	};
}
