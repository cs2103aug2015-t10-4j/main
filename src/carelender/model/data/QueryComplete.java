//@@author A0121815N
package carelender.model.data;

import carelender.controller.Controller;
import carelender.model.Model;
import carelender.model.strings.QueryFeedback;

public class QueryComplete extends QueryBase{
	private EventList events;
	private boolean forComplete;
	
    public QueryComplete(Boolean forComplete) {  	
        super(QueryType.COMPLETE);
        this.forComplete = forComplete;
        events = new EventList();
    }

    public void addEvent ( Event e ) {
        events.add(e.copy());
    }

    public void setEventList ( EventList e ) {
        events = e;
    }
    
    @Override
	public void controllerExecute() {
    	for (int i = 0; i < events.size(); i++) {
    		events.get(i).setCompleted(forComplete);
			Model.getInstance().updateEvent(events.get(i));
		}
    	if (forComplete) {
    		Controller.displayMessage(QueryFeedback.completeTask(events.size()));
    	} else {
    		Controller.displayMessage(QueryFeedback.uncompleteTask(events.size()));
    	}
	}

	@Override
	public EventList searchExecute() {
		EventList returnList = new EventList();
		return returnList;
	}
}
