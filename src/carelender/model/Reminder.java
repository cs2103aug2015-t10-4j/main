package carelender.model;

import java.util.Calendar;
import java.util.Date;

public class Reminder {
	private int uid;
	private Date start;
	private Calendar reTime;
	
	public Reminder(int uid, Date start, Calendar reTime){
		this.uid = uid;
		this.start = start;
		this.reTime = reTime;
	}
	
	public Calendar getReTime(){
		return reTime;
	}
	
	public Date getStart(){
		return start;
	}
	
}
