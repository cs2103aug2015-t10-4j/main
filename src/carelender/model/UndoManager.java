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
	private Stack<UndoStep> redoStack;

	private UndoManager() {
		undoStack = new Stack<>();
		redoStack = new Stack<>();
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
	
	public void redoUpdate(EventList beforeUpdate) {
		redoStack.push(new UndoStep(beforeUpdate, UndoStep.UndoType.UPDATE));
	}

	/**
	 * Pops an undo step off the undo stack
	 * 
	 * @return The command to undo or null if there is none
	 */
	public void undo() {
		if (!undoStack.isEmpty()) {
			UndoStep undoStep = undoStack.pop();
			redoStack.push(undoStep);
			switch (undoStep.getUndoType()) {
			case ADD:
				Model.getInstance().undoAddedEvent(undoStep.getUndoData());
				break;
			case DELETE:
				Model.getInstance().undoDeletedEvent(undoStep.getUndoData());
				break;
			case UPDATE:
				Model.getInstance().undoUpdatedEvent(undoStep.getUndoData(), true);
				break;
			default:
				break;
			}
		}
	}
	
	public void clearRedoStack() {
		redoStack.empty();
	}
	
	public void redo() {
		if (!redoStack.isEmpty()) {
			UndoStep redoStep = redoStack.pop();
			undoStack.push(redoStep);
			switch (redoStep.getUndoType()) {
			case ADD:
				Model.getInstance().undoDeletedEvent(redoStep.getUndoData());
				break;
			case DELETE:
				Model.getInstance().undoAddedEvent(redoStep.getUndoData());
				break;
			case UPDATE:
				Model.getInstance().undoUpdatedEvent(redoStep.getUndoData(), false);
				break;
			default:
				break;
			}
		}
	}
}
