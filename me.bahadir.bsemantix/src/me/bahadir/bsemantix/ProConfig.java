package me.bahadir.bsemantix;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.core.runtime.Platform;

public class ProConfig {
	
	public static final String MONGODB_HOST = "mongo-host";
	public static final String MONGODB_PORT = "mongo-port";
	public static final String TDB_LOCATION = "tdb-location";
	public static final Properties properties;
	static {

		
		// Create properties file if not exist
		
		File file = new File("bsxconfig.cfg");
		properties = new Properties();
			try {
				if (!file.exists()) {
					
					loadDefaults(properties);
					properties.store(new FileOutputStream("bsxconfig.cfg"), "Configiuration file for BSemantiX");
				} else {
					properties.load(new FileInputStream("bsxconfig.cfg"));
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}

	public static String get(String key) {
		return properties.getProperty(key);
	}
	
	private static void loadDefaults(Properties props) {
		props.setProperty(MONGODB_HOST, "127.0.0.1");
		props.setProperty(MONGODB_PORT, "27017");
		//props.setProperty(TDB_LOCATION, Platform.getInstanceLocation().getURL())
	}

}
