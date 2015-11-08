//@@author A0121815N
package carelender.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import carelender.view.gui.UIController.UIType;

/**
 * Applications settings are handled by this class, it saves files into a settings.dat file for persistence
 */
public class AppSettings {

	private static final String settingsFile = "settings.dat";
	private static AppSettings singleton = null;

	public static AppSettings getInstance() {
		if (singleton == null) {
			singleton = new AppSettings();
		}
		return singleton;
	}

	private HashMap<SettingName, DataType> typeHash;
	private HashMap<DataType, HashMap<SettingName, Object>> appSettingsHash;
	private static Logger log;

	@SuppressWarnings("unchecked")
    private AppSettings() {
		File file = new File(settingsFile);
		log = Logger.getLogger(Model.class.getName());
		
		typeHash = new HashMap<>();
		typeHash.put(SettingName.USERNAME, DataType.STRING);
		typeHash.put(SettingName.DEFAULT_UITYPE, DataType.UITYPE);
		typeHash.put(SettingName.ISFREE, DataType.BOOLEAN);
		typeHash.put(SettingName.CURRENT_INDEX, DataType.INTEGER);
		
		appSettingsHash = new HashMap<>();
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			BufferedReader br = new BufferedReader(new FileReader("settings.dat"));     
			if (br.readLine() == null) {			
				appSettingsHash.put(DataType.INTEGER, new HashMap<SettingName, Object>());
				appSettingsHash.put(DataType.UITYPE, new HashMap<SettingName, Object>());
				appSettingsHash.put(DataType.BOOLEAN, new HashMap<SettingName, Object>());
				appSettingsHash.put(DataType.STRING, new HashMap<SettingName, Object>());
			} else {
				FileInputStream fis = new FileInputStream(settingsFile);
				ObjectInputStream ois = new ObjectInputStream(fis);
				appSettingsHash = (HashMap<DataType, HashMap<SettingName, Object>>) ois.readObject();
				ois.close();
				fis.close();
			}
			br.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (Exception e) {
		}
	}

	public Integer getIntSetting(SettingName name) {
		try {
		return (int) appSettingsHash.get(DataType.INTEGER).get(name);
		} catch(Exception e) {
			log.log(Level.FINE, "Failed to get integer setting");
			return null;
		}
	}
	
	public boolean getBooleanSetting(SettingName name) {
		return (boolean) appSettingsHash.get(DataType.BOOLEAN).get(name);
	}
	
	public String getStringSetting(SettingName name) {

		return (String) appSettingsHash.get(DataType.STRING).get(name);
	}

	public UIType getUITypeSetting(SettingName name) {

		return (UIType) appSettingsHash.get(DataType.UITYPE).get(name);
	}
	
	public void setIntSetting(SettingName name, int value) {
		if (isDataTypeType(name, DataType.INTEGER)) {
			appSettingsHash.get(DataType.INTEGER).put(name, value);
			saveSetting();
		}
	}
	
	public void setBooleanSetting(SettingName name, boolean value) {
		if (isDataTypeType(name, DataType.BOOLEAN)) {
			appSettingsHash.get(DataType.BOOLEAN).put(name, value);
			saveSetting();
		}
	}
	
	public void setStringSetting(SettingName name, String value) {
		if (isDataTypeType(name, DataType.STRING)) {
			appSettingsHash.get(DataType.STRING).put(name, value);
			saveSetting();
		}
	}

	public void setUITypeSetting(SettingName name, UIType value) {
		if (isDataTypeType(name, DataType.UITYPE)) {
			appSettingsHash.get(DataType.UITYPE).put(name, value);
			saveSetting();
		}
	}
	
	/**
	 * Writes current Setting HashMap to file
	 */
	public void saveSetting() {
		try {
			FileOutputStream fos = new FileOutputStream(settingsFile);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			
			oos.writeObject(appSettingsHash);
			oos.close();
			fos.close();
		} catch (IOException ioe) {
			log.log(Level.FINE, "Failed to add setting");
		}
	}

	/**
	 * Checks the datatype of a setting name is correct
	 * @param name Name of setting
	 * @param type
	 * @return Boolean
	 */
	private Boolean isDataTypeType (SettingName name , DataType type ) {
		return typeHash.get(name) == type;
	}

	public enum DataType {
		INTEGER, BOOLEAN, STRING, UITYPE
	}
	
	public enum SettingName {
		USERNAME, DEFAULT_UITYPE, CURRENT_INDEX, ISFREE
	}	
}
