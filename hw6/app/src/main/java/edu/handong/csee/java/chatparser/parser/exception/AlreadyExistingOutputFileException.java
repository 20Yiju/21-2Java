package edu.handong.csee.java.chatparser.parser.exception;

/**
 * class for AlreadyExistingOutputFileException
 * @author jeong-yiju
 *
 */
public class AlreadyExistingOutputFileException extends Exception{
	
	/**
	 * AlreadyExistingOutputFileException()
	 */
	public AlreadyExistingOutputFileException() {
		super("AlreadyExistingOutputFileException: ");
	}
	
	/**
	 * AlreadyExistingOutputFileException(String message)
	 * @param message put my exception message
	 */
	public AlreadyExistingOutputFileException(String message) {
		super (message);
	}
	

}
