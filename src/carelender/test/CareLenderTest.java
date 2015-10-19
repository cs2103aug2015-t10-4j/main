package carelender.test;
import static org.junit.Assert.*;
import org.junit.Test;
import carelender.model.AppSettings;

public class CareLenderTest extends AppSettings{

	@Test
	public void testAppSettings() {
		setIntSetting(SettingName.CURRENT_INDEX, 56);
		setBooleanSetting(SettingName.ISFREE, true);
		setStringSetting(SettingName.USERNAME, "Shalom");
		fail("Not yet implemented");
	}

}
