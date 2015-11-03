package carelender.model.data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * Events are stored in this format when returned from the Storage component.
 * EventObjects should be within EventLists when returned from Storage.
 * 		Single objects are returned in an EventList of one element.
 * Used by:
 * 			EventList
 */

public class Event implements Serializable{
    private int uid;
    private String name;
    private boolean completed;
    //If dateRange is null, task is a FLOATING_TASK.
    //Deadline tasks have DateRange with same start and end date.
    private DateRange[] dateRange;
    private DateRecurrence dateRecurrence;
    private Date dateCreated;

    public Event ( Event eventObject ) {
        uid = eventObject.uid;
        name = eventObject.name;
        DateRange [] eventObjectDateRange = eventObject.dateRange;
        dateRange = new DateRange[eventObjectDateRange.length];
        for ( int i = 0 ; i < eventObjectDateRange.length; i++ ) {
            dateRange[i] = eventObjectDateRange[i].copy();
        }
        
        /*if ( eventObjectDateRange != null ) {
            dateRange = new DateRange[eventObjectDateRange.length];
            for ( int i = 0 ; i < eventObjectDateRange.length; i++ ) {
                dateRange[i] = eventObjectDateRange[i].copy();
            }
        } else {
            dateRange = null;
        }*/

        if(dateRecurrence != null) {
            dateRecurrence = eventObject.dateRecurrence.copy();
        }
    }
    public Event (int uidToSet, String nameToSet, DateRange[] dateRangetoSet) {
        //TODO: Initialize internal fields.
        this.uid = uidToSet;
        this.name = nameToSet;
        this.dateRange = dateRangetoSet;
    }

    public void setUid(int uid){
        this.uid = uid;
    }
    
    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
    
    public void setDateRange (DateRange[] dateRangeToSet) {
        this.dateRange = dateRangeToSet;
    }
    
    public Date getDateCreated() {
        return dateCreated;
    }
    
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
    
    public boolean getCompleted(){
        return completed;
    }
    
    public DateRange[] getDateRange () {
        return this.dateRange;
    }
    
    public Date getLatestDate () {
        Date lastDate = null;
        if ( this.dateRange == null ) return null;
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
        if ( this.dateRange == null ) return null;
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
    
    public Date getEarliestDateFromNow () {
        Date currentDate = new Date();
        Date firstDate = null;
        if ( this.dateRange == null ) return null;
        for (DateRange dateR : this.dateRange) {
            if (dateR.getStart().after(currentDate)) {
                if (firstDate == null) {
                    firstDate = dateR.getStart();
                } else {
                    if (dateR.getStart().before(firstDate)) {
                        firstDate = dateR.getStart();
                    }
                }
            }
        }
        return firstDate;
    }

    public int getUid() {
        return uid;
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

    public String getInfo() {
        String dateString = new SimpleDateFormat("E dd MMM h:mma").format(getEarliestDate());
        return dateString + " | " + this.name;
    }

    public Event copy () {
        return new Event(this);
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
