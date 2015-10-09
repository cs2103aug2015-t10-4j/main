package carelender.controller.callbacks;

import carelender.model.data.EventObject;

/**
 * Used for the event selection input blocking state
 */
public interface OnEventSelectedCallback {
    void onChosen(EventObject selected);
}
