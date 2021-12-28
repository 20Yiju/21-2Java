package edu.handong.csee.java.chatparser.parser;

import edu.handong.csee.java.chatparser.ChatParser.ValueTypes;
import edu.handong.csee.java.chatparser.datamodel.Message;

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
		int num = 0;
		for(int i = 0; i < message.length(); i++) {
			if(message.charAt(i) == '[' || message.charAt(i) == ']') {
				num++;
			}
			
		}
		//System.out.println(message);
		//System.out.println(num);
		if(num == 4 || num == 6) {
			
			String[] pmes = message.split("] ");
			String wrt = pmes[0].trim();
			wrt = wrt.replace("[", "");
			wrt = wrt.replace("]", "");
			String date = pmes[1].trim();
			date = date.replace("[", "");
			date = date.replace("]", "");
			String mes = pmes[2].trim();
			
			parsemessage.datesetter(date);
			parsemessage.writersetter(wrt);
			parsemessage.messagesetter(mes);
			return parsemessage;
			
		}
		else {
			
			String[] pmes = message.split("] ");
			String wrt = pmes[0].trim();
			wrt = wrt.replace("[", "");
			wrt = wrt.replace("]", "");
			String date = pmes[1].trim();
			date = date.replace("[", "");
			date = date.replace("]", "");
			String mes1 = pmes[2].trim();
			String mes2 = pmes[3].trim();
			String mes = mes1 + "] " +mes2;
			
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
