package carelender.model.data;

import carelender.controller.Controller;
import carelender.view.gui.UserInterfaceController;

/**
 * Processes the Switch UI
 */
public class QuerySwitchUI extends QueryBase {
    //Flag to determine if it's a settings switch or a UI toggle
    private boolean isSettingSwitch;
    private UserInterfaceController.UIType uiType = null;

    public QuerySwitchUI(boolean isSettingSwitch) {
        super(QueryType.SWITCHUI);
        this.isSettingSwitch = isSettingSwitch;
        uiType = null;
    }
    public QuerySwitchUI(boolean isSettingSwitch, UserInterfaceController.UIType uiType) {
        super(QueryType.SWITCHUI);
        this.isSettingSwitch = isSettingSwitch;
        this.uiType =  uiType;
    }

    @Override
    public void controllerExecute() {
        if ( isSettingSwitch ) {
            Controller.getGUI().setUI(UserInterfaceController.UIType.SETTING);
        } else {
            if ( uiType != null ) {
                Controller.getGUI().setUI(uiType);
            } else {
                Controller.getGUI().toggleUI();
            }
        }
    }

    @Override
    public EventList searchExecute() {
        return null;
    }
}
