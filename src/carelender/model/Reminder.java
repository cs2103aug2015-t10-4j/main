//@@author A0121815N
package carelender.model;

import java.util.Calendar;
import java.util.Date;

/*
 * A reminder object which stores reminder timings
 */
public class Reminder {
	private int uid;
	private Date start;
	private Calendar reTime;
	
	public Reminder(int uid, Date start, Calendar reTime){
		this.uid = uid;
		this.start = start;
		this.reTime = reTime;
	}
	
	public void setReTime(Calendar reTime) {
		this.reTime = reTime;
	}
	
	public void snooze(){
		reTime.add(Calendar.MINUTE, 10);
	}
	
	public int getUid() {
		return uid;
	}
	
	public Calendar getReTime(){
		return reTime;
	}
	
	public Date getStart(){
		return start;
	}
	
	
}
