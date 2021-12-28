package edu.handong.csee.java.chatparser.parser;

import edu.handong.csee.java.chatparser.ChatParser.ValueTypes;
import edu.handong.csee.java.chatparser.datamodel.Message;

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
		//System.out.println(message);
		/**
		 * field of message split with "," 
		 */
		int a = 0;
		for(int i = 0; i < message.length(); i++) {
			//System.out.println(message.charAt(i));
			if(message.charAt(i) == ',') {
				a++;
			}
		}
		//System.out.println(a);
		if(a > 2) {
			String[] pmes = message.split(",");
			String date = pmes[0].trim().replace("\"", "");
			String wrt = pmes[1].trim().replace("\"", "");
			String mes1 = pmes[2].trim().replace("\"", "");
			String mes2 = pmes[3].trim().replace("\"", "");
			//System.out.println(mes1);
			//System.out.println(mes2);
	
			String mes = mes1 + ", " + mes2;
			//System.out.println(mes);
			
			parsemessage.datesetter(date);
			parsemessage.writersetter(wrt);
			parsemessage.messagesetter(mes);
			return parsemessage;
		}
		else {
			String[] pmes = message.split(",");
			String date = pmes[0].trim().replace("\"", "");
			String wrt = pmes[1].trim().replace("\"", "");
			String mes = pmes[2].trim().replace("\"", "");
			
			parsemessage.datesetter(date);
			parsemessage.writersetter(wrt);
			parsemessage.messagesetter(mes);
			return parsemessage;
		}
		
		
		
	
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
