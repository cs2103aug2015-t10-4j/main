//@@author A0133269A
package carelender.model.strings;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Contains all the date formats
 */
public class DateFormats {
    public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");
    public static final SimpleDateFormat DAY_IN_YEAR = new SimpleDateFormat("D");
    public static final SimpleDateFormat YEAR = new SimpleDateFormat("y");
    public static final SimpleDateFormat DATE_FORMAT_DAY = new SimpleDateFormat("d EEE");
    public static final SimpleDateFormat DATE_FORMAT_MONTH = new SimpleDateFormat("dd MMM");
    public static final SimpleDateFormat DATE_FORMAT_YEAR = new SimpleDateFormat("MMM YYYY");
    public static final SimpleDateFormat DEBUG_FORMAT = new SimpleDateFormat("E dd MMM h:mma");
    
    //@@author A0125566B
    /**
     * Format key as YYYY D d EEE given date.
     * YYYY and D are used to sort the keys by date.
     * 
     * YYYY is the year of the event.
     * D is the day in the year.
     * d is the day in the month.
     * EEE is the day of the week.
     * 
     * @param date
     * 		Date to format as key.
     * @return
     * 		Key to use in taskDisplay
     */
    public static String FORMAT_KEY (Date date) {
		if (date == null) {
			return "";
		}
    	
        String day = DATE_FORMAT_DAY.format(date);
        String dayInYear = DAY_IN_YEAR.format(date);
        //If the day in the year is less than 3 digits, prepend with 0s, eg. 64 to 064
        //Necessary for radix sort of String keys in TreeMap to ensure order is preserved.
        for (int i = 0; i < (3 - dayInYear.length()); i++ ) {
        	dayInYear = "0" + dayInYear;
        }
        String year = YEAR.format(date);
        
        //Concatenate all the parts of the key together.
        return (year + " " + dayInYear + " " + day);
	}
}
