//@@author A0121815N
package carelender.model.data;


import carelender.controller.Controller;
import carelender.model.ReminderManager;
import carelender.model.strings.QueryFeedback;

import java.util.Calendar;

public class QueryRemind extends QueryBase {
    private EventList events;
    private int minutes = 0;

    public QueryRemind() {
        super(QueryType.REMINDER);
        events = new EventList();
    }

    public void setOffset ( int minutes ) {
        this.minutes = minutes;
    }

    public void addEvent ( Event e ) {
        events.add(e.copy());
    }

    public void setEventList ( EventList e ) {
        events = e;
    }

    @Override
    public void controllerExecute() {
        for ( Event event : events ) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(event.getEarliestDate());
            calendar.add(Calendar.MINUTE, -minutes);
            ReminderManager.getInstance().addReminder(event.copy(), calendar);
        }

        Controller.clearMessages();
        if ( events.size() == 1 ) {
            Controller.displayMessage(QueryFeedback.reminderAdded());
        } else {
            Controller.displayMessage(QueryFeedback.reminderAdded(events.size()));
        }
    }

    @Override
    public EventList searchExecute() {
        return null;
    }
}
