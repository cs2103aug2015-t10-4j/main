//@@author A0121815N
package carelender.model.data;

import carelender.model.UndoManager;

public class QueryRedo extends QueryBase{

    public QueryRedo() {
        super(QueryType.REDO);
    }

    @Override
    public void controllerExecute() {
        UndoManager.getInstance().redo();
    }

    @Override
    public EventList searchExecute() {
        return null;
    }
}
