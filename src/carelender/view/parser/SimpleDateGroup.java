//@@author A0133269A
package carelender.view.parser;

import java.util.Calendar;
import java.util.Date;

/**
 * Used to store data about the date time parts of the query string
 */
public class SimpleDateGroup {
    Date[] dates;
    String text;
    int position;
    boolean hasTime;

    public SimpleDateGroup(Date[] dates, String text, int position, boolean hasTime) {
        this.dates = dates;
        this.text = text;
        this.position = position;
        this.hasTime = hasTime;
        normaliseTimes(!hasTime);
    }

    /**
     * Helper function to normalise the time components of the dates
     * i.e. Remove the second and millisecond parts
     * @param clearTime If true, it will also remove the hour and minute parts
     */
    private void normaliseTimes(boolean clearTime) {
        Calendar calendar = Calendar.getInstance();
        for ( int i = 0 ; i < dates.length; i++ ) {
            calendar.setTime(dates[i]);
            calendar.set(Calendar.MILLISECOND,0);
            calendar.set(Calendar.SECOND,0);
            if (clearTime) {
                calendar.set(Calendar.HOUR_OF_DAY,0);
                calendar.set(Calendar.MINUTE,0);
            }
            dates[i] = calendar.getTime();
        }
    }
}
