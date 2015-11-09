# A0133269Aunused
###### carelender\view\parser\DateTimeFormat.java
``` java
package carelender.view.parser;

/**
 * Stores the format of a date time string, used by the date time parser
 * Note: Depreciated class
 *       Natty is now used to parse the date time.
 */
public class DateTimeFormat {
    private String format;
    private boolean hasTime, hasDate, hasYear;
    private int trimBack;

    public DateTimeFormat(String format, boolean hasTime, boolean hasDate) {
        this.format = format;
        this.hasTime = hasTime;
        this.hasDate = hasDate;
        this.hasYear = true;
        this.trimBack = 0;
    }

    public DateTimeFormat(String format, boolean hasTime, boolean hasDate, int trimBack) {
        this.format = format;
        this.hasTime = hasTime;
        this.hasDate = hasDate;
        this.trimBack = trimBack;
    }

    public DateTimeFormat(String format, boolean hasTime, boolean hasDate, boolean hasYear) {
        this.format = format;
        this.hasTime = hasTime;
        this.hasDate = hasDate;
        this.hasYear = hasYear;
        this.trimBack = 0;
    }

    public DateTimeFormat(String format, boolean hasTime, boolean hasDate, boolean hasYear, int trimBack) {
        this.format = format;
        this.hasTime = hasTime;
        this.hasDate = hasDate;
        this.hasYear = hasYear;
        this.trimBack = trimBack;
    }

    public String getFormat() {
        if ( trimBack > 0 ) {
            return format.substring(0, format.length() - trimBack - 1);
        }
        return format;
    }

    public boolean hasTime() {
        return hasTime;
    }

    public boolean hasDate() {
        return hasDate;
    }

    @Override
    public String toString() {
        return format + " | time: " + hasTime + " | date: " + hasDate + " | year: " + hasYear;
    }
}
```
###### carelender\view\parser\DateTimeParser.java
``` java
    //Matches all kinds of date time strings, unused since natty came along
    //In case of emergency, break glass
    private static final Map<String, DateTimeFormat> DATE_FORMAT_REGEXPS = new HashMap<String, DateTimeFormat>() {{
        put("^\\d{8}$", new DateTimeFormat("yyyyMMdd", false, true));
        put("^\\d{1,2}-\\d{1,2}-\\d{4}$", new DateTimeFormat("dd-MM-yyyy", false, true));
        put("^\\d{4}-\\d{1,2}-\\d{1,2}$", new DateTimeFormat("yyyy-MM-dd", false, true));
        put("^\\d{1,2}/\\d{1,2}/\\d{4}$", new DateTimeFormat("MM/dd/yyyy", false, true));
        put("^\\d{4}/\\d{1,2}/\\d{1,2}$", new DateTimeFormat("yyyy/MM/dd", false, true));
        put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}$", new DateTimeFormat("dd MMM yyyy", false, true));
        put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}$", new DateTimeFormat("dd MMMM yyyy", false, true));
        put("^\\d{1,2}\\s[a-z]{3}$", new DateTimeFormat("dd MMM", false, true, false));
        put("^\\d{1,2}\\s[a-z]{4,}$", new DateTimeFormat("dd MMMM", false, true, false));
        put("^[a-z]{3}\\s\\d{1,2}$", new DateTimeFormat("MMM dd", false, true, false));
        put("^[a-z]{4,}\\s\\d{1,2}$", new DateTimeFormat("MMMM dd", false, true, false));
        put("^[0-2]?[0-9][0-5][0-9](hrs)$", new DateTimeFormat("HHmm", true, false, false, 3));
        put("^[0-2]?[0-9]\\s[0-5][0-9](hrs)$", new DateTimeFormat("HH mm", true, false, false, 3));
        put("^[0-2]?[0-9][0-5][0-9](hr)$", new DateTimeFormat("HHmm", true, false, false, 2));
        put("^[0-2]?[0-9]\\s[0-5][0-9](hr)$", new DateTimeFormat("HH mm", true, false, false, 2));
        put("^[0-2]?[0-9][0-5][0-9]$", new DateTimeFormat("HHmm", true, false, false));
        put("^[0-2]?[0-9]\\s[0-5][0-9]$", new DateTimeFormat("HH mm", true, false, false));
        put("^[0-2]?[0-9]:[0-5][0-9]$", new DateTimeFormat("HH:mm", true, false, false));
        put("^([1-9]|1[0-2])(pm|am|AM|PM)$", new DateTimeFormat("HHa", true, false, false));
        put("^([1-9]|1[0-2])\\s(pm|am|AM|PM)$", new DateTimeFormat("HH a", true, false, false));
        put("^\\d{8}\\s\\d{4}$", new DateTimeFormat("yyyyMMdd HHmm", true, true));
        put("^\\d{1,2}-\\d{1,2}-\\d{4}\\s[0-2]?[0-9]:[0-5][0-9]$", new DateTimeFormat("dd-MM-yyyy HH:mm", true, true));
        put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s[0-2]?[0-9]:[0-5][0-9]$", new DateTimeFormat("yyyy-MM-dd HH:mm", true, true));
        put("^\\d{1,2}/\\d{1,2}/\\d{4}\\s[0-2]?[0-9]:[0-5][0-9]$", new DateTimeFormat("MM/dd/yyyy HH:mm", true, true));
        put("^\\d{4}/\\d{1,2}/\\d{1,2}\\s[0-2]?[0-9]:[0-5][0-9]$", new DateTimeFormat("yyyy/MM/dd HH:mm", true, true));
        put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s[0-2]?[0-9]:[0-5][0-9]$", new DateTimeFormat("dd MMM yyyy HH:mm", true, true));
        put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s[0-2]?[0-9]:[0-5][0-9]$", new DateTimeFormat("dd MMMM yyyy HH:mm", true, true));
        put("^\\d{1,2}-\\d{1,2}-\\d{4}\\s[0-2]?[0-9]:[0-5][0-9]:[0-5][0-9]$", new DateTimeFormat("dd-MM-yyyy HH:mm:ss", true, true));
        put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s[0-2]?[0-9]:[0-5][0-9]:[0-5][0-9]$", new DateTimeFormat("yyyy-MM-dd HH:mm:ss", true, true));
        put("^\\d{1,2}/\\d{1,2}/\\d{4}\\s[0-2]?[0-9]:[0-5][0-9]:[0-5][0-9]$", new DateTimeFormat("MM/dd/yyyy HH:mm:ss", true, true));
        put("^\\d{4}/\\d{1,2}/\\d{1,2}\\s[0-2]?[0-9]:[0-5][0-9]:[0-5][0-9]$", new DateTimeFormat("yyyy/MM/dd HH:mm:ss", true, true));
        put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s[0-2]?[0-9]:[0-5][0-9]:[0-5][0-9]$", new DateTimeFormat("dd MMM yyyy HH:mm:ss", true, true));
        put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s[0-2]?[0-9]:[0-5][0-9]:[0-5][0-9]$", new DateTimeFormat("dd MMMM yyyy HH:mm:ss", true, true));
    }};

    /**
     * Determine SimpleDateFormat pattern matching with the given date string. Returns null if
     * format is unknown. You can simply extend DateUtil with more formats if needed.
     * Depreciated, Natty is now used to parse the date time.
     * @param dateString The date string to determine the SimpleDateFormat pattern for.
     * @return DateTimeFormat pattern, or null if format is unknown.
     * @see DateTimeFormat
     */
    public static DateTimeFormat determineDateFormat(String dateString) {
        for (String regexp : DATE_FORMAT_REGEXPS.keySet()) {
            if (dateString.toLowerCase().matches(regexp)) {
                return DATE_FORMAT_REGEXPS.get(regexp);
            }
        }
        return null; // Unknown format.
    }

    /**
     * Converts a date time string into a date object
     * Depreciated, Natty is now used to parse the date time.
     * @param dateString The date string to be converted
     * @return Date object, or null if it does not match a known format
     */

    public static Date stringToDate ( String dateString ) {
        DateTimeFormat dateTimeFormat = determineDateFormat(dateString);
        if ( dateTimeFormat == null ) {
            return null;
        }


        try {
            DateFormat format = new SimpleDateFormat(dateTimeFormat.getFormat());
            Date date = format.parse(dateString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.SECOND, 0);
            if ( !dateTimeFormat.hasTime() ) {
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
            }

            return date;
        } catch ( ParseException e ) {
            System.out.println("Cannot parse date [" + dateString + "]");
            return null;
        }
    }

    /**
     * Replaces all the parts of the string referencing dates to make processing easier
     * @param inputString  String to have bits removed from
     * @param dateParts Parts of the date to be removed
     * @param replace String to replace with
     * @return String with dates replaced
     */
    public static String replaceDateParts ( String inputString, String[] dateParts, String replace ) {
        //This magical regex matches all "and" "," "&" outside quotation marks
        //It will then remove all of those outside quotes
        inputString = inputString.replaceAll("(and|,|&)(?=(?:[^\"]|\"[^\"]*\")*$)", "");
        for ( String dateSubstring : dateParts ) {
            inputString = inputString.replace(dateSubstring, replace);
        }
        return inputString;
    }


    
    /**
     * Similar to parseDateTimeRaw, but it tries to detect date ranges in the text.
     * @param inputString Date string to be parsed
     * @return Array of DateRange objects
     */
    public static DateRange[] parseDateTime(String inputString) {
        return parseDateTime(inputString,null);
    }

```
###### carelender\view\parser\DateTimeParser.java
``` java
    /**
     * Sets date object to 00:00hrs
     * @return Date object
     */
    public static Date startOfDay ( Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        return calendar.getTime();
    }

    /**
     * Sets date object to 23:59hrs
     * @return Date object
     */
    public static Date endOfDay ( Date date ) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MILLISECOND, 999);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        return calendar.getTime();
    }

```
