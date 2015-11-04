package carelender.model.strings;

/**
 * Class to get feedback strings for all queries
 */
public class QueryFeedback {
    public static String addTask(String name) {
        return "Added [" + name + "]";
    }
    public static String deleteTask (String name ) {
        return "Deleted ["+name+"]";
    }
    public static String deleteTask (int count ) {
        return "Deleted "+count+" tasks";
    }
    public static String completeTask (String name ) {
        return "Completed ["+name+"]";
    } 
    public static String completeTask (int count ) {
    	return "Completed "+count+" tasks";
    } 
    public static String uncompleteTask (int count ) {
    	return "Uncompleted "+count+" tasks";
    } 
    public static String deleteConfirmation (int count) {
        return "Are you sure you want to delete " + count + " events? [Y/N]";
    }
    public static String deleteCancelled () {
        return "Cancelled, nothing deleted :)";
    }
    public static String updateCancelled () {
        return "Cancelled, nothing updated :)";
    }

    public static String reminderAdded(int size) {
        return size + " reminders added";
    }

    public static String reminderAdded() {
        return "Reminder added!";
    }
}
