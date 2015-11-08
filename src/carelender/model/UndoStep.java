//@@author A0121815N
package carelender.model;

import carelender.model.data.EventList;

/**
 * Stores a single step for undo
 */
public class UndoStep {
    private EventList undoData;
    private UndoType undoType;

    public UndoStep(EventList undoData, UndoType undoType) {
        this.undoData = undoData;
        this.undoType = undoType;
    }

    public EventList getUndoData() {
        return undoData;
    }

    public UndoType getUndoType() {
        return undoType;
    }

    public enum UndoType {
        ADD, DELETE, UPDATE
    }
}
