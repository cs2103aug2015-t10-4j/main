package carelender.model.data;

/**
 * Used for determining the type of query
 */
public enum QueryType {
    ERROR,      //Invalid command entered
    DUMMY,      //Temporary //TODO: Remove
    ADD,        //Adding new event
    DELETE,     //Deleting an event
    UPDATE,     //Updating an event
    EDIT,       //Editing an event
    HELP,       //Showing the help list
    LIST,       //Listing tasks //TODO: Split into different types
    CLEAR,       //Clears the message screen
    SWITCHUI
}