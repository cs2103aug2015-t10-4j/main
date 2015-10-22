package carelender.model;

import carelender.model.data.EventList;
import carelender.model.data.Event;

import java.security.PublicKey;
import java.util.Stack;

/**
 * This class should be called every time a change happens to the model.
 */

public class UndoManager {
	private static UndoManager singleton = null;

	public static UndoManager getInstance() {
		if (singleton == null) {
			singleton = new UndoManager();
		}
		return singleton;
	}

	private Stack<UndoStep> undoStack;

	private UndoManager() {
		undoStack = new Stack<>();
	}

	/**
	 * Called when a new item or new items are added. Will create a
	 * corresponding delete query on the stack
	 * 
	 * @param added
	 */
	public void add(EventList added) {
		undoStack.push(new UndoStep(added, UndoStep.UndoType.ADD));

	}

	public void delete(EventList deleted) {
		undoStack.push(new UndoStep(deleted, UndoStep.UndoType.DELETE));
	}

	public void update(EventList beforeUpdate) {
		undoStack.push(new UndoStep(beforeUpdate, UndoStep.UndoType.UPDATE));
	}

	/**
	 * Pops an undo step off the undo stack
	 * 
	 * @return The command to undo or null if there is none
	 */
	public void undo() {
		if (!undoStack.isEmpty()) {
			UndoStep undoStep = undoStack.pop();
			switch (undoStep.getUndoType()) {
			case ADD:
				Model.getInstance().undoAddedEvent(undoStep.getUndoData());
				break;
			case DELETE:
				Model.getInstance().undoDeletedEvent(undoStep.getUndoData());
				break;
			case UPDATE:
				Model.getInstance().undoUpdatedEvent(undoStep.getUndoData());
				break;
			default:
				break;
			}
		}

	}
}
