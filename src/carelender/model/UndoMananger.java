package carelender.model;

import carelender.model.data.EventList;
import carelender.model.data.EventObject;

import java.security.PublicKey;
import java.util.Stack;

/**
 * This class should be called every time a change happens to the model.
 */
public class UndoMananger {
    private Stack<UndoStep> undoStack;

    UndoMananger () {
        undoStack = new Stack<>();
    }
    /**
     * Called when a new item or new items are added.
     * Will create a corresponding delete query on the stack
     * @param added
     */
    public void add ( EventList added ) {
        //Invert the event list

    }
    public void delete ( EventList deleted ) {

    }

    public void update ( EventList beforeUpdate, EventList afterUpdate ) {
        undoStack.push(new UndoStep(beforeUpdate, UndoStep.UndoType.UPDATE));
    }

    /**
     * Pops an undo step off the undo stack
     * @return The command to undo or null if there is none
     */
    public UndoStep undo() {
        return undoStack.pop();
    }
}
