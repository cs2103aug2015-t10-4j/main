//@@author A0125566B
package carelender.model.data;

import carelender.model.strings.DateFormats;

import java.io.Serializable;
import java.util.Date;

/**
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
    private String category;

    public Event ( Event eventObject ) {
        uid = eventObject.uid;
        name = eventObject.name;
        category = eventObject.category;
        completed = eventObject.completed;
        DateRange [] eventObjectDateRange = eventObject.dateRange;
        
        if ( eventObjectDateRange != null ) {
            dateRange = new DateRange[eventObjectDateRange.length];
            for ( int i = 0 ; i < eventObjectDateRange.length; i++ ) {
                dateRange[i] = eventObjectDateRange[i].copy();
            }
        } else {
            dateRange = null;
        }

        if(dateRecurrence != null) {
            dateRecurrence = eventObject.dateRecurrence.copy();
        }
    }
    public Event (int uidToSet, String nameToSet, DateRange[] dateRangetoSet, String category) {
        this.uid = uidToSet;
        this.name = nameToSet;
        this.dateRange = dateRangetoSet;
        this.category = category;
    }

    public void setUid(int uid){
        this.uid = uid;
    }
    public int getUid() {
        return uid;
    }

    public void setName (String nameToSet) {
        this.name = nameToSet;
    }
    public String getName () {
        return this.name;
    }
  
    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
    public boolean getCompleted(){
        return completed;
    }
    
    public void setDateRange (DateRange[] dateRangeToSet) {
        this.dateRange = dateRangeToSet;
    }
    public DateRange[] getDateRange () {
        return this.dateRange;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateCreated() {
        return dateCreated;
    }
 
    public void setCategory(String category) {
        this.category = category;
    }
    public String getCategory() {
        return category;
    }
    
    //@@author A0125566B
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

    //@@author A0125566B
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
    
    //@@author A0125566B
    public Date getEarliestDateFromNow () {
        Date currentDate = new Date();
        Date firstDate = null;
        if ( this.dateRange == null ) return null;
        for (DateRange dateR : this.dateRange) {
            if (dateR.getStart().after(currentDate) || dateR.getEnd().after(currentDate)) {
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
        return EventType.DEADLINE_TASK;
    }

    public String getInfo() {
        String dateString = DateFormats.DEBUG_FORMAT.format(getEarliestDate());
        return this.uid + " | " + this.name + " | " + dateString;
    }
    //@@author A0133269A
    public String getDetailedInfo() {
        String dateConcat = " ";
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append(" | ");

        for ( DateRange dateRange : this.dateRange ) {
            sb.append(dateConcat);
            dateConcat = ", ";

            String startString = DateFormats.DEBUG_FORMAT.format(dateRange.getStart());
            sb.append(startString);
            if ( dateRange.isRange() ) {
                String endString = DateFormats.DEBUG_FORMAT.format(dateRange.getEnd());
                sb.append(" - ");
                sb.append(endString);
            }
        }

        if ( category != null ) {
            sb.append( "| cat: ");
            sb.append(category);
            sb.append(" ");
        }

        if ( completed ) {
            sb.append("| complete");
        } else {
            sb.append("| incomplete");
        }
        return sb.toString();
    }

    public Event copy () {
        return new Event(this);
    }
    //@@author A0125566B
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
