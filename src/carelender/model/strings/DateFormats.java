//@@author A0133269A
package carelender.model.strings;

import java.text.SimpleDateFormat;

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
}
