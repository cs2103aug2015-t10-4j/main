//@@author A0121815N
package carelender.model.data;

import carelender.model.UndoManager;

public class QueryUndo extends QueryBase{

    public QueryUndo() {
        super(QueryType.UNDO);
    }

	@Override
	public void controllerExecute() {
		UndoManager.getInstance().undo();
	}

	@Override
	public EventList searchExecute() {
		return null;
	}
}