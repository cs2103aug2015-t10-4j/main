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
	
	public String getStringSetting(AppSettingType settingType) throws InvalidTypeException {
		if ( getSettingData(settingType) != DataType.STRING ) {
			throw new InvalidTypeException(settingType.toString() + " is not a string type");
		}
		switch ( settingType ) {
			case USERNAME:
				return null; //The setting
			default:
				return null;
		}
	}
	
	public void setSetting ( AppSettingType settingType, Object data ) {
		DataType type = getSettingData(settingType);
		switch ( type ) {
		case STRING:
			String dataS = (String) data;
			//Save the data
			break;
		case INTEGER:
			Integer dataI = (Integer) data;
			//Save the data
			break;
		case BOOLEAN:
			Boolean dataB = (Boolean) data;
			//Save the data
			break;
		}
	}
	
	public DataType getSettingData(AppSettingType settingType) {
		switch ( settingType ) {
			case USERNAME:
				return DataType.STRING;
			default:
				break;
		}
		return null;
	}
	
	public enum DataType {
		INTEGER, BOOLEAN, STRING
	}
	
	public enum AppSettingType {
		USERNAME
	}
	
}
