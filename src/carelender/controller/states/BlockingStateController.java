package carelender.controller.states;

import carelender.controller.Controller;
import carelender.controller.callbacks.OnConfirmedCallback;
import carelender.controller.callbacks.OnEventSelectedCallback;
import carelender.controller.callbacks.OnSelectedCallback;
import carelender.model.data.EventList;
import carelender.model.data.Event;
import carelender.model.strings.ErrorMessages;

/**
 * Manages the logic for the states that require user input
 */
public class BlockingStateController {
    private BlockingState blockingState;
    private OnEventSelectedCallback onEventSelectedCallback;
    private OnSelectedCallback onSelectedCallback;
    private OnConfirmedCallback onConfirmedCallback;

    //Temp variables
    private EventList selectionList; //Used for selecting multiple events
    private String [] stringSelectionList; //Used for selecting multiple choices


    public BlockingStateController() {
        blockingState = BlockingState.NONE;
        onEventSelectedCallback = null;
        onConfirmedCallback = null;
        onSelectedCallback = null;
    }

    /**
     * Called before the normal processing.
     * Blocks the rest of the program from running when user input is required.
     * @param userInput user's input
     * @return true if program is blocked
     */
    public boolean processBlockingState ( String userInput ) {
        switch (blockingState) {
            case EVENTSELECTION:
                stateEventSelection(userInput);
                break;
            case SELECTION:
                stateSelection(userInput);
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
    public void startSelection(String message, String [] choices, OnSelectedCallback callback) {
        blockingState = BlockingState.SELECTION;
        stringSelectionList = choices;
        onSelectedCallback = callback;

        Controller.clearMessages();
        Controller.displayMessage(message);
        StringBuilder stringBuilder = new StringBuilder();
        String breakline = "";
        int count = 0;
        for ( String choice : choices ) {
                count++;
                stringBuilder.append(breakline);
                breakline = System.lineSeparator();
                stringBuilder.append(count);
                stringBuilder.append(". ");
                stringBuilder.append(choice);
            }
        Controller.displayMessage(stringBuilder.toString());
    }

    /**
     * Set the application into a choosing mode
     * @param message Message to show to user before displaying the choices
     * @param choices List of events to choose from
     * @param callback Code to be run after the choosing is complete
     */
    public void startEventSelection(String message, EventList choices, OnEventSelectedCallback callback) {
        blockingState = BlockingState.EVENTSELECTION;
        selectionList = choices;
        onEventSelectedCallback = callback;
        Controller.clearMessages();
        Controller.displayMessage(selectionList.toString());
    }

    /**
     * Set the application into a confirmation mode
     * @param message Message to show user
     */
    public void startConfirmation ( String message, OnConfirmedCallback callback ) {
        blockingState = BlockingState.CONFIRMING;
        onConfirmedCallback = callback;
        Controller.clearMessages();
        Controller.displayMessage(message);
    }


    /**
     * This state is used when the user is required to choose from a list of choices
     * @param userInput User's input
     */
    private void stateSelection(String userInput) {
        try {
            int chosen = Integer.parseInt( userInput );
            if ( chosen < 1 || chosen > stringSelectionList.length ) {
                Controller.displayMessage(ErrorMessages.invalidNumberRange(1, stringSelectionList.length));
            } else {
                blockingState = BlockingState.NONE;
                if ( onSelectedCallback != null ) {
                    onSelectedCallback.onChosen(chosen-1);
                }
            }
        } catch ( NumberFormatException e ) {
            Controller.displayMessage(ErrorMessages.invalidNumber());
        }
    }

    /**
     * This state is used when the user is required to choose from a list of events
     * @param userInput User's input
     */
    private void stateEventSelection(String userInput) {
        try {
            int chosen = Integer.parseInt( userInput );
            if ( chosen < 1 || chosen > selectionList.size() ) {
                Controller.displayMessage(ErrorMessages.invalidNumberRange(1, selectionList.size()));
            } else {
                Event selectedObject = selectionList.get(chosen-1);
                blockingState = BlockingState.NONE;
                if ( onEventSelectedCallback != null ) {
                    onEventSelectedCallback.onChosen(selectedObject);
                }
            }
        } catch ( NumberFormatException e ) {
            Controller.displayMessage(ErrorMessages.invalidNumber());
        }
    }

    private void stateConfirming(String userInput) {
        userInput = userInput.trim().toLowerCase();
        if ( userInput.startsWith("y") ) {
            blockingState = BlockingState.NONE;
            if ( onConfirmedCallback != null ) {
                onConfirmedCallback.onConfirmed(true);
            }
        } else if ( userInput.startsWith("n") ) {
            blockingState = BlockingState.NONE;
            if ( onConfirmedCallback != null ) {
                onConfirmedCallback.onConfirmed(false);
            }
        } else {
            Controller.displayMessage(ErrorMessages.invalidConfirmation());
        }
    }



}
