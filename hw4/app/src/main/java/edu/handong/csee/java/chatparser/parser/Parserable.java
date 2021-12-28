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
	 */
	public Message parse(String message);
	/**
	 * get proper value need
	 */
	public String getValue(ChatParser.ValueTypes type);
	
}
