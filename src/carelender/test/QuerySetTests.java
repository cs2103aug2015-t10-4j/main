//@@author A0133907E
package carelender.test;

import static org.junit.Assert.*;
import org.junit.Test;
import carelender.model.AppSettings;
import carelender.model.AppSettings.SettingName;
import carelender.view.gui.UIController.UIType;

public class QuerySetTests {
	
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
	
	/**
	 * Test QuerySet controllerExecute() without invoking Controller
	 * @param keyword
	 * @param value
	 */
	private void controllerExecute(String keyword, String value) {
		switch (keyword) {
			case "username":
				AppSettings.getInstance().setStringSetting(SettingName.USERNAME,(String) value);
				//String newNameHint = "Welcome back " + value;
				//Controller.displayAnnouncement(newNameHint);
				break;
			case "startview":
				UIType newDefaultUIType = getUIType(value.toUpperCase());
				System.out.println(5);
				AppSettings.getInstance().setUITypeSetting(SettingName.DEFAULT_UITYPE, newDefaultUIType);
				System.out.println(6);
				break;
			default:
				break;
		}
	}
	
	@Test
	public void testGetUIType() {
		assertEquals(getUIType("TIMELINE"), UIType.TIMELINE);
	}
	
	@Test
	public void testcontrollerExecute() {
		AppSettings.getInstance().setStringSetting(SettingName.USERNAME, "Default");
		controllerExecute("username", "new name");
		assertEquals(AppSettings.getInstance().getStringSetting(SettingName.USERNAME), "new name");
	}
}
