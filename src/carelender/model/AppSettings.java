package carelender.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.google.gson.Gson;

public class AppSettings {
	
	private HashMap<SettingName, DataType> typeHash;
	private HashMap<DataType, HashMap<SettingName, Object>> appSettingsHash;
	private static Logger log;

	public AppSettings() {
		File file = new File("settings.dat");
		log = Logger.getLogger(Model.class.getName());
		
		typeHash = new HashMap<>();
		typeHash.put(SettingName.USERNAME, DataType.STRING);
		typeHash.put(SettingName.ISFREE, DataType.BOOLEAN);
		typeHash.put(SettingName.CURRENT_INDEX, DataType.INTEGER);
		
		appSettingsHash = new HashMap<>();
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			BufferedReader br = new BufferedReader(new FileReader("settings.dat"));     
			if (br.readLine() == null) {	
				System.out.println("Blank");				
				appSettingsHash.put(DataType.INTEGER, new HashMap<>());
				appSettingsHash.put(DataType.BOOLEAN, new HashMap<>());
				appSettingsHash.put(DataType.STRING, new HashMap<>());
			} else {
				System.out.println("Stuffed");
				FileInputStream fis = new FileInputStream("settings.dat");
				ObjectInputStream ois = new ObjectInputStream(fis);
				appSettingsHash = (HashMap<DataType, HashMap<SettingName, Object>>) ois.readObject();
				ois.close();
				fis.close();
			}
			System.out.println(appSettingsHash.get(DataType.BOOLEAN).get(SettingName.ISFREE));
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (Exception e) {
		}
	}
	
	Boolean isDataTypeType (SettingName name , DataType type ) {
		return typeHash.get(name) == type;
	}
	
	public int getIntSetting(SettingName name) {
		return (int) appSettingsHash.get(DataType.INTEGER).get(name);
	}
	
	public boolean getBooleanSetting(SettingName name) {
		return (boolean) appSettingsHash.get(DataType.BOOLEAN).get(name);
	}
	
	public String getStringSetting(SettingName name) {
		return (String) appSettingsHash.get(DataType.STRING).get(name);
	}
	
	public void setIntSetting(SettingName name, int value) {
		System.out.println("Setting Int Setting");
		if (isDataTypeType(name, DataType.INTEGER)) {
			appSettingsHash.get(DataType.INTEGER).put(name, value);
			saveSetting();
		}
	}
	
	public void setBooleanSetting(SettingName name, boolean value) {
		System.out.println("Setting Boolean Setting");
		if (isDataTypeType(name, DataType.BOOLEAN)) {
			appSettingsHash.get(DataType.BOOLEAN).put(name, value);
			saveSetting();
		}
	}
	
	public void setStringSetting(SettingName name, String value) {
		System.out.println("Setting String Setting");
		if (isDataTypeType(name, DataType.STRING)) {
			appSettingsHash.get(DataType.STRING).put(name, value);
			saveSetting();
		}
	}
	
	public void saveSetting() {
		try {
			FileOutputStream fos = new FileOutputStream("settings.dat");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			
			oos.writeObject(appSettingsHash);
			oos.close();
			fos.close();
		} catch (IOException ioe) {
			log.log(Level.FINE, "Failed to add setting");
		}
	}

	public enum DataType {
		INTEGER, BOOLEAN, STRING
	}
	
	public enum SettingName {
		USERNAME, CURRENT_INDEX, ISFREE
	}	
}
