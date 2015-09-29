package carelender.view;

import carelender.model.data.QueryDummy;
import carelender.model.data.QueryObject;

/**
 * Parses the user input
 */
public class InputParser {

    public QueryObject parseCompleteInput ( String input ) {
        //TODO: Determine what kind of query this is
        QueryObject newQuery;
        //Dummy code
        newQuery = new QueryDummy(input);
        return newQuery;
    }
}
