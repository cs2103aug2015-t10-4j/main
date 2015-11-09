# A0125566Bunused
###### carelender\model\data\DateRecurrence.java
``` java
package carelender.model.data;

import java.io.Serializable;

/*
 * Data object to help with managing recurring tasks.
 * Used by:
 *			EventObject
 *
 * Currently not implemented as task manager scope was narrowed to not include recurring dates.
 */

public class DateRecurrence implements Serializable{
    private int interval;
    private IntervalType intervalType;
    private int numberOfRecurrences;

    public DateRecurrence ( DateRecurrence dateRecurrence ) {
        interval = dateRecurrence.interval;
        numberOfRecurrences = dateRecurrence.numberOfRecurrences;
        intervalType = dateRecurrence.getIntervalType();
    }
    public DateRecurrence (int interval, IntervalType intervalType, int numberOfRecurrences ) {
        this.interval = interval;
        this.intervalType = intervalType;
        this.numberOfRecurrences = numberOfRecurrences;
    }

    public int getInterval() {
        return interval;
    }

    public IntervalType getIntervalType() {
        return intervalType;
    }

    public int getNumberOfRecurrences() {
        return numberOfRecurrences;
    }

    public DateRecurrence copy() {
        return new DateRecurrence(this);
    }
    public enum IntervalType {
        DAY,
        WEEK,
        MONTH,
        YEAR
    };
}
```
