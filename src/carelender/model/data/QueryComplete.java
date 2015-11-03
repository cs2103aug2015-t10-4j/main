package carelender.model.data;

import carelender.controller.Controller;
import carelender.controller.callbacks.OnConfirmedCallback;
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

	//private Event selectedObject; // Used for confirmation

    public void addEvent ( Event e ) {
        events.add(e.copy());
    }

    public void setEventList ( EventList e ) {
        events = e;
    }
    
    @Override
	public void controllerExecute() {
    	for (int i = 0; i < events.size(); i++) {
			Model.getInstance().setComplete(events.get(i), forComplete);
		}
    	if (forComplete) {
    		Controller.displayMessage(QueryFeedback.completeTask(events.size()));
    	} else {
    		Controller.displayMessage(QueryFeedback.uncompleteTask(events.size()));
    	}
    	
		/*Controller.clearMessages();
		final OnConfirmedCallback deleteConfirmedCallback = new OnConfirmedCallback() {
			@Override
			public void onConfirmed(boolean confirmed) {
				if ( confirmed ) {
					Model.getInstance().deleteEvent(events);
					Controller.displayMessage("Deleting " + events.size() + " tasks");
				} else {
					Controller.displayMessage("Cancelled delete");
				}
				Controller.refreshDisplay();
			}
		};

        if ( events != null && events.size() > 0) {
            Controller.getBlockingStateController()
                    .startConfirmation("Are you sure you want to delete " + events.size() + " events? [Y/N]", deleteConfirmedCallback);
        }*/


		/*final OnEventSelectedCallback deleteCallback = new OnEventSelectedCallback() {
			@Override
			public void onChosen(Event selected) {
				selectedObject = selected;
				Controller.getBlockingStateController()
						.startConfirmation("Are you sure you want to delete \"" + selected.getName() + "\"? [Y/N]", deleteConfirmedCallback);
			}
		};*/

		/*if ( searchResults.size() == 0 ) {
			Controller.displayMessage("There is no task called " + getName());
		} else if ( searchResults.size() > 1 ) {
			String message = "There are multiple \""+ getName()+"\" tasks, please choose the one to delete.";
			Controller.getBlockingStateController().startEventSelection(message, searchResults, deleteCallback);
		} else {
			deleteCallback.onChosen(searchResults.get(0));
		}*/


	}

	@Override
	public EventList searchExecute() {
		EventList returnList = new EventList();
		
		/*if (Model.getInstance().retrieveEvent() != null) {
			for (Event event : Model.getInstance().retrieveEvent()) {
				if (Search.isEventNameExact(event, getName())) {
					returnList.add(event.copy());
				}
			}
		}*/
		return returnList;
	}
}
