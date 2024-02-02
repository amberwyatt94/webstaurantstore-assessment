package com.assessment.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class PropertyReader {

	// To read keys from properties file
	public static String readQaConfigFile(String key) {
		try {
			Properties properties = new Properties();
			properties.load(new FileInputStream(new File(GlobalVariables.qaConfigFilePath)));

			return properties.getProperty(key);
		} catch (Exception e) {
			return null;
		}
	}

	public static String readDev1ConfigFile(String key) {
		try {
			Properties properties = new Properties();
			properties.load(new FileInputStream(new File(GlobalVariables.dev1ConfigFilePath)));

			return properties.getProperty(key);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String readDev2ConfigFile(String key) {
		try {
			Properties properties = new Properties();
			properties.load(new FileInputStream(new File(GlobalVariables.dev2ConfigFilePath)));

			return properties.getProperty(key);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String readProdConfigFile(String key) {
		try {
			Properties properties = new Properties();
			properties.load(new FileInputStream(new File(GlobalVariables.prodConfigFilePath)));

			return properties.getProperty(key);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String readlegacysiteConfigFile(String key) {
		try {
			Properties properties = new Properties();
			properties.load(new FileInputStream(new File(GlobalVariables.legacysiteConfigFilePath)));

			return properties.getProperty(key);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String readstagingConfigFile(String key) {
		try {
			Properties properties = new Properties();
			properties.load(new FileInputStream(new File(GlobalVariables.stagingConfigFilePath)));

			return properties.getProperty(key);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String readApplicationConfigFile(String key) {
		try {
			Properties properties = new Properties();
			properties.load(new FileInputStream(new File(GlobalVariables.applicationConfigPath)));

			return properties.getProperty(key);
		} catch (Exception e) {
			return null;
		}
	}
}