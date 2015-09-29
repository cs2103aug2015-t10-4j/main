package carelender.model.data;

/*
 * Data object to help with managing recurring tasks.
 * Used by:
 *			EventObject
 */

public class DateRecurrence {
	private int interval;
	private IntervalType intervalType;
	private int numberOfRecurrences;
	
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

	public enum IntervalType {
		DAY,
		WEEK,
		MONTH,
		YEAR
	};
}
