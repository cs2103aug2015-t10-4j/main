package carelender.controller;

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
    String [] temp;
    private HintGenerator() {
        temp = new String[5];
        temp[0] = "It seems you have free time this week, would you like to add more tasks?";
        temp[1] = "You seem busy on sunday, you could move tasks to tuesday instead.";
        temp[2] = "Don't forget all those deadlines tomorrow!";
        temp[3] = "This month looks clear, why not plan a vacation?";
        temp[4] = "You've been planning lot's of tasks recently, take a break.";
    }

    public String getHint() {
        return temp[(int)Math.floor(Math.random()*temp.length)];
    }
}
