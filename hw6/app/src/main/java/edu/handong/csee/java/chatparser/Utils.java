package edu.handong.csee.java.chatparser;

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
		int j = 0;
		
		if(message.length() < 10)
				return false;
		
		for(int i = 0; i < 10; i++) {
			if(message.charAt(i) == '-') {
				j++;
			}

		}
		if(j == 2 && message.charAt(0) != '[') {
			return true;
		}
	
		// TODO: implement this static method to check if the message is exported form Mac.

		
		return false;

	}
	
	/**
	 * method of checking T/F Window version
	 * @param message input message
	 * @return true or false
	 */
	public static boolean isWindowsExportedMessage(String message) {
		
		// TODO: implement this static method to check if the message is exported form Windows.
		
		if(message.length() == 0)
			return false;
		
		else if(message.charAt(0) == '[') {
			return true;
		}
		
		return false;
	}

}
