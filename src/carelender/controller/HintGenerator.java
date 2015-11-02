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


    String [] availableHints;
	ArrayList<String> hints;
    private HintGenerator() {
		hints = new ArrayList<>();
        availableHints = Model.getInstance().loadStringArray("hints.dat");


        
        dailyEventNumbers = new int[6*7];
		weeklyEventNumbers = new int [6];
		monthlyEventNumber = 0;
    }


    public void generateHints() {

    	Calendar c = Calendar.getInstance();
    	c.setFirstDayOfWeek(Calendar.MONDAY);
    	c.setTime(new Date());
    	int todayIndex = c.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
    	if(todayIndex<0){
    		todayIndex = 6;
    	}

    	int tomorrowIndex = todayIndex++;
    	//Is tomorrow busy
    	if(dailyEventNumbers[tomorrowIndex] > 9) {
    		availableHints[0] = "Don't forget all those deadlines tomorrow!";
    	} else {
    		availableHints[0] = "Have a nice day :)";
    	}
    	//Is the following three days busy
    	int threeDaysTasks = 0;
    	for(int i=0; i<3; i++){
    		threeDaysTasks += dailyEventNumbers[todayIndex + i];
    	}
    	if(threeDaysTasks >= 27){
    		availableHints[1] = "You've been planning lot's of tasks recently, take a break.";
    	} else {
    		availableHints[1] = "Have a nice day :)";
    	}
    	//Is this week busy
    	int sevenDaysTasks = 0;
    	for(int i=0; i<7; i++){
    		sevenDaysTasks += dailyEventNumbers[todayIndex + i];
    	}
    	if(sevenDaysTasks >= 42){
    		availableHints[2] = "It seems you have many tasks this week, would you like to move some tasks to next week?";
    	} else if(sevenDaysTasks < 21) {
    		availableHints[2] = "It seems you have free time this week, would you like to add more tasks?";
    	} else {
    		availableHints[2] = "Have a nice day :)";
    	}
    	//Is this month busy
    	int monthTasks = 0;
    	for(int i=0; i<28; i++){
    		monthTasks += dailyEventNumbers[i];
    	}
    	if(monthTasks < 10) {
    		availableHints[3] = "This month looks clear, why not plan a vacation?";
    	} else {
    		availableHints[3] = "Have a nice day :)";
    	}
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
    
    private void resetDailyEventNumbers(){
    	for(int i=0; i < dailyEventNumbers.length; i++){
    		dailyEventNumbers[i] = 0;
    	}
		monthlyEventNumber = 0;
		for ( int i = 0 ; i < weeklyEventNumbers.length; i++) {
			weeklyEventNumbers[i] = 0;
		}
    }

	public void setDailyEventNumbers(int[][] monthEventNumbers) {
		resetDailyEventNumbers();
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
