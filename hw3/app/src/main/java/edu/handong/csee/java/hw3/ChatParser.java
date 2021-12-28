package edu.handong.csee.java.hw3;

import edu.handong.csee.java.hw3.datamodel.Message;
import edu.handong.csee.java.hw3.parser.ParserForMac;
import edu.handong.csee.java.hw3.parser.ParserForWindows;
import edu.handong.csee.java.hw3.parser.Parserable;

/**
 * main class to print and run program
 * @author jeong-yiju
 *
 */
public class ChatParser {
	
	private int numOfMessages=0;
	
	/**
	 * enumeration constant
	 * @author jeong-yiju
	 *
	 */
	public static enum ValueTypes {
		/**
		 * datetime type
		 */
		DATETIME,
		/**
		 * writer type
		 */
		WRITER, 
		/**
		 * message type
		 */
		MESSAGE};

	/**
	 * main method to run program
	 * @param args input message
	 */
	public static void main(String[] args) {
		
		ChatParser myChatParser = new ChatParser();
		
		myChatParser.run(args);

	}
	
	/**
	 * program run method
	 * @param ChatMessages input message
	 */
	public void run(String[] ChatMessages) {
		
		setNumOfMessages(ChatMessages.length);
		
		if(getNumOfMessages() != 0) {
			System.out.println("The number of all messages processing: " + getNumOfMessages() + "\n");
		}
		
		for(int messageIndex = 0; messageIndex < this.getNumOfMessages(); messageIndex++) {

			String currentChatMessage = ChatMessages[messageIndex];

			Parserable parser = null;

			System.out.println("Parsing Message " + (messageIndex+1));
			
			if(Utils.isMacExportedMessage(currentChatMessage))	
				parser = new ParserForMac();
			else if (Utils.isWindowsExportedMessage(currentChatMessage))
				parser = new ParserForWindows();
			else {
				System.out.println("Message cannot be processed as its format is not supported!!: " + currentChatMessage);
				return;
			}

			Message newMessage = parser.parse(currentChatMessage);
			System.out.println(parser.getValue(ValueTypes.DATETIME) + 
								"|" + parser.getValue(ValueTypes.WRITER) + 
								"|" + parser.getValue(ValueTypes.MESSAGE));
			newMessage.print();

			System.out.println();
			
		}

		if(ChatMessages.length==0)
			System.out.println("No messages!!");
		
	}
	
	/**
	 * get total number of messages received
	 * @return number of input message
	 */
	public int getNumOfMessages() {
		return numOfMessages;
	}
	
	/**
	 * set total number of messages received
	 * @param numOfMessages number of input message
	 */
	public void setNumOfMessages(int numOfMessages) {
		this.numOfMessages = numOfMessages;
	}

}
