//@@author A0133269A
package carelender.controller.callbacks;

import carelender.model.data.Event;

/**
 * Used for the event selection input blocking state
 */
public interface OnEventSelectedCallback {
    void onChosen(Event selected);
}
