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
	//Deadline tasks have DateRange have same start and end date.
	private DateRange[] timeRange;
	private DateRecurrence eventRecurrence;
	
	public EventObject () {
		//TODO: Initialize internal fields.
	}
	
	public EventType getEventType () {
		if (this.eventRecurrence == null) {
			if (this.timeRange == null) {
				return EventType.FLOATING_TASK;
			}
		} else {
			if (this.timeRange == null) {
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
