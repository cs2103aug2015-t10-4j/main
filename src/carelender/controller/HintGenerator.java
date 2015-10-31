package carelender.controller;

import carelender.model.data.EventList;

/**
 * This class is to generate the helpful hints that are displayed at the corner
 */
public class HintGenerator {
    private static HintGenerator singleton = null;
    public static HintGenerator getInstance() {
        if ( singleton == null ) {
            singleton = new HintGenerator();
        }
        return singleton;
    }

    //Reference to the events happening in the next 4 weeks
    private EventList monthEvents;


    String [] availableHints;
    private HintGenerator() {
        availableHints = new String[5];
        availableHints[0] = "It seems you have free time this week, would you like to add more tasks?";
        availableHints[1] = "You seem busy on sunday, you could move tasks to tuesday instead.";
        availableHints[2] = "Don't forget all those deadlines tomorrow!";
        availableHints[3] = "This month looks clear, why not plan a vacation?";
        availableHints[4] = "You've been planning lot's of tasks recently, take a break.";
    }

    /**
     * Sets the event list used for generating the hints
     * @param eventList List of events in the next 4 weeks
     */
    public void setEventList ( EventList eventList ) {
        monthEvents = eventList;
        generateHints();
    }


    public void generateHints() {
        //TODO: Make use of monthEvents to generate hints and put into availableHints
    }

    /**
     * Called by the UI to display a random hint every minute or so
     * @return Hint from availableHints
     */
    public String getHint() {
        return availableHints[(int)Math.floor(Math.random()* availableHints.length)];
    }

    enum HintType {
        HINT,       //Generic hints, like "press up to access previous commands"
        DAY_FREE,
        DAY_BUSY,
        WEEK_FREE,
        WEEK_BUSY,
        MONTH_FREE,
        MONTH_BUSY,
    }
}
