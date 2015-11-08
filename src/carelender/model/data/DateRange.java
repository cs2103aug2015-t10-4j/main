//@@author A0125566B
package carelender.model.data;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/*
 * Data object to help with managing task time ranges.
 * Used by:
 * 			EventObject
 */

public class DateRange implements Serializable{
	private Date start;
	private Date end;
	private boolean hasTime;

	public DateRange ( DateRange dateRange ) {
		start = (Date)dateRange.getStart().clone();
		end = (Date)dateRange.getEnd().clone();
		hasTime = dateRange.hasTime;
	}
	
	public DateRange (Date date, boolean hasTime) {
		start = end = date;
		this.hasTime = hasTime;
	}

	public DateRange (Date startToSet, Date endToSet, boolean hasTime) {
		if (!endToSet.after(startToSet)) {
			this.start = endToSet;
			this.end = startToSet;
		} else {
			this.start = startToSet;
			this.end = endToSet;
		}
		this.hasTime = hasTime;
	}

	
	public Date getEnd () {
		return end;
	}
	
	public void setEnd (Date end) {
		if (this.start != null) {
			if (!end.after(this.start)) {
				this.end = this.start;
				return;
			}
		}
		this.end = end;
	}
	
	public Date getStart () {
		return start;
	}
	
	public void setStart (Date start) {
		if (this.end != null) {
			if (!start.before(this.end)) {
				this.start = this.end;
				return;
			}
		}
		this.start = start;
	}
	
	/**
	 * Return number of days between the start and end date.
	 * 
	 * @return
	 * 		Number of days between two dates.
	 */
	public long getDaysBetween () {
		if ( isRange() ) {
			long difference = this.end.getTime() - this.start.getTime();
			long days = TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS);
		    return days <= 0 ? 0 : days;
		}
		return 0;
	}

    /**
     * Checks if this is a range or just a single point in time
     * @return true if this is a range
     */
    public boolean isRange() {
        return start != end;
    }

	public DateRange copy () {
		return new DateRange(this);
	}

	public String toString () {
		if ( isRange() ) {
			return start.toString() + " - " + end.toString();
		} else {
			return start.toString();
		}
	}

	public boolean hasTime() {
		return hasTime;
	}
}
