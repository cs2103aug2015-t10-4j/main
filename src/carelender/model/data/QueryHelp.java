package carelender.model.data;

import carelender.controller.Controller;
import carelender.view.parser.InputParser;

/**
 * Used for help queries
 */
public class QueryHelp extends QueryBase {
    public QueryHelp() {
        super(QueryType.HELP);
    }
    
    @Override
	public void controllerExecute() {
    	Controller.showHelp();
	}

	@Override
	public EventList searchExecute() {
		return null;
	}
}
