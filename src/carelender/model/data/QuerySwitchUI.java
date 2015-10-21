package carelender.model.data;

import carelender.controller.Controller;
import carelender.view.UserInterfaceController;

/**
 * Processes the Switch UI
 */
public class QuerySwitchUI extends QueryBase {
    //Flag to determine if it's a settings switch or a UI toggle
    private boolean isSettingSwitch;

    public QuerySwitchUI(boolean isSettingSwitch) {
        super(QueryType.SWITCHUI);
        this.isSettingSwitch = isSettingSwitch;
    }

    @Override
    public void controllerExecute() {
        if ( isSettingSwitch ) {
            System.out.println("Switch to settings");
            Controller.getGUI().setUI(UserInterfaceController.UIType.SETTING);
        } else {
            System.out.println("Toggle UI");
            Controller.getGUI().toggleUI();
        }
    }

    @Override
    public EventList searchExecute() {
        return null;
    }
}
