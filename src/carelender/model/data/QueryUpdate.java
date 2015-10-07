package carelender.model.data;

import java.util.Date;
import java.util.HashMap;

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
}
