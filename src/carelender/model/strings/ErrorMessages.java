package carelender.model.strings;

/**
 * Messages for different errors
 */
public class ErrorMessages {
    public static String invalidCommand( String command ) {
        return "[" + command + "] is not a valid command. Type ? for help.";
    }

    public static String tooManyDateRange() {
       return "Please input at most one date range";
    }
    public static String emptySearch() {
        return "Please input something to search";
    }

    public static String addNoParameters() {
        return "What would you like to add?";
    }
    public static String deleteNoParameters() {
        return "What would you like to delete?";
    }

    public static String completeNoParameters() {
        return "What would you like to complete?";
    }

    public static String setNoParameters() {
        return "What would you like to set?";
    }
    public static String updateNoParameters() {
        return "What would you like to update?";
    }

    public static String remindNoParameters() {
        return "What do you want a reminder for?";
    }
    public static String nothingListed() {
        return "There is nothing displayed, please use the list or search commands";
    }

    public static String invalidIndices() {
        return "Error parsing indices";
    }
    public static String singleIndexOnly() {
        return "Please input only one index";
    }

    public static String invalidUpdate() {
        return "Update command invalid";
    }


    public static String invalidConfirmation() {
        return "Sorry, we didn't understand you. Please say [Yes] or [No]";
    }
    public static String invalidNumberRange(int start, int end) {
        return "You've chosen an invalid option, please enter a number between " + start + " and " + end;
    }
    public static String invalidNumber() {
        return "Please input a number";
    }
    public static String invalidNumber(String s) {
        return s + " is not a number. Please input a number";
    }

}
