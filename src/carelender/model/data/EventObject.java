package carelender.model.data;

import java.util.Date;

/*
 * Events are stored in this format when returned from the Storage component.
 * EventObjects should be within EventLists when returned from Storage.
 * 		Single objects are returned in an EventList of one element.
 * Used by:
 * 			EventList
 */

public class EventObject {
	private long uid;
	private String name;
	//If timeRange is null, task is a FLOATING_TASK.
	//Deadline tasks have DateRange with same start and end date.
	private DateRange[] dateRange;
	private DateRecurrence dateRecurrence;
	
	public EventObject (long uidToSet, String nameToSet, DateRange[] dateRangetoSet) {
		//TODO: Initialize internal fields.
		this.uid = uidToSet;
		this.name = nameToSet;
		this.dateRange = dateRangetoSet;
	}
	
	public DateRange[] getDateRange () {
		return this.dateRange;
	}
	
	public void setDateRange (DateRange[] dateRangeToSet) {
		this.dateRange = dateRangeToSet;
	}
	
	public Date getLatestDate () {
		Date lastDate = null;
		
		for (DateRange dateR : this.dateRange) {
			if (lastDate == null) {
				lastDate = dateR.getEnd();
			} else {
				if (dateR.getEnd().after(lastDate)) {
					lastDate = dateR.getEnd();
				}
			}
		}
		return lastDate;
	}
	
	public Date getEarliestDate () {
		Date firstDate = null;
		
		for (DateRange dateR : this.dateRange) {
			if (firstDate == null) {
				firstDate = dateR.getStart();
			} else {
				if (dateR.getStart().before(firstDate)) {
					firstDate = dateR.getStart();
				}
			}
		}
		return firstDate;
	}
	
	public String getName () {
		return this.name;
	}
	
	public void setName (String nameToSet) {
		this.name = nameToSet;
	}
	
	public EventType getEventType () {
		if (this.dateRecurrence == null) {
			if (this.dateRange == null) {
				return EventType.FLOATING_TASK;
			} else {
				
			}
		} else {
			if (this.dateRange == null) {
				return EventType.REC_FLOATING_TASK;
			}
		}
		//TODO: Do more type checks.
		return EventType.DEADLINE_TASK;
	}
	
	public enum EventType {
		FLOATING_TASK,
		DEADLINE_TASK,
		SINGLE_RANGE_EVENT,
		MUTLI_RANGE_EVENT,
		REC_FLOATING_TASK,
		REC_DEADLINE_TASK,
		REC_SINGLE_RANGE_EVENT,
		REC_MULTI_RANGE_EVENT
	};
}
