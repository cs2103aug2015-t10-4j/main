# A0121815Nunused
###### carelender\model\data\QueryRemind.java
``` java
package carelender.model.data;


import carelender.controller.Controller;
import carelender.model.ReminderManager;
import carelender.model.strings.QueryFeedback;

import java.util.Calendar;

public class QueryRemind extends QueryBase {
    private EventList events;
    private int minutes = 0;

    public QueryRemind() {
        super(QueryType.REMINDER);
        events = new EventList();
    }

    public void setOffset ( int minutes ) {
        this.minutes = minutes;
    }

    public void addEvent ( Event e ) {
        events.add(e.copy());
    }

    public void setEventList ( EventList e ) {
        events = e;
    }

    @Override
    public void controllerExecute() {
        for ( Event event : events ) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(event.getEarliestDate());
            calendar.add(Calendar.MINUTE, -minutes);
            ReminderManager.getInstance().addReminder(event.copy(), calendar);
        }

        Controller.clearMessages();
        if ( events.size() == 1 ) {
            Controller.displayMessage(QueryFeedback.reminderAdded());
        } else {
            Controller.displayMessage(QueryFeedback.reminderAdded(events.size()));
        }
    }

    @Override
    public EventList searchExecute() {
        return null;
    }
}
```
###### carelender\model\Reminder.java
``` java
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
```
###### carelender\model\ReminderList.java
``` java
package carelender.model;


import java.util.ArrayList;

/*
 * Reminders List for Reminder Objects
 */
public class ReminderList extends ArrayList<Reminder>{

    /**
     * Default Serial ID
     */
    private static final long serialVersionUID = 1L;


}
```
###### carelender\model\ReminderManager.java
``` java
package carelender.model;

import java.util.Calendar;
import java.util.PriorityQueue;

import carelender.model.data.Event;

/**
 * Handles reminders for events
 */
public class ReminderManager {
    private static ReminderManager singleton = null;

    public static ReminderManager getInstance() {
        if (singleton == null) {
            singleton = new ReminderManager();
        }
        return singleton;
    }

    private PriorityQueue<Reminder> reminders;

    private ReminderManager() {
        reminders = new PriorityQueue<>();
    }

    public boolean addReminder(Event eventObj, Calendar reTime) {
        Reminder reminder = new Reminder((int) eventObj.getUid(), eventObj.getEarliestDate(), reTime);
        reminders.add(reminder);
        return true;
    }

    /**
     * Get reminders which have been triggered
     * @return List of reminders which have been triggered
     */
    public ReminderList getReminders() {
        Calendar cal = Calendar.getInstance();

        ReminderList reminderList = new ReminderList();
        while (!reminders.isEmpty()) {
            if (reminders.peek().getReTime().compareTo(cal) == -1) {
                reminderList.add(reminders.poll());
            } else {
                break;
            }
        }
        return reminderList;
    }

    /**
     * Readds reminder into the reminders PriorityQueue
     * @param reminder
     */
    public void snoozeReminder(Reminder reminder){
        reminder.snooze();
        reminders.add(reminder);
    }

}
```
