package edu.handong.csee.java.chatparser.parser.exception;

/**
 * class for DataFileNotFoundException
 * @author jeong-yiju
 *
 */
public class DataFileNotFoundException extends Exception{

	/**
	 * DataFileNotFoundException()
	 */
	public DataFileNotFoundException() {
		super("DataFileNotFoundException: ");
	}
	
	/**
	 * DataFileNotFoundException(String message)
	 * @param message put my exception message
	 */
	public DataFileNotFoundException(String message) {
		super (message);
	}
	
	

}
