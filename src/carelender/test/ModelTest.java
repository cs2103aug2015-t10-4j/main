package carelender.test;
import static org.junit.Assert.*;
import org.junit.Test;

import carelender.controller.Search;
import carelender.model.AppSettings;
import carelender.model.Model;
import carelender.model.data.Event;
import carelender.model.AppSettings.SettingName;

public class ModelTest {

	@Test
	public void testAppSettings() {
		AppSettings.getInstance().setIntSetting(SettingName.CURRENT_INDEX, 56);
		AppSettings.getInstance().setBooleanSetting(SettingName.ISFREE, true);
		AppSettings.getInstance().setStringSetting(SettingName.USERNAME, "Shalom");
		
		//assertEquals(56, AppSettings.getInstance().getIntSetting(SettingName.CURRENT_INDEX));
		assertEquals(true, AppSettings.getInstance().getBooleanSetting(SettingName.ISFREE));
		assertEquals("Shalom", AppSettings.getInstance().getStringSetting(SettingName.USERNAME));
		//fail("Not yet implemented");
	}
	@Test
	public void testModel() {
//		Event event = new Event(1, "Hello", null);
//		Model.getInstance().setSaveFileName("test.dat");
//		assertTrue(Model.getInstance().addEvent(event));
//		assertNotNull(Model.getInstance().retrieveEvent());
		//assertEquals("1. hello\n2. my\n3. name\n4. is\n5. really\n", executeUserInput("display"));
	}
}