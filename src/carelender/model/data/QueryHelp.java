//@@author A0133269A
package carelender.model.data;

import carelender.controller.Controller;

/**
 * Used for help queries
 */
public class QueryHelp extends QueryBase {
    public QueryHelp() {
        super(QueryType.HELP);
    }
    
    @Override
    public void controllerExecute() {
        Controller.clearMessages();
        Controller.showHelp();
    }

    @Override
    public EventList searchExecute() {
        return null;
    }
}
