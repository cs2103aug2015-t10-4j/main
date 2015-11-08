//@@author A0125566B
package carelender.model.data;

import java.util.HashMap;

import carelender.controller.Controller;
import carelender.controller.callbacks.OnConfirmedCallback;
import carelender.model.Model;
import carelender.model.strings.QueryFeedback;

/**
 * Used for add queries
 */
public class QueryUpdate extends QueryList {
    HashMap<UpdateParam, Object> updateParamsList = new HashMap<>();;
    EventList updateList;
    
    public QueryUpdate() {
        super(QueryType.UPDATE);
        updateList = new EventList();
    }
    
    public void addEvent ( Event e ) {
    	updateList.add(e.copy());
    }
    
    public void addUpdateParam (UpdateParam key, Object value) {
		this.updateParamsList.put(key, value);
	}
	
	public HashMap<UpdateParam, Object> getUpdateParamsList () {
		return updateParamsList;
	}
    
    public enum UpdateParam {
		NAME,
		DATE_RANGE,
		CATEGORY
	};

	@Override
	public void controllerExecute() {
		Controller.clearMessages();
		
		final OnConfirmedCallback updateConfirmedCallback = new OnConfirmedCallback() {
			@Override
			public void onConfirmed(boolean confirmed) {
				if ( confirmed ) {
					for ( Event event : updateList ) {
						System.out.println("UPDATING:" + (String)updateParamsList.get(QueryUpdate.UpdateParam.NAME));
						if ( updateParamsList.containsKey(QueryUpdate.UpdateParam.NAME) ) {
							String fromName = (String)updateParamsList.get(QueryUpdate.UpdateParam.NAME);
							event.setName(fromName);
						}

						if ( updateParamsList.containsKey(QueryUpdate.UpdateParam.DATE_RANGE) ) {
							DateRange[] fromDateRange = (DateRange[])updateParamsList.get(QueryUpdate.UpdateParam.DATE_RANGE);
							event.setDateRange(fromDateRange);
						}
						
						if ( updateParamsList.containsKey(QueryUpdate.UpdateParam.CATEGORY) ) {
							String category = (String)updateParamsList.get(QueryUpdate.UpdateParam.CATEGORY);
							event.setCategory(category);
						}
						
						Model.getInstance().updateEvent(event);
						System.out.println(event.getName());
					}
					Controller.refreshDisplay();
				} else {
					Controller.displayMessage(QueryFeedback.updateCancelled());
				}
			}
		};

		if ( updateList.size() > 1 ) {
			if ( updateParamsList.containsKey(QueryUpdate.UpdateParam.DATE_RANGE) ) {
				Controller.displayMessage("Cannot bulk update dates! Will cause conflicts");
			} else {
				Controller.getBlockingStateController()
		        .startConfirmation("Are you sure you want to update " + updateList.size() + " events? [Y/N]", updateConfirmedCallback);
			}
		} else if ( updateList.size() == 1 ) {
			updateConfirmedCallback.onConfirmed(true);
		}
		
	}

	@Override
	public EventList searchExecute() {
		EventList returnList = new EventList();
		return returnList;
	}
}
