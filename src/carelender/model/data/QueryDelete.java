package carelender.model.data;

import carelender.controller.Controller;
import carelender.controller.Search;
import carelender.controller.callbacks.OnConfirmedCallback;
import carelender.controller.callbacks.OnEventSelectedCallback;
import carelender.model.Model;

/**
 * Used for delete queries
 */
public class QueryDelete extends QueryBase {
    private String name;
    public QueryDelete() {
        super(QueryType.DELETE);
    }

	private Event selectedObject; // Used for confirmation

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Override
	public void controllerExecute() {
		EventList searchResults = searchExecute();

		final OnConfirmedCallback deleteConfirmedCallback = new OnConfirmedCallback() {
			@Override
			public void onConfirmed(boolean confirmed) {
				if ( confirmed ) {
					Controller.displayMessage("Deleting [" + selectedObject.getInfo() + "]");
					Model.getInstance().deleteEvent(selectedObject);
				}
			}
		};

		final OnEventSelectedCallback deleteCallback = new OnEventSelectedCallback() {
			@Override
			public void onChosen(Event selected) {
				selectedObject = selected;
				Controller.getBlockingStateController()
						.startConfirmation("Are you sure you want to delete \"" + selected.getName() + "\"? [Y/N]", deleteConfirmedCallback);

			}
		};


		if ( searchResults.size() == 0 ) {
			Controller.displayMessage("There is no task called " + getName());
		} else if ( searchResults.size() > 1 ) {
			String message = "There are multiple \""+ getName()+"\" tasks, please choose the one to delete.";
			Controller.getBlockingStateController().startEventSelection(message, searchResults, deleteCallback);
		} else {
			deleteCallback.onChosen(searchResults.get(0));
		}
	}

	@Override
	public EventList searchExecute() {
		EventList returnList = new EventList();
		
		if (Model.getInstance().retrieveEvent() != null) {
			for (Event event : Model.getInstance().retrieveEvent()) {
				if (Search.isEventNameExact(event, getName())) {
					returnList.add(event.copy());
				}
			}
		}
		return returnList;
	}
}
