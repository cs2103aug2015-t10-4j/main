package carelender.model.data;

import carelender.controller.Controller;

/**
 * Used for queries that cannot be parsed.
 * Simply prints the error to screen
 */
public class QueryError extends QueryBase {
    private String message;
    private boolean isHelpDisplayed;

    public QueryError(String message) {
        super(QueryType.ERROR);
        this.message = message;
    }
    public QueryError(String message, boolean isHelpDisplayed) {
        super(QueryType.ERROR);
        this.message = message;
        this.isHelpDisplayed = isHelpDisplayed;
    }

    public String getMessage() {
        return message;
    }

    public boolean isHelpDisplayed() {
        return isHelpDisplayed;
    }
    
    @Override
	public void controllerExecute() {
    	Controller.displayMessage(getMessage());
        if ( isHelpDisplayed() ) {
            Controller.showHelp();
        }
	}

	@Override
	public EventList searchExecute() {
		return null;
	}
}
