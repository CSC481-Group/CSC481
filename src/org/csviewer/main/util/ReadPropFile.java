package org.csviewer.main.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ReadPropFile {

	
	public static Properties initProp() throws IOException {
		
		FileInputStream fis = new FileInputStream("resources/config.properties");
		
		Properties prop = new Properties();
		
		prop.load(fis);
		
		
		return prop;
	}
	
	public static void updateProp() throws IOException {
		
		Properties updProp = initProp();
		
		
		
		if (returnCheck() == true) {
		
			updProp.setProperty("continuePanel","false");
		
		} else {
			
			updProp.setProperty("continuePanel", "true");
			
		}
		
		FileOutputStream fin = new FileOutputStream("resources/config.properties");
		
		updProp.store(fin, null);
	}
	
	public static Boolean returnCheck() throws IOException {
		
		Properties checkProp = initProp();
		
		Boolean ackCheck = Boolean.parseBoolean(checkProp.getProperty("continuePanel"));
		
		return ackCheck;
		
	}

}
