//@@author A0133907E
package carelender.model.data;

import carelender.controller.Controller;
import carelender.model.AppSettings;
import carelender.model.AppSettings.SettingName;
import carelender.view.gui.UIController.UIType;

public class QuerySet extends QueryBase {

    String keyword;
    String value;

    public QuerySet(String keyword, String value) {
        super(QueryType.SET);
        this.keyword = keyword;
        this.value = value;
    }
    
    @Override
    public void controllerExecute() {
        switch (keyword) {
            case "username":
                AppSettings.getInstance().setStringSetting(SettingName.USERNAME,(String) value);
                String newNameHint = "Welcome back " + value;
                Controller.displayAnnouncement(newNameHint);
                break;
            case "startview":
                UIType newDefaultUIType = getUIType(value.toUpperCase());
                AppSettings.getInstance().setUITypeSetting(SettingName.DEFAULT_UITYPE, newDefaultUIType);
                break;
            default:
                break;
        }
    }

    @Override
    public EventList searchExecute() {
        return null;
    }

    /**
     * Converts user input string to corresponding UIType
     * @param input
     * @return
     */
    private UIType getUIType (String input) {
        UIType uiType = null;
        switch (input) {
            case "TIMELINE":
                uiType = UIType.TIMELINE;
                break;
            case "CALENDAR":
                uiType = UIType.CALENDAR;
                break;
            case "FLOATING":
                uiType = UIType.FLOATING;
                break;
            case "SETTINGS":
                uiType = UIType.SETTING;
                break;
        }
        return uiType;
    }
}

