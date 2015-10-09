package carelender.view.parser;

/**
 * Stores the format of a date time string, used by the date time parser
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
