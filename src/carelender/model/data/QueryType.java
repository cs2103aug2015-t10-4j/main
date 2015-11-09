//@@author A0125566B
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
    LIST,       //Listing tasks
    CLEAR,       //Clears the message screen
    UNDO,
    REDO,
    COMPLETE,
    SHOW,
    SWITCHUI,
    DATETEST,       //Used for testing date parsing
    SET,            //Used for customize setting
    EXIT,
}