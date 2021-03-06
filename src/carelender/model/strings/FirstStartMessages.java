//@@author A0133269A
package carelender.model.strings;

/**
 * All the messages that will be shown to the user when we first start.
 */
public class FirstStartMessages {
    public static String confirmation(String username) {
        return "Okay, we'll call you " + username + " from now on. [Yes/No]";
    }
    public static String confirmed(String username) {
        return "Alright, " + username + " let's start";
    }
    public static String askForName() {
        return "Hi there, before we start, what should we call you?";
    }
    public static String askForNameAgain() {
        return "Please tell us what to call you.";
    }
    public static String firstStart() {
        return "Welcome to CARELender(TM). ";
    }
    public static String welcomeBack( String username ) {
        return "Welcome back, " + username;
    }


}
