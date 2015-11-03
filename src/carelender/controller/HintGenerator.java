package carelender.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import carelender.model.Model;
import carelender.model.data.EventList;

/**
 * This class is to generate the helpful hints that are displayed at the corner
 */
public class HintGenerator {
    private static HintGenerator singleton = null;
    private int[] dailyEventNumbers, weeklyEventNumbers;
	private int monthlyEventNumber;
    public static HintGenerator getInstance() {
        if ( singleton == null ) {
            singleton = new HintGenerator();
        }
        return singleton;
    }

    ArrayList<String> basicHints;
	ArrayList<String> hints;
    private HintGenerator() {
    	hints = new ArrayList<String>();
    	basicHints = Model.getInstance().loadStringArrayList("hints.dat");
        hints = basicHints;
        
        dailyEventNumbers = new int[6*7];
		weeklyEventNumbers = new int [6];
		monthlyEventNumber = 0;
    }

    private int getDayOfWeek() {
        Calendar c = Calendar.getInstance();
        int todayIndex;
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(new Date());
        todayIndex = c.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
        if(todayIndex<0){
            todayIndex = 6;
        }
        return todayIndex;
    }
    
    /*
     * Generate hints for tomorrow
     */
    private void generateHintsForTomorrow(int todayIndex) {
        int tomorrowIndex = todayIndex + 1;
        if(dailyEventNumbers[tomorrowIndex] >= 9) {
        	String newHint = "You have may tasks tomorrow. Rest well today :)";
            hints.add(newHint);
        } else if (dailyEventNumbers[tomorrowIndex] > 3){
        	String newHint = "Don't forget all the deadlines tomorrow!";
            hints.add(newHint);
        } else {
        	String newHint = "It seems that you are having a slack day. Spend some time with your family :)";
            hints.add(newHint);
        }
    }
    
    /*
     * Generate hints for next week
     */
    private void generateHintsForWeek(int todayIndex) {
        int thisWeekEventNumber = weeklyEventNumbers[0];
        int nextWeekEventNumber = weeklyEventNumbers[1];

        if (nextWeekEventNumber > 42) {
            String newHint = "You have " + nextWeekEventNumber + " tasks next week! Be prepared.";
            hints.add(newHint);
        } else if (nextWeekEventNumber > 21) {
            String newHint = "You don't have many tasks next week. Try exercise more :)";
            hints.add(newHint);
        } else {
            if (thisWeekEventNumber < 30) {
                String newHint = "Next week is so free! How nice :)";
                hints.add(newHint);
            } else {
                String newHint = "Next week is so free! Hang on there :)";
                hints.add(newHint);
            }
        }
    }
    
    /*
     * Generate hints for the following 28 days
     */
    private void generateHintsForMonth(){
        if(monthlyEventNumber < 10) {
            String newHint = "This month looks clear, why not plan a vacation?";
            hints.add(newHint);
        } else {
            String newHint = "Have a nice day :)";
            hints.add(newHint);
        }
    }

    private void resetHints() {
        hints = basicHints;
    }

    public void generateHints() {
    	int todayIndex = getDayOfWeek();

        resetHints();

        generateHintsForTomorrow(todayIndex);
        generateHintsForWeek(todayIndex);
        generateHintsForMonth();
    }

	/**
     * Called by the UI to display a random hint every minute or so
     * @return Hint from availableHints
     */
    public String getHint() {
    	int hintIndex = (int)Math.floor(Math.random()* hints.size());
        return hints.get(hintIndex);
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
    
    private void resetEventNumbers(){
    	for(int i=0; i < dailyEventNumbers.length; i++){
    		dailyEventNumbers[i] = 0;
    	}
		monthlyEventNumber = 0;
		for ( int i = 0 ; i < weeklyEventNumbers.length; i++) {
			weeklyEventNumbers[i] = 0;
		}
    }

	public void setDailyEventNumbers(int[][] monthEventNumbers) {
		resetEventNumbers();
		for(int i=0; i < monthEventNumbers.length; i++){
			for(int j=0; j < monthEventNumbers[i].length; j++){
				dailyEventNumbers[i] += monthEventNumbers[i][j];
			}
		}
		for ( int i = 0 ; i < dailyEventNumbers.length; i++) {
			weeklyEventNumbers[i / 7] += dailyEventNumbers[i];
			monthlyEventNumber += dailyEventNumbers[i];
		}
		generateHints();
	}
}
