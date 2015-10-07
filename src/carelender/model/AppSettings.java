package carelender.model;

public class AppSettings {
	
	
	public AppSettings() {
		
	}
	
	public int getIntSetting() {
		return 0;
	}
	
	public boolean getBoolSetting() {
		return false;
	}
	
	public String getStringSetting() {
		String string = new String();
		string = "Hello";
		return string;
	}
	
	public DataType getSettingData() {
		return null;
	}
	
	public enum DataType {
		INTEGER, BOOLEAN, STRING
	}
	
}
