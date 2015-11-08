package carelender.model.strings;

import java.text.SimpleDateFormat;

/**
 * Contains all the date formats
 */
public class DateFormats {
    public static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    public static final SimpleDateFormat dayInYear = new SimpleDateFormat("D");
    public static final SimpleDateFormat year = new SimpleDateFormat("y");
    public static final SimpleDateFormat dateFormatDay = new SimpleDateFormat("d EEE");
    public static final SimpleDateFormat dateFormatMonth = new SimpleDateFormat("dd MMM");
    public static final SimpleDateFormat dateFormatYear = new SimpleDateFormat("MMM YYYY");
    public static final SimpleDateFormat debugFormat = new SimpleDateFormat("E dd MMM h:mma");
}
