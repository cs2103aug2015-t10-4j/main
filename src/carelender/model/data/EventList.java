package carelender.model.data;

import java.util.ArrayList;

/*
 * Returned from Storage, list of tasks and events after operations such as retrieve.
 * Used by:
 * 			Storage
 * 			Logic
 */

public class EventList extends ArrayList<EventObject> {
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        String breakline = "";
        int count = 0;
        for ( EventObject eventObject : this ) {
            count++;
            stringBuilder.append(breakline);
            breakline = System.lineSeparator();
            stringBuilder.append(count);
            stringBuilder.append(". ");
            stringBuilder.append(eventObject.getInfo());
        }
        return stringBuilder.toString();
    }
}
