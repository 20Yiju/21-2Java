package edu.handong.csee.java.hw3;

/**
 * 
 * @author jeong-yiju
 * version checking class
 */
public class Utils {
	
	/**
	 * method of checking T/F Mac version
	 * @param message input message
	 * @return true or false
	 */
	public static boolean isMacExportedMessage(String message) {
		
		// TODO: implement this static method to check if the message is exported form Mac.
		if(message.indexOf('-') == 4) {
			return true;
		}
			
		
		return false;

	}
	
	/**
	 * method of checking T/F Window version
	 * @param message input message
	 * @return true or false
	 */
	public static boolean isWindowsExportedMessage(String message) {
		
		// TODO: implement this static method to check if the message is exported form Windows.
		
		if(message.indexOf('[') == 0) {
			return true;
		}
		

		
		return false;
	}

}
