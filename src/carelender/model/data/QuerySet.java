package carelender.model.data;

import carelender.model.AppSettings;
import carelender.model.AppSettings.SettingName;

public class QuerySet extends QueryBase {
	
	String keyword;
	String value;
	
    public QuerySet(String keyword, String value) {
        super(QueryType.SET);
    	System.out.println("Inside constructor");
        this.keyword = keyword;
        this.value = value;
        System.out.println("Keyword " + keyword);
        System.out.println("Value " + value);
    }
    
    @Override
	public void controllerExecute() {
    	System.out.println("Inside controllerExecute");
		switch(keyword){
			case "username":
				System.out.println("Inside switch username");
				AppSettings.getInstance().setStringSetting(SettingName.USERNAME, value);
				//refresh the screen
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

