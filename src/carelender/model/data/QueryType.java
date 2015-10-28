package carelender.model.data;

/**
 * Used for determining the type of query
 */
public enum QueryType {
    ERROR,      //Invalid command entered
    ADD,        //Adding new event
    DELETE,     //Deleting an event
    REMINDER,   //Setting reminder for an event
    UPDATE,     //Updating an event
    HELP,       //Showing the help list
    LIST,       //Listing tasks //TODO: Split into different types
    CLEAR,       //Clears the message screen
    UNDO,
    SWITCHUI,
    DATETEST,       //Used for testing date parsing
    DEV1,           //Used for whatever
    DEV2,            //Used for whatever
    EXIT
}