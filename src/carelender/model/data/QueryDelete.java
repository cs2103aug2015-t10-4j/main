package carelender.model.data;

import carelender.controller.Controller;
import carelender.controller.callbacks.OnConfirmedCallback;
import carelender.model.Model;
import carelender.model.strings.QueryFeedback;

/**
 * Used for delete queries
 */
public class QueryDelete extends QueryBase {
    private EventList events;
    public QueryDelete() {
        super(QueryType.DELETE);
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
        final OnConfirmedCallback deleteConfirmedCallback = new OnConfirmedCallback() {
            @Override
            public void onConfirmed(boolean confirmed) {
                if ( confirmed ) {
                    Model.getInstance().deleteEvent(events);
                    Controller.displayMessage(QueryFeedback.deleteTask(events.size()));
                } else {
                    Controller.displayMessage(QueryFeedback.deleteCancelled());
                }
                Controller.refreshDisplay();
            }
        };

        if ( events != null && events.size() > 0 ) {
            if ( events.size() == 1 ) {
                deleteConfirmedCallback.onConfirmed(true);
            } else {
                Controller.getBlockingStateController()
                        .startConfirmation(QueryFeedback.deleteConfirmation(events.size()), deleteConfirmedCallback);
            }
        }


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
