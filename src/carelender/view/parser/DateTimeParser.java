package carelender.view.parser;

import carelender.model.data.DateRange;
import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by JiaXun on 10/10/2015.
 */
public class DateTimeParser {
    //Matches all kinds of date time strings
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
     * Removes all the parts of the string referencing dates to make processing easier
     * @param inputString String to have bits removed from
     * @return String with dates removed
     */
    public static String removeDateParts ( String inputString ) {
        SimpleDateGroup[] simpleDateGroups = parseDateTimeRaw(inputString);
        if ( simpleDateGroups != null ) {
            for (SimpleDateGroup simpleDateGroup : simpleDateGroups) {
                int length = simpleDateGroup.text.length();
                inputString = inputString.substring(simpleDateGroup.position, simpleDateGroup.position + length);
            }
        }
        return inputString;
    }
    /**
     * Similar to parseDateTimeRaw, but it tries to detect date ranges in the text.
     * @param inputString Date string to be parsed
     * @return Array of DateRange objects
     */
    public static DateRange[] parseDateTime(String inputString) {
        ArrayList<DateRange> dateRanges = new ArrayList<>();
        //Natty cannot process strings like "mon-tue", it needs the space
        inputString = inputString.toLowerCase().replaceAll("-", " - ");
        //Split the string by "and" "," "&"
        String [] inputParts = inputString.split("(and|,|&)");
        for ( int i = 0; i < inputParts.length; i++ ) {
            inputParts[i] = inputParts[i].trim();
        }
        SimpleDateGroup[] dateGroups;

        for ( String inputPart : inputParts ) {
            dateGroups = parseDateTimeRaw(inputPart);
            if ( dateGroups == null ) continue;
            //By here, the array should only have one item
            //This loop is in case it has more than one
            for ( SimpleDateGroup dateGroup : dateGroups ) {
                int numberOfDates = dateGroup.dates.length;
                if ( numberOfDates == 1 ) {
                    dateRanges.add(new DateRange(dateGroup.dates[0]));
                } else if ( numberOfDates == 2 ) {
                    //Should be a range, search for "to" "-"
                    if ( dateGroup.text.contains("to") ||  dateGroup.text.contains("-") ) {
                        //Process as a date range
                        dateRanges.add(new DateRange(dateGroup.dates[0], dateGroup.dates[1]));
                    } else {
                        //Add as two separate dates
                        dateRanges.add(new DateRange(dateGroup.dates[0]));
                        dateRanges.add(new DateRange(dateGroup.dates[1]));
                    }
                } else {
                    for ( Date date : dateGroup.dates ) {
                        dateRanges.add(new DateRange(date));
                    }
                }
            }
        }
        if ( dateRanges.size() == 0 ) {
            return null;
        } else {
            DateRange[] ranges = new DateRange[dateRanges.size()];
            int i = 0;
            for ( DateRange range: dateRanges ) {
                ranges[i] = range;
                i++;
            }
            return ranges;
        }
    }
    /**
     * Parses the date time string and returns an array of SimpleDateGroup objects
     * @param inputString Date string to be parsed
     * @return SimpleDateGroup object array, null if no dates found
     */
    public static SimpleDateGroup[] parseDateTimeRaw (String inputString) {
        Parser parser = new Parser();
        List <DateGroup> groups = parser.parse(inputString);
        if ( groups.size() == 0 ) {
            return null;
        }
        SimpleDateGroup [] simpleGroups = new SimpleDateGroup[groups.size()];

        int i = 0;
        for(DateGroup group:groups) {
            List<Date> dates = group.getDates();
            int column = group.getPosition();
            String matchingValue = group.getText();
            boolean hasTime = stringHasTime(matchingValue);
            Date [] dateArray = new Date[dates.size()];
            for ( int j = 0 ; j < dates.size(); j++ ) {
                dateArray[j] = dates.get(j);
            }
            SimpleDateGroup simpleDateGroup = new SimpleDateGroup(dateArray, matchingValue, column, hasTime);
            simpleGroups[i] = simpleDateGroup;
            i++;
        }

        return simpleGroups;
    }

    /**
     * Runs through the date and searches for anything that looks like a time.
     * @param dateString Date string to be searched
     * @return true if there is a semblance of a time
     */
    public static boolean stringHasTime ( String dateString ) {
        //This regex will look for patters like this
        //   "3pm" "5:20a" "4:20" "13:40" "2044 hrs" "1202h" "at 3" "morning" "afternoon" "night" "evening" "noon"
        String timeRegex = "((1[0-2]|[0-9])(:[0-5][0-9])?\\s?(a|p)(m)?)|([0-2]?[0-9]:[0-5][0-9])|([0-2][0-9]:?[0-5][0-9]\\s?(hrs|hr|h))|(at\\s(1[0-2]|[0-9])(:[0-5][0-9])?)|(morning|afternoon|night|evening|noon)";
        return Pattern.compile(timeRegex).matcher(dateString).find();
    }

}
