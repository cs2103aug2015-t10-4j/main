package carelender.model.strings;

/**
 * Messages for different errors
 */
public class ErrorMessages {
    public static String invalidConfirmation() {
        return "Sorry, we didn't understand you. Please say [Yes] or [No]";
    }
    public static String invalidNumberRange(int start, int end) {
        return "You've chosen an invalid option, please enter a number between " + start + " and " + end;
    }
    public static String invalidNumber() {
        return "Please input a number";
    }

}
