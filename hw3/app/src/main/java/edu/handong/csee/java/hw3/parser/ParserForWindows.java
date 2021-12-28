package edu.handong.csee.java.hw3.parser;

import edu.handong.csee.java.hw3.ChatParser.ValueTypes;
import edu.handong.csee.java.hw3.datamodel.Message;

/**
 * 
 * @author jeong-yiju
 * class of parsemessage in Window version
 */
public class ParserForWindows implements Parserable{
	/**
	 * instance of Message
	 */
	Message parsemessage = new Message();
	
	/**
	 * message parse method
	 */
	@Override
	public Message parse(String message) {
		
		message = message.replace("[", "");
		message = message.replace("]", "#");
		
		String[] pmes = message.split("#");
		String wrt = pmes[0].trim();
		String date = pmes[1].trim();
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
