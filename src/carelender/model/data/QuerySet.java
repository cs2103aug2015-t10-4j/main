package carelender.model.data;

import carelender.controller.Controller;
import carelender.model.AppSettings;
import carelender.model.AppSettings.SettingName;

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
		switch(keyword){
			case "username":
				AppSettings.getInstance().setStringSetting(SettingName.USERNAME, value);
				String newNameHint = "Welcome back " + value;
				Controller.displayAnnouncement(newNameHint);
				break;
			case "remindertime":
				break;
			case "startview":
				break;
			default:
				break;
		}
	}

	@Override
	public EventList searchExecute() {
		return null;
	}
}

