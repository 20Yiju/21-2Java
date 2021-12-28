package edu.handong.csee.java.chatparser.parser;

import edu.handong.csee.java.chatparser.ChatParser;
import edu.handong.csee.java.chatparser.datamodel.Message;

/**
 * 
 * @author jeong-yiju
 * interface of Parser
 */
public interface Parserable {
	
	/**
	 * message parse method
	 * @param message input message
	 * @return return parsing message
	 */
	public Message parse(String message);
	
	/**
	 * get proper value need
	 * @param type date or writer or message
	 * @return each parsing message
	 */
	public String getValue(ChatParser.ValueTypes type);
	
}
