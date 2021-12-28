package edu.handong.csee.java.hw3.datamodel;

/**
 * 
 * @author jeong-yiju
 * Message class with method get and set message
 */
public class Message {
	String datetime;
	String writer;
	String message;
	
	/**
	 * Method for putting values in datetime
	 * @param date value
	 */
	public void datesetter(String date) {
		datetime = date;
	}
	
	/**
	 * Method for putting values in writer
	 * @param wrt writer value
	 */
	public void writersetter(String wrt) {
		writer = wrt;
	}
	/**
	 * Method for putting values in message
	 * @param mes message value
	 */
	public void messagesetter(String mes) {
		message = mes;
	}
	
	/**
	 * Method for get values in datetime
	 * @return datetime value
	 */
	public String dategetter() {
		return datetime;
	}
	
	/**
	 * Method for get values in writer
	 * @return writer value
	 */
	public String writergetter() {
		return writer;
		
	}

	/**
	 * Method for get values in message
	 * @return message value
	 */
	public String messagegetter() {
		return message;
		
	}
	
	/**
	 * print datetime, writer, message method
	 */
	public void print() {
				
		System.out.println("When: " + datetime);
		System.out.println("Who: " + writer);
		System.out.println("What: " + message);
		
	}

	
}
