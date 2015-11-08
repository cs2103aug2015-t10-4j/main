package carelender.model.strings;

import java.text.SimpleDateFormat;

/**
 * Contains all the date formats
 */
public class DateFormats {
    public static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM");
    public static final SimpleDateFormat dateFormatYear = new SimpleDateFormat("MMM YYYY");
    public static final SimpleDateFormat debugFormat = new SimpleDateFormat("E dd MMM h:mma");
}
