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
		//message = message.substring(17, message.length());
		for(int i = 0; i < message.length(); i++) {
			if(message.charAt(i) == '[' || message.charAt(i) == ']') {
				num++;
			}
			
		}
		//System.out.println(message);
		//System.out.println(num);
		if(num == 4 || num == 6 || num == 8) {
			//message = message.substring(17, message.length());
			String[] pmes = message.split("] ");
			String wrt = pmes[0].trim();
			wrt = wrt.replace("[", "");
			wrt = wrt.replace("]", "");
			String date = pmes[1].trim();
			date = date.replace("[", "");
			date = date.replace("]", "");
			//System.out.println(date);
			
			
			if(date.contains("오후")) {
				
				String[] pdate = date.split(":");
				String pm = pdate[0].substring(0, 2);
				String min = pdate[1];
				//System.out.println(pdate[0]);
				if(pdate[0].length() == 4) {
					int hour = Integer.parseInt(pdate[0].substring(pdate[0].length()-1, pdate[0].length()));
					
					if(pm.equals("오후")) {
						
						hour= (hour + 12);
					}
					
					date = hour + ":" + min;
				}
				
				else if(pdate[0].length()== 5) {
					int hour = Integer.parseInt(pdate[0].substring(pdate[0].length()-2, pdate[0].length()));
					
					if(hour % 12 == 0) {
						hour = 12;
					}
					else if(hour % 12 != 0) {
						hour += 12;
						//System.out.println(hour);
					}
					date = hour + ":" + min;
				}
				
			}
			
			if(date.contains("오전")) {
				String[] pdate = date.split(":");
				String am = pdate[0].substring(0, 2);
				String min = pdate[1];
				
				
				if(pdate[0].length() == 4) {
					String hour = pdate[0].substring(pdate[0].length()-1, pdate[0].length());
					
					hour = 0 + hour;
					
					date = hour + ":" + min;
				}
				if(pdate[0].length() == 5) {
					String hour = pdate[0].substring(pdate[0].length()-2, pdate[0].length());
					
					if(hour.equals("12")) {
						hour = "00";
					}
					
					date = hour + ":" + min;
				}
				
				
				
			}
			if(date.contains("PM")) {
				String[] pdate = date.split(":");
				int hour = Integer.parseInt(pdate[0]);
				
				String min = pdate[1].substring(0, 2);
				String pm = pdate[1].substring(pdate[1].length()-2, pdate[1].length());
				
				//System.out.println(min);
				
				if(pm.equals("PM")) {
					hour= (hour + 12);
				}
				if(hour % 12 == 0) {
					hour= 12;
				}
				String h = Integer.toString(hour);
				
				
				date = h + ":" + min;
				
			}
			if(date.contains("AM")) {
				String[] pdate = date.split(":");
				String hour = pdate[0];
				String am = pdate[1].substring(pdate[1].length()-2, pdate[1].length());
				
				String min = pdate[1].substring(0, 2);
				if(pdate[0].length() == 1) {
					hour = 0 + hour;
				}
				if(hour.equals("12")) {
					hour = "00";
				}
				
				date = hour + ":" + min;
			}
			
			String mes = pmes[2].trim();
			
			parsemessage.datesetter(date);
			parsemessage.writersetter(wrt);
			parsemessage.messagesetter(mes);
			return parsemessage;
		
		}

		else {
			
			//message = message.substring(17, message.length());
			String[] pmes = message.split("] ");
			String wrt = pmes[0].trim();
			wrt = wrt.replace("[", "");
			wrt = wrt.replace("]", "");
			String date = pmes[1].trim();
			date = date.replace("[", "");
			date = date.replace("]", "");
			
			
			if(date.contains("오후")) {
				String[] pdate = date.split(":");
				String pm = pdate[0].substring(0, 2);
				String min = pdate[1];
				
				if(pdate[0].length() == 4) {
					int hour = Integer.parseInt(pdate[0].substring(pdate[0].length()-1, pdate[0].length()));
					
					if(pm.equals("오후")) {
						
						hour= (hour + 12);
					}
					
					date = hour + ":" + min;
				}
				
				if(pdate[0].length() == 5) {
					String hour = pdate[0].substring(pdate[0].length()-2, pdate[0].length());
					if(hour == "12") {
						hour= "12";
					}
					
					date = hour + ":" + min;
				}
				
			}
			else if(date.contains("오전")) {
				String[] pdate = date.split(":");
				String am = pdate[0].substring(0, 2);
				String min = pdate[1];
				
				if(pdate[0].length() == 4) {
					String hour = pdate[0].substring(pdate[0].length()-1, pdate[0].length());
					
					hour = 0 + hour;
					
					date = hour + ":" + min;
				}
				if(pdate[0].length() == 5) {
					String hour = pdate[0].substring(pdate[0].length()-2, pdate[0].length());
					if(hour.equals("12")) {
						hour = "00";
					}
					
					date = hour + ":" + min;
				}
				
			}
			
			if(date.contains("PM")) {
				String[] pdate = date.split(":");
				int hour = Integer.parseInt(pdate[0]);
				
				String min = pdate[1].substring(0, 2);
				String pm = pdate[1].substring(pdate[1].length()-2, pdate[1].length());
				
				//System.out.println(min);
				
				if(pm.equals("PM")) {
					hour= (hour + 12);
				}
				if(pm.equals("PM") && hour % 12 == 0) {
					hour= 12;
				}
				String h = Integer.toString(hour);
				
				
				date = h + ":" + min;
				
			}
			
			else if(date.contains("AM")) {
				String[] pdate = date.split(":");
				String hour = pdate[0];
				String am = pdate[1].substring(pdate[1].length()-2, pdate[1].length());
				
				String min = pdate[1].substring(0, 2);
				if(pdate[0].length() == 1) {
					hour = 0 + hour;
				}
				else if(pdate[0].equals("12")) {
					hour = "00";
				}
				
				date = hour + ":" + min;
			}
			
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
