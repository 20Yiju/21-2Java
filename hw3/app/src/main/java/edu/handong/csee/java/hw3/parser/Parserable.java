package edu.handong.csee.java.hw3.parser;

import edu.handong.csee.java.hw3.ChatParser;
import edu.handong.csee.java.hw3.datamodel.Message;

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
