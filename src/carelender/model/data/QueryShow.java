//@@author A0133907E
package carelender.model.data;

import carelender.controller.Controller;

/**
 * Shows the specified item
 */
public class QueryShow extends QueryBase {
    private Event event;
    public QueryShow(Event event) {
        super(QueryType.SHOW);
        this.event = event;
    }

    @Override
    public void controllerExecute() {
        System.out.println(event.toString());
        Controller.getBlockingStateController().startEventPopup(event);
    }

    @Override
    public EventList searchExecute() {
        return null;
    }
}
