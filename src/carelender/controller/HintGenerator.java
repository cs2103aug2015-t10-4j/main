//@@author A0133907E
package carelender.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import carelender.model.Model;

/**
 * This class is to generate the helpful hints that are displayed under tabs
 */
public class HintGenerator {
    private static HintGenerator singleton = null;
    private int[] dailyEventNumbers, weeklyEventNumbers;
	private int monthlyEventNumber;
	
	private static final int SIX_WEEKS = 42;
	private static final int SIX_DAYS = 6;
	private static final int DAYS_PER_WEEK = 7;
	private static final int MONDAY_INDEX = 0;
	private static final int SUNDAY_INDEX = 6;
	private static final int MONTHLY_VACATION_THRESHOLD = 10;
	private static final int WEEKLY_FREE_THRESHOLD = 18;
	private static final int WEEKLY_BUSY_THRESHOLD = 30;
	private static final int DAILY_FREE_THRESHOLD = 3;
	private static final int DAILY_BUSY_THRESHOLD = 6;
	
    private static final ArrayList<String> basicHints = Model.getInstance().loadStringArrayList("hints.dat");
	private ArrayList<String> hints;

    private HintGenerator() {
    	hints = new ArrayList<String>();
        hints = basicHints;

        dailyEventNumbers = new int[SIX_WEEKS];
		weeklyEventNumbers = new int [SIX_DAYS];
		monthlyEventNumber = 0;
    }

    public static HintGenerator getInstance() {
        if ( singleton == null ) {
            singleton = new HintGenerator();
        }
        return singleton;
    }
    
    /**
     * Called by CalendarRenderer to pass a 2-D array monthEventNumbers into HintGenerator
     * Update dailyEventNumbers, weeklyEventNumbers and monthlyEventNumber accordingly
     * @param monthEventNumbers
     */
	public void setDailyEventNumbers(int[][] monthEventNumbers) {
		resetEventNumbers();

		//Update dailyEventNumbers
		for(int i=0; i < monthEventNumbers.length; i++){
			for(int j=0; j < monthEventNumbers[i].length; j++){
				dailyEventNumbers[i] += monthEventNumbers[i][j];
			}
		}

		//Update weeklyEventNumbers and monthlyEventNumber
		for ( int i = 0 ; i < dailyEventNumbers.length; i++) {
			weeklyEventNumbers[i / DAYS_PER_WEEK] += dailyEventNumbers[i];
			monthlyEventNumber += dailyEventNumbers[i];
		}

		generateHints();
	}
    
    /**
     * Calculate the day of week (e.g. Monday, Friday) for today
     * @return
     */
    private int getDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        int todayIndex;
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(new Date());
        todayIndex = calendar.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
        //Originally Sunday - Monday = -1 while a 6 is expected.
        if( todayIndex < MONDAY_INDEX ){
            todayIndex = SUNDAY_INDEX;
        }
        return todayIndex;
    }
    
    /**
     * Reset hints to the default set (basicHints)
     */
    private void resetHints() {
        hints = basicHints;
    }
    
    /**
     * Generate hints according to the number of tasks in different time ranges
     */
    public void generateHints() {
    	int todayIndex = getDayOfWeek();

        resetHints();

        generateHintsForTomorrow(todayIndex);
        generateHintsForWeek(todayIndex);
        generateHintsForMonth();
    }

    /**
     * Generate hints for tomorrow
     */
    private void generateHintsForTomorrow(int todayIndex) {
        int tomorrowIndex = todayIndex + 1;
        if(dailyEventNumbers[tomorrowIndex] > DAILY_BUSY_THRESHOLD) {
        	String newHint = "You have may tasks tomorrow. Rest well today :)";
            hints.add(newHint);
        } else if (dailyEventNumbers[tomorrowIndex] > DAILY_FREE_THRESHOLD){
        	String newHint = "Don't forget your " + dailyEventNumbers[tomorrowIndex] + " deadlines tomorrow!";
            hints.add(newHint);
        } else {
        	String newHint = "It seems that you are having a slack day. Spend some time with your family :)";
            hints.add(newHint);
        }
    }
    
    /**
     * Generate hints for next week
     */
    private void generateHintsForWeek(int todayIndex) {
        int thisWeekEventNumber = weeklyEventNumbers[0];
        int nextWeekEventNumber = weeklyEventNumbers[1];

        if (nextWeekEventNumber > WEEKLY_BUSY_THRESHOLD) {
            String newHint = "You have " + nextWeekEventNumber + " tasks next week! Be prepared.";
            hints.add(newHint);
        } else if (nextWeekEventNumber > WEEKLY_FREE_THRESHOLD) {
            String newHint = "You don't have many tasks next week. Try exercise more :)";
            hints.add(newHint);
        } else {
            if (thisWeekEventNumber < WEEKLY_FREE_THRESHOLD) {
                String newHint = "Next week is so free! How nice :)";
                hints.add(newHint);
            } else {
                String newHint = "Next week is so free! Hang on there :)";
                hints.add(newHint);
            }
        }
    }
    
    /**
     * Generate hints for the following 28 days
     */
    private void generateHintsForMonth(){
        if(monthlyEventNumber < MONTHLY_VACATION_THRESHOLD) {
            String newHint = "This month looks clear, why not plan a vacation?";
            hints.add(newHint);
        } else {
            String newHint = "Have a nice day :)";
            hints.add(newHint);
        }
    }
    
	/**
     * Called by the UI to display a random hint every minute or so
     * @return Hint from availableHints
     */
    public String getHint() {
    	int hintIndex = (int)Math.floor(Math.random()* hints.size());
        return hints.get(hintIndex);
    }
    
    /**
     * Reset dailyEventNumbers, weeklyEventNumbers and monthlyEventNumber
     */
    private void resetEventNumbers(){
    	for( int i = 0; i < dailyEventNumbers.length; i++ ){
    		dailyEventNumbers[i] = 0;
    	}
		monthlyEventNumber = 0;
		for ( int i = 0 ; i < weeklyEventNumbers.length; i++ ) {
			weeklyEventNumbers[i] = 0;
		}
    }

    /**
     * This ENUM is just a reference on different types of hints
     */
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
