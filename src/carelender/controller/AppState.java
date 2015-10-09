package carelender.controller;

/**
 * Enum for the application states
 */
public enum AppState {
    FIRSTSTART,     //Shown to the user when the application first starts
    DEFAULT,        //Normal screen the user sees
    CHOOSING,       //When there are conflicting items, the program will wait for the user to choose which item you want.
    CONFIRMING,     //A confirmation screen for deleting or editing items
    SETTINGS        //Settings screen
}
