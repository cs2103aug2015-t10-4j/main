package carelender.controller.states;

/**
 * Used when the program requires the user to input some data
 */
public enum InputRequestState {
    NONE,
    EVENTSELECTION,       //Used for selecting multiple events.
    SELECTION,      //Used for selecting options
    CONFIRMING,     //A confirmation screen for deleting or editing items
}
