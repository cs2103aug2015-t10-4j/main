//@@author A0133269A
package carelender.view.parser;

/**
 * Used by the Command class to store keywords
 */
public class CommandKeyword {
    String keyword;
    String type;
    DataPosition dataPosition;


    public CommandKeyword(String keyword, String type) {
        this.keyword = keyword;
        this.type = type;
        this.dataPosition = DataPosition.NONE;
    }
    public CommandKeyword(String keyword, String type, DataPosition dataPosition) {
        this.keyword = keyword;
        this.type = type;
        this.dataPosition = dataPosition;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getType() {
        return type;
    }

    public DataPosition getDataPosition() {
        return dataPosition;
    }

    public enum DataPosition {
        NONE, AFTER, BEFORE
    }
}
