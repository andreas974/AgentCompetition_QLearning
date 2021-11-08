package edu.kit.exp.common;

import java.util.Iterator;
import java.util.Properties;

/**
 * Class for constants used by client and server
 * 
 */
public class Constants {

	/* Server Connection Settings */
	public static final Integer SERVER_PORT = 7778;
	public static final String SERVER_RMI_OBJECT_NAME = "ServerCommunicationObject";
	public static final String TIME_STAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";

	/* read from properties files */
	private static boolean systemDebugMode;
	private static String serverName;

	public static boolean isSystemDebugMode() {
		return systemDebugMode;
	}

	public static String getServerName() {
		return serverName;
	}

	static {
		addSystemProperties(new String[] { "META-INF/systemdefault.properties", "META-INF/system.properties" });
	}

	public static void addSystemProperties(String[] propertyFiles) {
		Properties systemProperties = loadProperties(propertyFiles);

		if (systemProperties.containsKey("debugmode"))
			systemDebugMode = Boolean.valueOf((String) systemProperties.get("debugmode"));
		if (systemProperties.containsKey("server"))
			serverName = systemProperties.getProperty("server");
	}

	/***
	 * Reads multiple properties files parsed by {paramref propertyFiles}. First
	 * file read first. Indentically named properties are overwritten by later
	 * read properties files.
	 * 
	 * @param propertyFiles
	 * @return
	 */
	public static Properties loadProperties(String[] propertyFiles) {
		Properties returnProperties = new Properties();
		for (String propertyFile : propertyFiles) {
			try {
				Properties tmpProperties = new Properties();
				tmpProperties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(propertyFile));
				for (Iterator<Object> iterator = tmpProperties.keySet().iterator(); iterator.hasNext();) {
					String key = (String) iterator.next();
					returnProperties.put(key, tmpProperties.get(key));
				}
			} catch (Exception e) {
				System.out.println("Property file not found: " + propertyFile);
				// e.printStackTrace();
			}
		}

		return returnProperties;
	}
}