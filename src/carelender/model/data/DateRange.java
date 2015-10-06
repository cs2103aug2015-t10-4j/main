package carelender.model.data;

import java.util.Date;

/*
 * Data object to help with managing task time ranges.
 * Used by:
 * 			EventObject
 */

public class DateRange {
	private Date start;
	private Date end;
	
	public DateRange (Date startToSet, Date endToSet) {
		if (!endToSet.after(startToSet)) {
			this.start = endToSet;
			this.end = startToSet;
		} else {
			this.start = startToSet;
			this.end = endToSet;
		}
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
}
