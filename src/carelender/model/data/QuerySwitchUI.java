package carelender.model.data;

import carelender.controller.Controller;
import carelender.view.gui.UIController;

/**
 * Processes the Switch UI
 */
public class QuerySwitchUI extends QueryBase {
    //Flag to determine if it's a settings switch or a UI toggle
    private boolean isSettingSwitch;
    private UIController.UIType uiType = null;

    public QuerySwitchUI(boolean isSettingSwitch) {
        super(QueryType.SWITCHUI);
        this.isSettingSwitch = isSettingSwitch;
        uiType = null;
    }
    public QuerySwitchUI(boolean isSettingSwitch, UIController.UIType uiType) {
        super(QueryType.SWITCHUI);
        this.isSettingSwitch = isSettingSwitch;
        this.uiType =  uiType;
    }

    @Override
    public void controllerExecute() {
        if ( isSettingSwitch ) {
            Controller.getUI().setUI(UIController.UIType.SETTING);
        } else {
            if ( uiType != null ) {
                Controller.getUI().setUI(uiType);
            } else {
                Controller.getUI().toggleUI();
            }
        }
    }

    @Override
    public EventList searchExecute() {
        return null;
    }
}
