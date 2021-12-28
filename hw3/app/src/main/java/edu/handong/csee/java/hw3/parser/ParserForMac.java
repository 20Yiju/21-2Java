package edu.handong.csee.java.hw3.parser;

import edu.handong.csee.java.hw3.ChatParser.ValueTypes;
import edu.handong.csee.java.hw3.datamodel.Message;

/**
 * 
 * @author jeong-yiju
 * class of parsemessage in Mac version
 */
public class ParserForMac implements Parserable {
	/**
	 * instance of Message
	 */
	Message parsemessage = new Message();
	
	/**
	 * message parse method
	 */
	@Override
	public Message parse(String message) {
		
		/**
		 * field of message split with "," 
		 */
		String[] pmes = message.split(",");
		String date = pmes[0].trim();
		String wrt = pmes[1].trim();
		String mes = pmes[2].trim();
		
		parsemessage.datesetter(date);
		parsemessage.writersetter(wrt);
		parsemessage.messagesetter(mes);
		return parsemessage;
		
	
	}
	
	/**
	 * get proper value need
	 */
	@Override
	public String getValue(ValueTypes type) {
		
		if(type == ValueTypes.DATETIME) {
			return parsemessage.dategetter();
		}
		else if(type == ValueTypes.WRITER) {
			return parsemessage.writergetter();
		}
		else if(type == ValueTypes.MESSAGE) {
			return parsemessage.messagegetter();
		}
		return null;
	}
	
	
}
