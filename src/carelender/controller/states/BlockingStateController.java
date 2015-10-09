package carelender.controller.states;

import carelender.controller.Controller;
import carelender.controller.callbacks.OnConfirmedCallback;
import carelender.controller.callbacks.OnEventSelectedCallback;
import carelender.model.data.EventList;
import carelender.model.data.EventObject;
import carelender.model.strings.ErrorMessages;

/**
 * Manages the logic for the states that require user input
 */
public class BlockingStateController {
    private InputRequestState inputRequestState;
    private OnEventSelectedCallback onEventSelectedCallback;
    private OnConfirmedCallback onConfirmedCallback;

    //Temp variables
    private static EventList selectionList; //Used for selecting multiple objects


    public BlockingStateController() {
        inputRequestState = InputRequestState.NONE;
        onEventSelectedCallback = null;
        onConfirmedCallback = null;
    }

    /**
     * Called before the normal processing.
     * Blocks the rest of the program from running when user input is required.
     * @param userInput user's input
     * @return true if program is blocked
     */
    public boolean processBlockingState ( String userInput ) {
        switch ( inputRequestState ) {
            case EVENTSELECTION:
                stateChoosing(userInput);
                break;

            case CONFIRMING:
                stateConfirming(userInput);
                break;

            default:
                return false;
        }
        return true;
    }

    /**
     * Set the application into a choosing mode
     * @param message Message to show to user before displaying the choices
     * @param choices List of events to choose from
     * @param callback Code to be run after the choosing is complete
     */
    public void startEventSelection(String message, EventList choices, OnEventSelectedCallback callback) {
        inputRequestState = InputRequestState.EVENTSELECTION;
        selectionList = choices;
        onEventSelectedCallback = callback;
        Controller.displayMessage(message);
        Controller.displayMessage(selectionList.toString());
    }

    /**
     * Set the application into a confirmation mode
     * @param message Message to show user
     */
    public void startConfirmation ( String message, OnConfirmedCallback callback ) {
        inputRequestState = InputRequestState.CONFIRMING;
        onConfirmedCallback = callback;

        Controller.displayMessage(message);
    }


    /**
     * This state is used when the user is required to choose from a list of events
     * @param userInput User's input
     */
    private void stateChoosing ( String userInput ) {
        try {
            int chosen = Integer.parseInt( userInput );
            if ( chosen < 1 || chosen > selectionList.size() ) {
                String message = "You've chosen an invalid number, please enter a number between 1 and " + selectionList.size();
                Controller.displayMessage(message);
            } else {
                EventObject selectedObject = selectionList.get(chosen-1);
                inputRequestState = InputRequestState.NONE;
                if ( onEventSelectedCallback != null ) {
                    onEventSelectedCallback.onChosen(selectedObject);
                }
            }
        } catch ( NumberFormatException e ) {
            Controller.displayMessage("Please input a number");
        }
    }

    private void stateConfirming(String userInput) {
        userInput = userInput.trim().toLowerCase();
        if ( userInput.startsWith("y") ) {
            inputRequestState = InputRequestState.NONE;
            if ( onConfirmedCallback != null ) {
                onConfirmedCallback.onConfirmed(true);
            }
        } else if ( userInput.startsWith("n") ) {
            inputRequestState = InputRequestState.NONE;
            if ( onConfirmedCallback != null ) {
                onConfirmedCallback.onConfirmed(false);
            }
        } else {
            Controller.displayMessage(ErrorMessages.invalidConfirmation());
        }
    }



}
