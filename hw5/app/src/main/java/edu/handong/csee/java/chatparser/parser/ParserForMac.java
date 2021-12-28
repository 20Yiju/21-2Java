package edu.handong.csee.java.chatparser.parser;

import edu.handong.csee.java.chatparser.ChatParser.ValueTypes;
import edu.handong.csee.java.chatparser.datamodel.Message;

import org.apache.commons.csv.*;

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
		if(a == 4) {
			
			
			String[] pmes = message.split(",");
			String date = pmes[0].trim();
		
			//date = date.substring(0, date.length() - 3);
			String wrt = pmes[1].trim();
			String mes1 = pmes[2].trim();
			String mes2 = pmes[3].trim();
			String mes3 = pmes[4].trim();
			//System.out.println(mes1);
			//System.out.println(mes2);
			String mes = mes1 + ", " + mes2 + ", " + mes3;
			//System.out.println(mes);
			
			parsemessage.datesetter(date);
			parsemessage.writersetter(wrt);
			parsemessage.messagesetter(mes);
			return parsemessage;
		}
		if(a == 3) {
			
			String[] pmes = message.split(",");
			String date = pmes[0].trim();
			String wrt = pmes[1].trim();
			String mes1 = pmes[2].trim();
			String mes2 = pmes[3].trim();
			//System.out.println(mes1);
			//System.out.println(mes2);
			
			if(mes1.contains("Alright")) {
				//System.out.println("YES");
				String mes = mes1 + "," + mes2;
				parsemessage.messagesetter(mes);
			}
			else {
				String mes = mes1 + ", " + mes2;
				parsemessage.messagesetter(mes);
			}
			
			parsemessage.datesetter(date);
			parsemessage.writersetter(wrt);
			
			return parsemessage;
			
		}
		if(a == 5) {
			
			String[] pmes = message.split(",");
			String date = pmes[0].trim();
			String wrt = pmes[1].trim();
			String mes1 = pmes[2].trim();
			String mes2 = pmes[3].trim();
			String mes3 = pmes[4].trim();
			String mes4 = pmes[5].trim();
			//System.out.println(mes1);
			//System.out.println(mes2);
	
			String mes = mes1 + ", " + mes2 + ", " + mes3 + ", " + mes4;
			
			//System.out.println(mes);
			
			parsemessage.datesetter(date);
			parsemessage.writersetter(wrt);
			parsemessage.messagesetter(mes);
			return parsemessage;
		}
		if(a >  5) {
			String[] pmes = message.split(",");
			
			String date = pmes[0].trim();
			String wrt = pmes[1].trim();

			String mes = message.substring(message.indexOf('I'), message.length());
	
			parsemessage.datesetter(date);
			parsemessage.writersetter(wrt);
			parsemessage.messagesetter(mes);
			return parsemessage;
		}
		
	
		else {
			
			String[] pmes = message.split(",");
			String date = pmes[0].trim();
			String wrt = pmes[1].trim();
			String mes = pmes[2].trim();
			
			
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
