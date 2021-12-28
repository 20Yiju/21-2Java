package edu.handong.csee.java.chatparser;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import edu.handong.csee.java.chatparser.datamodel.Message;
import edu.handong.csee.java.chatparser.parser.ParserForMac;
import edu.handong.csee.java.chatparser.parser.ParserForWindows;
import edu.handong.csee.java.chatparser.parser.Parserable;
import edu.handong.csee.java.chatparser.parser.exception.AlreadyExistingOutputFileException;
import edu.handong.csee.java.chatparser.parser.exception.DataFileNotFoundException;

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
	 * @param args input path
	 * @throws IOException control I/O exception
	 */
	public static void main(String[] args) throws IOException {
		
		ChatParser myChatParser = new ChatParser();
		
		myChatParser.run(args);

	}
	
	/**
	 * method for running program
	 * @param args input path
	 * @throws IOException control I/O exception
	 */
	public void run(String[] args) throws IOException {
		
		String ChatMessages;
		
		if(args.length == 0) {
			System.out.println("No data and/or output paths are provided!!!");
			return;
			
		}
		/**
		 * try block
		 */
		try {
			File f = new File(args[1]); //result
			File g = new File(args[0]); //data
			
			if(g.exists() == false) {
				throw new DataFileNotFoundException();
			}
			
			
			if(f.exists() == true) {
				BufferedReader inputStream2= new BufferedReader(new FileReader(args[0]));				
				int ind = 0;
				String Mess = "";
				while((Mess = inputStream2.readLine()) != null) {					
					if(Utils.isMacExportedMessage(Mess) || Utils.isWindowsExportedMessage(Mess))	
						ind++;
				}
				System.out.println("The number of all messages processing: " + ind + "\n");
				inputStream2.close();
				throw new AlreadyExistingOutputFileException();
			}
			
			BufferedReader inputStream= new BufferedReader(new FileReader(args[0]));
			BufferedWriter filewriter = new BufferedWriter(new FileWriter(args[1]));
			
			
			
			int index = 0;
			while((ChatMessages = inputStream.readLine()) != null) {
		
				String currentChatMessage = ChatMessages;

				Parserable parser = null;
				
				if(Utils.isMacExportedMessage(currentChatMessage))	
					parser = new ParserForMac();
				else if (Utils.isWindowsExportedMessage(currentChatMessage))
					parser = new ParserForWindows();
				
				if(currentChatMessage.length() == 0 && index > 4 && parser == null) {
					filewriter.append("\n");
				}
				else if(parser == null && currentChatMessage.length() != 0 && currentChatMessage.charAt(0) != '-' && index > 4) {
					
					if (currentChatMessage.contains("들어왔습니다.") || currentChatMessage.contains("나갔습니다.")) {
						continue;
					}
					if(currentChatMessage.contains(" left.") || currentChatMessage.contains(" joined this chatroom.")) {
						continue;
					}
					else {
						filewriter.append("\n" + currentChatMessage);
					}
					
				}
	
				if(parser != null) {
					if (index != 0) {
						filewriter.append("\n");
						filewriter.append("\n");
						
					}
					filewriter.append("Parsing Message " + ++index);
					
					
					Message newMessage = parser.parse(currentChatMessage);
					filewriter.append("\n" + "When: " + newMessage.dategetter());
					filewriter.append("\n" + "Who: " + newMessage.writergetter());
					filewriter.append("\n" + "What: " + newMessage.messagegetter());
					
				}		
				
			}
			System.out.println("The number of all messages processing: " + index);
			
			
			System.out.println("\nAll the parsed messages are saved in " + args[1]);
			
			inputStream.close();
			filewriter.close();
			
		} 
		/**
		 * catch block for DataFileNotFoundException
		 */
		catch(DataFileNotFoundException e) {
			System.out.println("DataFileNotFoundException: " + args[0]);
			
			
		}
		/**
		 * catch block for AlreadyExistingOutputFileException
		 */
		catch(AlreadyExistingOutputFileException e) {
			System.out.println("AlreadyExistingOutputFileException: " + args[1]);
			
		}
		
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
