package edu.handong.csee.java.chatparser;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import org.apache.commons.csv.*;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Arrays;

import edu.handong.csee.java.chatparser.datamodel.Message;
import edu.handong.csee.java.chatparser.ds.CustomLinkedList;
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
@SuppressWarnings("unchecked")
public class ChatParser {
	private boolean help;
	private String date;
	private String enddate;
	private String startdate;
	static String input;
	static String output;
	private String writer;
	static String in = "";
	static String out = "";
	static String fileName = "";
	static String[] he;
	static String I;
	static ArrayList<Runnable> run = new ArrayList<Runnable>();
	
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
		
		
		String fN = "";
		int fco = 0;
		//String dir = "";
		/**
		 * map for writer
		 */
		HashMap<String, CustomLinkedList> mywriter = new HashMap<String, CustomLinkedList>();
		/**
		 * map for date
		 */
		HashMap<String, CustomLinkedList> mydate = new HashMap<String, CustomLinkedList>();		
		

		
		/**
		 * for CLI 
		 */
		Options options = createOptions();
		String sin = "";
		
		if(parseOptions(options, args)){
			if (help || args.length == 0){
				printHelp(options);
				return;
			}
			
			String ChatMessages;
			
			/**
			 * try block
			 */
			try {
				
				if(!input.contains("csv") && !input.contains("txt") ) {
					ArrayList<Thread> threads = new ArrayList<Thread>();
					//dir = "dir";
					I = args[1];
					File rf = new File(args[1]);
					
					File[] fileList = rf.listFiles();
					
					for(File file : fileList) {
						args[1] = I;
					      if(file.isFile()) {
					    	  fN = file.getName();
					    	  if(fN.contains("csv") || fN.contains("txt")) {
					    		  fileName = fN;
					    		  fco++; 
					    		  Runnable r = new DirFileReader(fileName);
					    		  Thread FN = new Thread(r);
					    		  FN.start();
					    		  threads.add(FN);
					    		  run.add(r);

					    	  }  
					      }
					}
					
					String ip = input.substring(0, input.length()-1);
					System.out.println("The number of files in the directory, " + ip + ": " + fco);
					
					for(Thread v : threads) {	
						try {
							v.join();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					
					for(Runnable runs : run) {
						HashMap<String, CustomLinkedList> WputMap = new HashMap<String, CustomLinkedList>();
						WputMap = ((DirFileReader) runs).retunWriter();
						HashMap<String, CustomLinkedList> DputMap = new HashMap<String, CustomLinkedList>();
						DputMap = ((DirFileReader) runs).retunDate();
						
						mywriter.putAll(WputMap);
						mydate.putAll(DputMap);
						
					}
					
				}
				else {
					
					;

					File f = new File(output); //result
					File g = new File(input); //data
					
					if(g.exists() == false) {
						//System.out.println("YES");
						throw new DataFileNotFoundException();
					}
					
					
					if(f.exists() == true) {
						BufferedReader inputStream2= new BufferedReader(new FileReader(input));				
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
					
					//BufferedReader inputStream= new BufferedReader(new FileReader(input));
					
					
					Reader inf = new FileReader(input);
					BufferedReader inputStream= new BufferedReader(inf);
					
					Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(inf);
					
					
					if(input.contains("csv")) {
						
						String wri = "";
						String mo = "";
						String dat = "";
						
						for (CSVRecord record : records) {
						    String columnOne = record.get(0);
						    String columnTwo = record.get(1);
						    String columnhr = record.get(2);
						    
						    String currentChatMessage = columnOne + ", " + columnTwo + ", " + columnhr;
						 
						    //System.out.println(currentChatMessage);
						    
							Parserable parser = new ParserForMac();
							
							
							Message newMes = parser.parse(currentChatMessage);
							//System.out.println(newMes);
					
							
							String da[] = newMes.dategetter().split(":");
							String[] t = da[0].split(" ");
							dat = t[0];
							
							
							//sSystem.out.println(dat);
							//System.out.println(newMes.messagegetter());
							
							if(!mywriter.containsKey(newMes.writergetter())) {
								//System.out.println("YES");
								CustomLinkedList messagesForWriter = new CustomLinkedList();
								messagesForWriter.addANodeToTail(currentChatMessage);
								mywriter.put(newMes.writergetter(),  messagesForWriter);
								
							}
							else {
								
								CustomLinkedList existingArrayList = mywriter.get(newMes.writergetter());
								existingArrayList.addANodeToTail(currentChatMessage);
								mywriter.put(newMes.writergetter(),  existingArrayList);
							}
							
							if(!mydate.containsKey(dat.trim())) {
								//System.out.println("NO");
								
								CustomLinkedList messagesForDate = new CustomLinkedList();
								String[] curr = currentChatMessage.split(",");
								String dte = curr[0];
								dte = dte.substring(0, dte.length() - 3);
								currentChatMessage = dte + "," + curr[1] + "," + newMes.messagegetter();
								messagesForDate.addANodeToTail(currentChatMessage);
								mydate.put(dat.trim(),  messagesForDate);
								
							}
							else {
								
								CustomLinkedList existingArrayList1 = mydate.get(dat.trim());
								String[] curr = currentChatMessage.split(",");
								String dte = curr[0];
								dte = dte.substring(0, dte.length() - 3);
								currentChatMessage = dte + "," + curr[1] + "," + newMes.messagegetter();
								
								
								//System.out.println(currentChatMessage);
								existingArrayList1.addANodeToTail(currentChatMessage);
								mydate.put(dat.trim(),  existingArrayList1);
							}
							
						}
						
					}
					
					
					
					else {
						int index = 0;
						int in = 0;
						String ymd = "";
						String wri = "";
						String dat = "";
						String mo = "";
						while((ChatMessages = inputStream.readLine()) != null) {
					
							String currentChatMessage = ChatMessages;

							if(currentChatMessage.length() != 0 && currentChatMessage.charAt(0) == '-') {
								ymd = currentChatMessage.replace("-", "");
								if(ymd.contains(",")) {
									String[] pymd = ymd.split(",");
									String[] pmd = pymd[1].split(" ");
									String m = pmd[1].trim();
									String d = pmd[2].trim();
									String y = pymd[2].trim();
									
									if(m.equals("March")) {
										m = "03";
									}
									if(m.equals("April")) {
										m = "04";
									}
									if(m.equals("May")) {
										m = "05";
									}
									if(m.equals("June")) {
										m = "06";
									}
									if(d.length() == 1) {
										d = 0 + d;
									}
									ymd = y + "-" + m + "-" + d + " ";
								}
								else {
									String[] pymd = ymd.split(" ");
									String y = pymd[1].trim();
									String m = pymd[2].trim();
									String d = pymd[3].trim();
									
									y = y.replace("년", "").trim();
									m = m.replace("월", "").trim();
									d = d.replace("일", "").trim();
									
									
									if(d.length() == 1) {
										d = 0 + d;
									}
									if(m.length() == 1) {
										m = 0 + m;
									}
									
									ymd = y + "-" + m + "-" + d + " ";
								}
				
							} 
							

							Parserable parser = null;
							
							
							if (Utils.isWindowsExportedMessage(currentChatMessage)) {
								dat = ymd;
								parser = new ParserForWindows();
							}
							

							
							if(parser != null) {
								Message newMes = parser.parse(currentChatMessage);
								wri = newMes.writergetter().trim();
								if(mo == "m") {
									dat = newMes.dategetter().trim().substring(0, 10);
								}
								
								
								//System.out.println(dat);
								
								if(!mywriter.containsKey(newMes.writergetter())) {
									CustomLinkedList messagesForWriter = new CustomLinkedList();
									
									String[] cur = currentChatMessage.split("]");
									String ch = "[" + newMes.dategetter();
									//currentChatMessage = currentChatMessage.replace(cur[1], ch);
									currentChatMessage = "[" + dat + newMes.dategetter() + currentChatMessage;
									
									messagesForWriter.addANodeToStart(currentChatMessage.trim());
									
									mywriter.put(newMes.writergetter(),  messagesForWriter);
									
								}
								else {
									//System.out.println("YES");
									
									CustomLinkedList existingArrayList = mywriter.get(newMes.writergetter());
									String[] cur = currentChatMessage.split("]");
									String ch = "[" + newMes.dategetter();
									//currentChatMessage = currentChatMessage.replace(cur[1], ch);
									currentChatMessage = "[" + dat + newMes.dategetter() + currentChatMessage;
									
									existingArrayList.addANodeToStart(currentChatMessage.trim());
									mywriter.put(newMes.writergetter(),  existingArrayList);
								}
								
								if(!mydate.containsKey(dat.trim())) {
									
									
									CustomLinkedList messagesForDate = new CustomLinkedList();
									if(mo == "m") {
										String[] curr = currentChatMessage.split(",");
										String dte = curr[0];
										dte = dte.substring(0, dte.length() - 3);
										currentChatMessage = dte + "," + curr[1] + "," + newMes.messagegetter();
										//currentChatMessage = dte + "," + ;
									}
									else {
										String[] cur = currentChatMessage.split("]");
										String ch = "[" + newMes.dategetter();
										//currentChatMessage = currentChatMessage.replace(cur[1], ch);
										currentChatMessage = "[" + dat + newMes.dategetter() + currentChatMessage;
									}
									
									
									//System.out.println(currentChatMessage);
									messagesForDate.addANodeToStart(currentChatMessage.trim());
									mydate.put(dat.trim(),  messagesForDate);
								}
								else {
									//System.out.println("NO");
									CustomLinkedList existingArrayList1 = mydate.get(dat.trim());
									
									if(mo == "m") {
										String[] curr = currentChatMessage.split(",");
										String dte = curr[0];
										dte = dte.substring(0, dte.length() - 3);
										currentChatMessage = dte + "," + curr[1] + "," + newMes.messagegetter();
									}
									else {
										String[] cur = currentChatMessage.split("]");
										String ch = "[" + newMes.dategetter();
										currentChatMessage = "[" + dat + newMes.dategetter() + currentChatMessage;
									}
									
									//System.out.println(currentChatMessage);
									existingArrayList1.addANodeToStart(currentChatMessage.trim());
									mydate.put(dat.trim(),  existingArrayList1);
								}
								
								
								
							}

							
							if(parser == null && currentChatMessage.length() != 0 && currentChatMessage.charAt(0) != '-' && index > 4) {
								
								if (currentChatMessage.contains("들어왔습니다.") || currentChatMessage.contains("나갔습니다.")) {
									continue;
								}
								if(currentChatMessage.contains(" left.") || currentChatMessage.contains(" joined this chatroom.")) {
									continue;
								}
								else {
									//System.out.println(currentChatMessage);
									if(mo.equals("m")) {
										currentChatMessage = currentChatMessage.replace("mac", "\"mac\"");
									}
									
									else {
										currentChatMessage = currentChatMessage.replace("\"\"mac\"\"", "\"mac\"");
									}
									
									CustomLinkedList existingArrayList = mywriter.get(wri.trim());
									ArrayList<String> arrList = existingArrayList.sort(new StringComparator());
									//ArrayList<String> arrList = existingArrayList.toArrayList();
									//existingArrayList.showList();
									
									existingArrayList.deleteHeadNode();
									
									String nt = arrList.get(arrList.size() - 1);
									nt = nt + "\n" + currentChatMessage;
									
									
									existingArrayList.addANodeToStart(nt);
									//existingArrayList.showList();
									//System.exit(0);
									//existingArrayList.addANodeToTail(currentChatMessage);
									mywriter.put(wri.trim(),  existingArrayList);
									
									CustomLinkedList existingArrayList1 = mydate.get(dat.trim());
									ArrayList<String> arrList1 = existingArrayList1.sort(new StringComparator());
									
									//existingArrayList1.showList();
									existingArrayList1.deleteHeadNode();
									//System.out.println("YYYYYYYYYYYYYYYYY");
									
									
									String nt1 = arrList1.get(arrList1.size()-1);
									//existingArrayList1.remove(nt1);
									//System.out.println(nt1);
									nt1 = nt1 + "\n" + currentChatMessage;
									//System.out.println(nt1);
									
									existingArrayList1.addANodeToStart(nt1);
									//existingArrayList1.showList();
									//System.exit(0);
									mydate.put(dat.trim(),  existingArrayList1);
									
									
								}
								
							}
							
							else if(currentChatMessage.length() == 0 && index > 4 && parser == null) {
								//System.out.println("YES");
								
								CustomLinkedList existingArrayList = mywriter.get(wri);
								
								ArrayList<String> arrList = existingArrayList.sort(new StringComparator());
								
								existingArrayList.deleteHeadNode();
								
								String nt = arrList.get(arrList.size() - 1);
								nt = nt + "\n";
								
								existingArrayList.addANodeToStart(nt);
								mywriter.put(wri,  existingArrayList);
								
								CustomLinkedList existingArrayList1 = mydate.get(dat.trim());
								
								ArrayList<String> arrList1 = existingArrayList1.sort(new StringComparator());
								
								existingArrayList1.deleteHeadNode();
								
								String nt1 = arrList1.get(arrList1.size() - 1);
								nt1 = nt1 + "\n";
								existingArrayList1.addANodeToStart(nt1);
								//existingArrayList1.addANodeToStart("\n");
								mydate.put(dat.trim(),  existingArrayList1);
							
								
							}
							index++;
							
							
						}
						
						
					}
					inputStream.close();
				}
				BufferedWriter filewriter = new BufferedWriter(new FileWriter(output));
				
				if(startdate != null && enddate == null && writer == null) {
					String mow = "";
					if(!mydate.containsKey(startdate.trim())) {
						System.out.println("The number of messages filtered by the start date, " + startdate +": 0");
						System.out.println("\nAll the parsed messages are saved in " + output);
						//System.out.println("YES");
						return;
					}
					else {
						int de = 0;
						TreeMap<String, CustomLinkedList> sorted = new TreeMap<>();
						sorted.putAll(mydate);
						
						for(String dkey : sorted.keySet()) {
							//System.out.println(dkey);
							if(dkey.equals(startdate.trim()) || de > 0) {
								
								CustomLinkedList value2 = sorted.get(dkey);
								ArrayList<String> srotedList2 = value2.sort(new StringComparator());
								Collections.sort(srotedList2);
								
								for(String v : srotedList2) {
									Parserable parser2 = null;
									
									if(v.charAt(0) == '[') {
										v = v.substring(34, v.length());
									}
									
									if(Utils.isMacExportedMessage(v)) {
										parser2 = new ParserForMac();
										mow = "m";
									}
										
									
									else if (Utils.isWindowsExportedMessage(v)) {
										parser2 = new ParserForWindows();
										mow = "w";
									}
									
									
									if(parser2 != null) {
										if (de != 0) {
											filewriter.append("\n");
											filewriter.append("\n");
											
										}
										
										filewriter.append("Parsing Message " + ++de);
										Message newMessage = parser2.parse(v);
										
										if(mow == "w") {
											filewriter.append("\n" + "When: " + dkey + " " + newMessage.dategetter());
											filewriter.append("\n" + "Who: " + newMessage.writergetter());
											filewriter.append("\n" + "What: " + newMessage.messagegetter());
										}
										if(mow == "m") {
											filewriter.append("\n" + "When: " + newMessage.dategetter());
											filewriter.append("\n" + "Who: " + newMessage.writergetter());
											filewriter.append("\n" + "What: " + newMessage.messagegetter());
										}
										
									}
								}
								
							}							
						}
						
						
						System.out.println("The number of messages filtered by the start date, " + startdate +": " + de);
					}
					
	
				}
				
				if(startdate != null && enddate == null && writer != null) {
					String mow = "";
					if(!mydate.containsKey(startdate.trim())) {
						System.out.println("The number of messages filtered by the start date, " + startdate + ", and the writer, " + writer +": 0");
						System.out.println("\nAll the parsed messages are saved in " + output);
						//System.out.println("YES");
						return;
					}
					else {
						int de = 0;
						TreeMap<String, CustomLinkedList> sorted = new TreeMap<>();
						sorted.putAll(mydate);
						
						for(String dkey : sorted.keySet()) {
							//System.out.println(dkey);
							
							if(dkey.contains("-")) {
								if(dkey.equals(startdate.trim()) || de > 0) {
									
									CustomLinkedList value2 = sorted.get(dkey);
									ArrayList<String> srotedList2 = value2.sort(new StringComparator());
									Collections.sort(srotedList2);
									
									for(String v : srotedList2) {
										Parserable parser2 = null;
										
										if(v.charAt(0) == '[') {
											v = v.substring(34, v.length());
										}
										
										if(Utils.isMacExportedMessage(v)) {
											parser2 = new ParserForMac();
											mow = "m";
										}
											
										
										else if (Utils.isWindowsExportedMessage(v)) {
											parser2 = new ParserForWindows();
											mow = "w";
										}
										
										//System.out.println(v);
										Message newMessage = parser2.parse(v);
										if(newMessage.writergetter().equals(writer)) {
											if(parser2 != null) {
												if (de != 0) {
													filewriter.append("\n");
													filewriter.append("\n");
													
												}
												
												filewriter.append("Parsing Message " + ++de);
												
												
												if(mow == "w") {
													filewriter.append("\n" + "When: " + dkey + " " + newMessage.dategetter());
													filewriter.append("\n" + "Who: " + newMessage.writergetter());
													filewriter.append("\n" + "What: " + newMessage.messagegetter());
												}
												if(mow == "m") {
													filewriter.append("\n" + "When: " + newMessage.dategetter());
													filewriter.append("\n" + "Who: " + newMessage.writergetter());
													filewriter.append("\n" + "What: " + newMessage.messagegetter());
												}
												
												
												
												
											}
										}
									}
									
								}
							}
														
						}
						
						
						System.out.println("The number of messages filtered by the start date, " + startdate + ", and the writer, " + writer + ": " + de);
					}
					
	
				}
				
				
				
				
				if(startdate == null && enddate != null && writer != null) {
					String mow = "";
					if(!mydate.containsKey(enddate.trim())) {
						System.out.println("The number of messages filtered by the end date, " + enddate + ", and the writer, " + writer + ": 0");
						System.out.println("\nAll the parsed messages are saved in " + output);
						//System.out.println("YES");
						return;
					}
					else {
						int de = 0;
						TreeMap<String, CustomLinkedList> sorted = new TreeMap<>();
						sorted.putAll(mydate);
						
						for(String dkey : sorted.keySet()) {
							//System.out.println(dkey);
							if(dkey.contains("-")) {
								String[] sped = enddate.split("-");
								String[] spedk = dkey.split("-");
								String spedi = sped[1] + sped[2];
								String spedki = spedk[1] + spedk[2];
								int spedit =  Integer.parseInt(spedi);
								int spedkit =  Integer.parseInt(spedki);
								if (spedit >= spedkit ) {
									
									CustomLinkedList value2 = sorted.get(dkey);
									ArrayList<String> srotedList2 = value2.sort(new StringComparator());
									Collections.sort(srotedList2);
								
									for(String v : srotedList2) {
										Parserable parser2 = null;
										
										if(v.charAt(0) == '[') {
											v = v.substring(34, v.length());
										}
										
										if(Utils.isMacExportedMessage(v)) {
											parser2 = new ParserForMac();
											mow = "m";
										}
											
										
										else if (Utils.isWindowsExportedMessage(v)) {
											parser2 = new ParserForWindows();
											mow = "w";
										}
										Message newMessage = parser2.parse(v);
										if(newMessage.writergetter().equals(writer)) {
											if(parser2 != null) {
												if (de != 0) {
													filewriter.append("\n");
													filewriter.append("\n");
													
												}
												
												filewriter.append("Parsing Message " + ++de);
												
												
												if(mow == "w") {
													filewriter.append("\n" + "When: " + dkey + " " + newMessage.dategetter());
													filewriter.append("\n" + "Who: " + newMessage.writergetter());
													filewriter.append("\n" + "What: " + newMessage.messagegetter());
												}
												if(mow == "m") {
													filewriter.append("\n" + "When: " + newMessage.dategetter());
													filewriter.append("\n" + "Who: " + newMessage.writergetter());
													filewriter.append("\n" + "What: " + newMessage.messagegetter());
												}
												
												
												
												
											}
										}
										
										
										
									}
									
								}
							}
							
							
							
							//System.out.println(spedit);
							//System.out.println(spedkit);
							
														
						}
						
						
						System.out.println("The number of messages filtered by the end date, " + enddate +", and the writer, " + writer + ": " + de);
					}
					
	
				}
				
				
				
				
				
				
				if(startdate == null && enddate != null && writer == null) {
					String mow = "";
					if(!mydate.containsKey(enddate.trim())) {
						System.out.println("The number of messages filtered by the end date, " + enddate +": 0");
						System.out.println("\nAll the parsed messages are saved in " + output);
						//System.out.println("YES");
						return;
					}
					else {
						int de = 0;
						TreeMap<String, CustomLinkedList> sorted = new TreeMap<>();
						sorted.putAll(mydate);
						
						for(String dkey : sorted.keySet()) {
							//System.out.println(dkey);
							if(dkey.contains("-")) {
								String[] sped = enddate.split("-");
								String[] spedk = dkey.split("-");
								String spedi = sped[1] + sped[2];
								String spedki = spedk[1] + spedk[2];
								int spedit =  Integer.parseInt(spedi);
								int spedkit =  Integer.parseInt(spedki);
								if (spedit >= spedkit ) {
									
									CustomLinkedList value2 = sorted.get(dkey);
									ArrayList<String> srotedList2 = value2.sort(new StringComparator());
									//Collections.sort(srotedList2);
								
									for(String v : srotedList2) {
										Parserable parser2 = null;
										
										if(v.charAt(0) == '[') {
											v = v.substring(34, v.length());
										}
										
										if(Utils.isMacExportedMessage(v)) {
											parser2 = new ParserForMac();
											mow = "m";
										}
											
										
										else if (Utils.isWindowsExportedMessage(v)) {
											parser2 = new ParserForWindows();
											mow = "w";
										}
										
										
										if(parser2 != null) {
											if (de != 0) {
												filewriter.append("\n");
												filewriter.append("\n");
												
											}
											
											filewriter.append("Parsing Message " + ++de);
											Message newMessage = parser2.parse(v);
											
											if(mow == "w") {
												filewriter.append("\n" + "When: " + dkey + " " + newMessage.dategetter());
												filewriter.append("\n" + "Who: " + newMessage.writergetter());
												filewriter.append("\n" + "What: " + newMessage.messagegetter());
											}
											if(mow == "m") {
												filewriter.append("\n" + "When: " + newMessage.dategetter());
												filewriter.append("\n" + "Who: " + newMessage.writergetter());
												filewriter.append("\n" + "What: " + newMessage.messagegetter());
											}
											
											
											
											
										}
									}
									
								}
							}
							
							
							
							//System.out.println(spedit);
							//System.out.println(spedkit);
							
														
						}
						
						
						System.out.println("The number of messages filtered by the end date, " + enddate +": " + de);
					}
					
	
				}
				
				if(startdate != null && enddate != null && writer == null) {
					String mow = "";
					if(!mydate.containsKey(enddate.trim())) {
						System.out.println("The number of messages filtered by the start date, " + startdate +", and the end date, " + enddate +": 0");
						System.out.println("\nAll the parsed messages are saved in " + output);
						//System.out.println("YES");
						return;
					}
					else {
						int de = 0;
						TreeMap<String, CustomLinkedList> sorted = new TreeMap<>();
						sorted.putAll(mydate);
						for(String dkey : sorted.keySet()) {
							
							String[] sped = enddate.split("-");
							String[] spedk = dkey.split("-");
							String spedi = sped[1] + sped[2];
							String spedki = spedk[1] + spedk[2];
							int spedit =  Integer.parseInt(spedi);
							int spedkit =  Integer.parseInt(spedki);
							
							//System.out.println(spedit);
							//System.out.println(spedkit);
							
							if (dkey.equals(startdate.trim()) && de == 0) {
								CustomLinkedList value2 = sorted.get(dkey);
								ArrayList<String> srotedList2 = value2.sort(new StringComparator());
								Collections.sort(srotedList2);
								
								for(String v : srotedList2) {
									Parserable parser2 = null;
									
									if(v.charAt(0) == '[') {
										v = v.substring(34, v.length());
									}
									
									if(Utils.isMacExportedMessage(v)) {
										parser2 = new ParserForMac();
										mow = "m";
									}
										
									
									else if (Utils.isWindowsExportedMessage(v)) {
										parser2 = new ParserForWindows();
										mow = "w";
									}
									
									
									if(parser2 != null) {
										if (de != 0) {
											filewriter.append("\n");
											filewriter.append("\n");
											
										}
										
										filewriter.append("Parsing Message " + ++de);
										Message newMessage = parser2.parse(v);
										
										if(mow == "w") {
											filewriter.append("\n" + "When: " + dkey + " " + newMessage.dategetter());
											filewriter.append("\n" + "Who: " + newMessage.writergetter());
											filewriter.append("\n" + "What: " + newMessage.messagegetter());
										}
										if(mow == "m") {
											filewriter.append("\n" + "When: " + newMessage.dategetter());
											filewriter.append("\n" + "Who: " + newMessage.writergetter());
											filewriter.append("\n" + "What: " + newMessage.messagegetter());
										}
										
										
										
										
									}
								}
							}
								
							else if (spedit >= spedkit && de != 0) {
								
								CustomLinkedList value2 = sorted.get(dkey);
								ArrayList<String> srotedList2 = value2.sort(new StringComparator());
								Collections.sort(srotedList2);
								
								
								
								for(String v : srotedList2) {
									Parserable parser2 = null;
									
									if(v.charAt(0) == '[') {
										v = v.substring(34, v.length());
									}
									
									if(Utils.isMacExportedMessage(v)) {
										parser2 = new ParserForMac();
										mow = "m";
									}
										
									
									else if (Utils.isWindowsExportedMessage(v)) {
										parser2 = new ParserForWindows();
										mow = "w";
									}
									
									
									if(parser2 != null) {
										if (de != 0) {
											filewriter.append("\n");
											filewriter.append("\n");
											
										}
										
										filewriter.append("Parsing Message " + ++de);
										Message newMessage = parser2.parse(v);
										
										if(mow == "w") {
											filewriter.append("\n" + "When: " + dkey + " " + newMessage.dategetter());
											filewriter.append("\n" + "Who: " + newMessage.writergetter());
											filewriter.append("\n" + "What: " + newMessage.messagegetter());
										}
										if(mow == "m") {
											filewriter.append("\n" + "When: " + newMessage.dategetter());
											filewriter.append("\n" + "Who: " + newMessage.writergetter());
											filewriter.append("\n" + "What: " + newMessage.messagegetter());
										}
										
										
										
										
									}
								}
								
							}							
						}
						
						System.out.println("The number of messages filtered by the start date, " + startdate +", and the end date, " + enddate + ": " + de);

					}
					
	
				}
				
				
				if(startdate != null && enddate != null && writer != null) {
					String mow = "";
					
					int de = 0;
					TreeMap<String, CustomLinkedList> sorted = new TreeMap<>();
					sorted.putAll(mydate);
					for(String dkey : sorted.keySet()) {
						
						String[] sped = enddate.split("-");
						String[] spedk = dkey.split("-");
						String spedi = sped[1] + sped[2];
						String spedki = spedk[1] + spedk[2];
						int spedit =  Integer.parseInt(spedi);
						int spedkit =  Integer.parseInt(spedki);
						
						//System.out.println(spedit);
						//System.out.println(spedkit);
						
						if (dkey.equals(startdate.trim()) && de == 0) {
							CustomLinkedList value2 = sorted.get(dkey);
							ArrayList<String> srotedList2 = value2.sort(new StringComparator());
							Collections.sort(srotedList2);
							
							
							
							for(String v : srotedList2) {
								
								if(v.charAt(0) == '[') {
									v = v.substring(34, v.length());
								}
								
								Parserable parser2 = null;
								
								
								
								if(Utils.isMacExportedMessage(v)) {
									parser2 = new ParserForMac();
									mow = "m";
								}
									
								
								else if (Utils.isWindowsExportedMessage(v)) {
									parser2 = new ParserForWindows();
									mow = "w";
								}
								
								
								
								
								Message newMessage = parser2.parse(v);
								
								
								
								
								if(newMessage.writergetter().equals(writer.trim())) {
									if(parser2 != null) {
										if (de != 0) {
											filewriter.append("\n");
											filewriter.append("\n");
											
										}
										
										filewriter.append("Parsing Message " + ++de);
										
										
										if(mow == "w") {
											filewriter.append("\n" + "When: " + dkey + " " + newMessage.dategetter());
											filewriter.append("\n" + "Who: " + newMessage.writergetter());
											filewriter.append("\n" + "What: " + newMessage.messagegetter());
										}
										if(mow == "m") {
											filewriter.append("\n" + "When: " + newMessage.dategetter());
											filewriter.append("\n" + "Who: " + newMessage.writergetter());
											filewriter.append("\n" + "What: " + newMessage.messagegetter());
										}

									}
								}
								
								
							}
						}
							
						else if (spedit >= spedkit && de != 0) {
							
							CustomLinkedList value2 = sorted.get(dkey);
							ArrayList<String> srotedList2 = value2.sort(new StringComparator());
							Collections.sort(srotedList2);
							
							
							
							for(String v : srotedList2) {

								if(v.charAt(0) == '[') {
									v = v.substring(34, v.length());
								}
								
								Parserable parser2 = null;
								
								if(Utils.isMacExportedMessage(v)) {
									parser2 = new ParserForMac();
									mow = "m";
								}
									
								
								else if (Utils.isWindowsExportedMessage(v)) {
									parser2 = new ParserForWindows();
									mow = "w";
								}
								
								//System.out.println(v);
								Message newMessage = parser2.parse(v);
								
								
								
								
								if(newMessage.writergetter().equals(writer.trim())) {
									
									if(parser2 != null) {
										if (de != 0) {
											filewriter.append("\n");
											filewriter.append("\n");
											
										}
										
										filewriter.append("Parsing Message " + ++de);
										
										
										if(mow == "w") {
											filewriter.append("\n" + "When: " + dkey + " " + newMessage.dategetter());
											filewriter.append("\n" + "Who: " + newMessage.writergetter());
											filewriter.append("\n" + "What: " + newMessage.messagegetter());
										}
										if(mow == "m") {
											filewriter.append("\n" + "When: " + newMessage.dategetter());
											filewriter.append("\n" + "Who: " + newMessage.writergetter());
											filewriter.append("\n" + "What: " + newMessage.messagegetter());
										}

									}
								}
								
								
							}
							
						}							
					}
					
					System.out.println("The number of messages filtered by the start date, " + startdate +", the end date, " + enddate +", and the writer, " + writer + ": " + de);
					
				}
				
				
				
				if(writer == null && date == null && startdate == null && enddate == null ) {
					
					
					/*for(int i=0; i<myDirFileReader.threads.size(); i++) {
			            Thread t = myDirFileReader.threads.get(i);
			            try {
			            	System.out.println(t);
			                t.join();
			            }catch(InterruptedException e) {
			            	e.printStackTrace();
			            }
			        }*/
					
					int de = 0;
					String mow = "";
					String sd = "";
					
					
					if(mydate.size() == 0) {
						//System.out.println("YES");
						System.out.println("The number of all messages processing: 0");
						System.out.println("\nAll the parsed messages are saved in " + output);
						return;
					}
					/*Object[] mapkey = mydate.keySet().toArray();
					Arrays.sort(mapkey);*/
					
					TreeMap<String, CustomLinkedList> sorted = new TreeMap<>();
					sorted.putAll(mydate);
					
					for(String dt : sorted.keySet()) {
						//System.out.println(dt);
						CustomLinkedList value = sorted.get(dt);
						
						ArrayList<String> srotedList = value.sort(new StringComparator());
						Collections.sort(srotedList);
						
						
						for(String v : srotedList) {
							
							for(String sdate : sorted.keySet()) {
								//System.out.println((mydate.get(sdate)));
								if(mydate.get(sdate).onList(v)) {
									//System.out.println("YES");
									sd = sdate;
								}
							}
							
							if(v.charAt(0) == '[') {
								v = v.substring(34, v.length());
							}
							Parserable parser2 = null;
							
							if(Utils.isMacExportedMessage(v)) {
								parser2 = new ParserForMac();
								mow = "m";
							}

							else if (Utils.isWindowsExportedMessage(v)) {
								parser2 = new ParserForWindows();
								mow = "w";
							}
								
							
							if(parser2 != null) {
								if (de != 0) {
									filewriter.append("\n");
									filewriter.append("\n");
									
								}
								filewriter.append("Parsing Message " + ++de);
								
								
								Message newMessage = parser2.parse(v);
								//System.out.println(newMessage.messagegetter());
								
								if(mow == "w") {
									filewriter.append("\n" + "When: " + sd + " " + newMessage.dategetter());
									filewriter.append("\n" + "Who: " + newMessage.writergetter());
									filewriter.append("\n" + "What: " + newMessage.messagegetter());
								}
								else if(mow == "m") {
									filewriter.append("\n" + "When: " + newMessage.dategetter());
									filewriter.append("\n" + "Who: " + newMessage.writergetter());
									filewriter.append("\n" + "What: " + newMessage.messagegetter());
								}
								
							}
							
						}
					}
					System.out.println("The number of all messages processing: " + de);
					
				}
				
				else if(writer != null && startdate == null && enddate == null) {
					
					
					int de = 0;
					String mow = "";
					String sd = "";
					
					TreeMap<String, CustomLinkedList> sorted = new TreeMap<>();
					sorted.putAll(mywriter);
					
					if(!mywriter.containsKey(writer)) {
						System.out.println("The number of messages filtered by the writer, " + writer +": 0");
						System.out.println("\nAll the parsed messages are saved in " + output);
						return;
					}
					CustomLinkedList value = sorted.get(writer);
					ArrayList<String> srotedList = value.sort(new StringComparator());
					Collections.sort(srotedList);
					

					for(String v : srotedList) {
						//System.out.println(v);

						
						//TreeMap<String, CustomLinkedList<String>> sorting = new TreeMap<>();
						//sorting.putAll(mydate);
						
						for(String sdate : sorted.keySet()) {
							//System.out.println(v);
							if(mywriter.get(sdate).onList(v)) {
								//System.out.println("YES");
								sd = v.substring(1, 11);
							}
						}
						
						if(v.charAt(0) == '[') {
							v = v.substring(17, v.length());
							//System.out.println(v);
						}
						
						
						Parserable parser2 = null;
						
						if(Utils.isMacExportedMessage(v)) {
							parser2 = new ParserForMac();
							mow = "m";
						}

						else if (Utils.isWindowsExportedMessage(v)) {
							parser2 = new ParserForWindows();
							mow = "w";
						}
							
						
						if(parser2 != null) {
							if (de != 0) {
								filewriter.append("\n");
								filewriter.append("\n");
								
							}
							filewriter.append("Parsing Message " + ++de);
							
							
							Message newMessage = parser2.parse(v);
							
							
							if(mow == "w") {
								filewriter.append("\n" + "When: " + sd + " " + newMessage.dategetter());
								filewriter.append("\n" + "Who: " + newMessage.writergetter());
								filewriter.append("\n" + "What: " + newMessage.messagegetter());
							}
							if(mow == "m") {
								filewriter.append("\n" + "When: " + newMessage.dategetter().substring(0, newMessage.dategetter().length() - 3));
								filewriter.append("\n" + "Who: " + newMessage.writergetter());
								filewriter.append("\n" + "What: " + newMessage.messagegetter());
							}
							
						}
					}
					
					System.out.println("The number of messages filtered by the writer, " + writer +": " + de);
				}
				
				if(writer == null && date != null && startdate == null && enddate == null) {
					String mow = "";
					if(!mydate.containsKey(date.trim())) {
						System.out.println("The number of messages filtered by the date, " + date +": 0");
						System.out.println("\nAll the parsed messages are saved in " + output);
						//System.out.println("YES");
						return;
					}
					else {
						int de = 0;
						TreeMap<String, CustomLinkedList> sorted = new TreeMap<>();
						sorted.putAll(mydate);
						
						CustomLinkedList value2 = sorted.get(date.trim());
						ArrayList<String> srotedList = value2.sort(new StringComparator());
						Collections.sort(srotedList);
						
						for(String v : srotedList) {
							Parserable parser2 = null;
							
							if(v.charAt(0) == '[') {
								v = v.substring(34, v.length());
							}
							
							if(Utils.isMacExportedMessage(v)) {
								parser2 = new ParserForMac();
								mow = "m";
							}
								
							
							else if (Utils.isWindowsExportedMessage(v)) {
								parser2 = new ParserForWindows();
								mow = "w";
							}
							
							
							if(parser2 != null) {
								if (de != 0) {
									filewriter.append("\n");
									filewriter.append("\n");
									
								}
								
								filewriter.append("Parsing Message " + ++de);
								Message newMessage = parser2.parse(v);
								
								if(mow == "w") {
									filewriter.append("\n" + "When: " + date + " " + newMessage.dategetter());
									filewriter.append("\n" + "Who: " + newMessage.writergetter());
									filewriter.append("\n" + "What: " + newMessage.messagegetter());
								}
								if(mow == "m") {
									filewriter.append("\n" + "When: " + newMessage.dategetter());
									filewriter.append("\n" + "Who: " + newMessage.writergetter());
									filewriter.append("\n" + "What: " + newMessage.messagegetter());
								}
								
								
								
								
							}
						}
						System.out.println("The number of messages filtered by the date, " + date +": " + de);
					}
					
	
				}

				
				

								
				if(date != null && writer != null && startdate == null && enddate == null) {
					
					if(!mydate.containsKey(date.trim()) && !mywriter.containsKey(writer)) {
						System.out.println("The number of messages filtered by the date, " + date +", and the writer, " + writer +": 0");
						System.out.println("\nAll the parsed messages are saved in " + output);
						//System.out.println("YES");
						return;
					}
					
					String mow = "";
					TreeMap<String, CustomLinkedList> sorted = new TreeMap<>();
					sorted.putAll(mydate);
					for(String dkey : sorted.keySet()) {
						if(dkey.equals(date)) {
							CustomLinkedList value3 = sorted.get(date.trim());
							ArrayList<String> arrList = value3.toArrayList();
							Collections.sort(arrList);
							int de = 0;
							for(String v : arrList) {
								
								if(v.charAt(0) == '[') {
									v = v.substring(34, v.length());
								}
								
								Parserable parser2 = null;
								if(Utils.isMacExportedMessage(v)) {
									parser2 = new ParserForMac();
									mow = "m";
								}
									
								
								else if (Utils.isWindowsExportedMessage(v)) {
									parser2 = new ParserForWindows();
									mow = "w";
								}
								
								if(parser2 != null) {
									Message newMessage = parser2.parse(v);
									
									String kw = newMessage.writergetter().trim();
									
									if(kw.equals(writer)) {

										if(parser2 != null) {
											if (de != 0) {
												filewriter.append("\n");
												filewriter.append("\n");
												
											}
											filewriter.append("Parsing Message " + ++de);
											
											
											Message neww = parser2.parse(v);
											
											if(mow == "w") {
												filewriter.append("\n" + "When: " + date + " " + neww.dategetter());
												filewriter.append("\n" + "Who: " + neww.writergetter());
												filewriter.append("\n" + "What: " + neww.messagegetter());
											}
											if(mow == "m") {
												filewriter.append("\n" + "When: " + neww.dategetter());
												filewriter.append("\n" + "Who: " + neww.writergetter());
												filewriter.append("\n" + "What: " + neww.messagegetter());
											}
										}
									}
									
								}
								
							}
							System.out.println("The number of messages filtered by the date, " + date +", and the writer, " + writer +": " + de);
						}
					}	
					
					
				}
				
				System.out.println("\nAll the parsed messages are saved in " + output);
				
				
				filewriter.close();
				
			} 
			/**
			 * catch block for DataFileNotFoundException
			 */
			catch(DataFileNotFoundException e) {
				System.out.println("DataFileNotFoundException: " + input);
				
				
			}
			/**
			 * catch block for AlreadyExistingOutputFileException
			 */
			catch(AlreadyExistingOutputFileException e) {
				System.out.println("AlreadyExistingOutputFileException: " + output);
				/*int c = 0;
				for(int k = 0; k < output.length(); k++) {
					if(output.charAt(k) == '/') {
						c++;
					}
				}
				if(c > 0) {
					String[] a = output.split("/");
					
					String ot = "/" + a[2] + "/" + a[3];
					
					System.out.println("AlreadyExistingOutputFileException: " + ot);
				}
				else {
					System.out.println("AlreadyExistingOutputFileException: " + output);
				}*/
			}
			
			
		}

		
	}
	/**
	 * method for identify options
	 * @param options CLI options user want to use
	 * @param args options input
	 * @return True or False
	 */
	private boolean parseOptions(Options options, String[] args) {
		CommandLineParser parser = new DefaultParser();

		try {

			CommandLine cmd = parser.parse(options, args);

			help = cmd.hasOption("h");
			date = cmd.getOptionValue("d");
			input = cmd.getOptionValue("i");
			output = cmd.getOptionValue("o");
			writer = cmd.getOptionValue("w");
			startdate = cmd.getOptionValue("sd");
			enddate = cmd.getOptionValue("ed");
	

		} catch (Exception e) {
			printHelp(options);
			return false;
		}

		return true;
	}

	// Definition Stage
	/**
	 * CLI options definition blocks
	 * @return option return choose option
	 */
	private Options createOptions() {
		Options options = new Options();

		/*options.addOption(Option.builder("d").longOpt("date")
			.desc("Apply a filter by a specific date")
			.hasArg()
			.argName("Date filter")
			//.required()
			.build());*/

		// add options by using OptionBuilder
		options.addOption(Option.builder("h").longOpt("help")
				.desc("Show a Help page")
				.build());

		options.addOption(Option.builder("i").longOpt("input")
				.desc("Set a directory path or input data file path for chat messages")
				.hasArg()
				.argName("Input dir/file path")
				.required()
				.build());

		options.addOption(Option.builder("o").longOpt("output")
				.desc("Set an output data file path for chat messages")
				.hasArg()
				.argName("Output file path")
				.required()
				.build());

		options.addOption(Option.builder("sd").longOpt("startdate")
				.desc("Apply a filter by a specific start date")
				.hasArg()
				.argName("Start date filter")
				//.required()
				.build());

		options.addOption(Option.builder("ed").longOpt("enddate")
				.desc("Apply a filter by a specific end date")
				.hasArg()
				.argName("End date filter")
				//.required()
				.build());

		options.addOption(Option.builder("w").longOpt("writer")
				.desc("Apply a filter by a specific writer")
				.hasArg()
				.argName("Writer filter")
				//.required()
				.build());

		return options;
	}
	
	/**
	 * method for printing help page
	 * @param options choose options
	 */
	private void printHelp(Options options) {
		// automatically generate the help statement
		HelpFormatter formatter = new HelpFormatter();
		String header = "Chatparser program";
		String footer ="\nThis chatparser is implemented in 2021-1 Java class.";
		formatter.printHelp("chatparser", header, options, footer, true);
	}	
	
	
	/**
	 * class for StringComparator
	 * @author jeong-yiju
	 *
	 */
	class StringComparator implements Comparator<String> {
	    /**
	     * compare string
	     */
		@Override
		public int compare(String a, String b) {
	        return a.compareToIgnoreCase(b);
	    }
	}
}
