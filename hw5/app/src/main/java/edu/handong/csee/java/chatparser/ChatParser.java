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
import java.util.HashMap;

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
	
	private boolean help;
	private String date;
	private String input;
	private String output;
	private String writer;
	private String startdate;
	private String enddate;
	
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
		
		/**
		 * Mapping key: writer & value: message
		 */
		HashMap<String, ArrayList<String>> mywriter = new HashMap<String, ArrayList<String>>();
		/**
		 * Mapping key: date & value: message
		 */
		HashMap<String, ArrayList<String>> mydate = new HashMap<String, ArrayList<String>>();		
		
		
		
		/**
		 * for CLI 
		 */
		Options options = createOptions();
		
		if(parseOptions(options, args)){
			if (help){
				printHelp(options);
				return;
			}
			
			String ChatMessages;
			
			/**
			 * try block
			 */
			try {
				File f = new File(output); //result
				File g = new File(input); //data
				
				if(g.exists() == false) {
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
				BufferedWriter filewriter = new BufferedWriter(new FileWriter(output));
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
							ArrayList<String> messagesForWriter = new ArrayList<String>();
							messagesForWriter.add(currentChatMessage);
							mywriter.put(newMes.writergetter(),  messagesForWriter);
							
						}
						else {
							
							ArrayList<String> existingArrayList = mywriter.get(newMes.writergetter());
							existingArrayList.add(currentChatMessage);
							mywriter.put(newMes.writergetter(),  existingArrayList);
						}
						
						if(!mydate.containsKey(dat.trim())) {
							//System.out.println("NO");
							
							ArrayList<String> messagesForDate = new ArrayList<String>();
							String[] curr = currentChatMessage.split(",");
							String dte = curr[0];
							dte = dte.substring(0, dte.length() - 3);
							currentChatMessage = dte + "," + curr[1] + "," + newMes.messagegetter();
							messagesForDate.add(currentChatMessage);
							mydate.put(dat.trim(),  messagesForDate);
							
						}
						else {
							
							ArrayList<String> existingArrayList1 = mydate.get(dat.trim());
							String[] curr = currentChatMessage.split(",");
							String dte = curr[0];
							dte = dte.substring(0, dte.length() - 3);
							currentChatMessage = dte + "," + curr[1] + "," + newMes.messagegetter();
							
							
							//System.out.println(currentChatMessage);
							existingArrayList1.add(currentChatMessage);
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
						
						if(Utils.isMacExportedMessage(currentChatMessage))	{
							if(date != null) {
								dat = date.trim();
							}
							mo = "m";
							parser = new ParserForMac();
						}
						else if (Utils.isWindowsExportedMessage(currentChatMessage)) {
							dat = ymd;
							parser = new ParserForWindows();
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
								
								//System.out.println(currentChatMessage);
								
								ArrayList<String> existingArrayList = mywriter.get(wri.trim());
								String nt = existingArrayList.get((existingArrayList.size()) - 1);
								existingArrayList.remove(nt);
								//System.out.println(nt);
								nt = nt + "\n" + currentChatMessage;
								//System.out.println(nt);
								
								existingArrayList.add(nt);
								
								
								mywriter.put(wri.trim(),  existingArrayList);
								
								ArrayList<String> existingArrayList1 = mydate.get(dat.trim());
								String nt1 = existingArrayList1.get((existingArrayList1.size()) - 1);
								existingArrayList1.remove(nt1);
								//System.out.println(nt1);
								nt1 = nt1 + "\n" + currentChatMessage;
								//System.out.println(nt1);
								
								existingArrayList1.add(nt1);
								mydate.put(dat.trim(),  existingArrayList1);
								
								
							}
							
						}
							
						if(parser != null) {
							Message newMes = parser.parse(currentChatMessage);
							wri = newMes.writergetter().trim();
							if(mo == "m") {
								dat = newMes.dategetter().trim().substring(0, 10);
							}
							
							
							//System.out.println(dat);
							
							if(!mywriter.containsKey(newMes.writergetter())) {
								ArrayList<String> messagesForWriter = new ArrayList<String>();
								messagesForWriter.add(currentChatMessage);
								mywriter.put(newMes.writergetter(),  messagesForWriter);
								
							}
							else {
								//System.out.println("YES");
								ArrayList<String> existingArrayList = mywriter.get(newMes.writergetter());
								existingArrayList.add(currentChatMessage);
								mywriter.put(newMes.writergetter(),  existingArrayList);
							}
							
							if(!mydate.containsKey(dat.trim())) {
								
								
								ArrayList<String> messagesForDate = new ArrayList<String>();
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
								messagesForDate.add(currentChatMessage);
								mydate.put(dat.trim(),  messagesForDate);
							}
							else {
								//System.out.println("NO");
								ArrayList<String> existingArrayList1 = mydate.get(dat.trim());
								
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
								existingArrayList1.add(currentChatMessage);
								mydate.put(dat.trim(),  existingArrayList1);
							}
							
							
							
						}
						
						
						else if(currentChatMessage.length() == 0 && index > 4 && parser == null) {
							
							ArrayList<String> existingArrayList = mywriter.get(wri);
							existingArrayList.add("\n");
							mywriter.put(wri,  existingArrayList);
							
							ArrayList<String> existingArrayList1 = mydate.get(dat.trim());
							existingArrayList1.add("\n");
							mydate.put(dat.trim(),  existingArrayList1);
						
							
						}
						
										
						index++;
					}
				}
				
				if(startdate != null && enddate == null) {
					String mow = "";
					if(!mydate.containsKey(startdate.trim())) {
						System.out.println("The number of messages filtered by the start date, " + startdate +": 0");
						System.out.println("\nAll the parsed messages are saved in " + output);
						//System.out.println("YES");
						return;
					}
					else {
						int de = 0;
						for(String dkey : mydate.keySet()) {
							//System.out.println(dkey);
							if(dkey.equals(startdate.trim()) || de != 0) {
								
								ArrayList<String> value2 = mydate.get(dkey);
								Collections.sort(value2);
								
								for(String v : value2) {
									Parserable parser2 = null;
									
									if(v.charAt(0) == '[') {
										v = v.substring(17, v.length());
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
				
				if(startdate == null && enddate != null) {
					String mow = "";
					if(!mydate.containsKey(enddate.trim())) {
						System.out.println("The number of messages filtered by the end date, " + enddate +": 0");
						System.out.println("\nAll the parsed messages are saved in " + output);
						//System.out.println("YES");
						return;
					}
					else {
						int de = 0;
						for(String dkey : mydate.keySet()) {
							//System.out.println(dkey);
							String[] sped = enddate.split("-");
							String[] spedk = dkey.split("-");
							String spedi = sped[1] + sped[2];
							String spedki = spedk[1] + spedk[2];
							int spedit =  Integer.parseInt(spedi);
							int spedkit =  Integer.parseInt(spedki);
							
							//System.out.println(spedit);
							//System.out.println(spedkit);
							
							if (spedit >= spedkit ) {
								
								ArrayList<String> value2 = mydate.get(dkey);
								Collections.sort(value2);
								
								for(String v : value2) {
									Parserable parser2 = null;
									
									if(v.charAt(0) == '[') {
										v = v.substring(17, v.length());
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
						
						
						System.out.println("The number of messages filtered by the end date, " + enddate +": " + de);
					}
					
	
				}
				
				if(startdate != null && enddate != null) {
					String mow = "";
					if(!mydate.containsKey(enddate.trim())) {
						System.out.println("The number of messages filtered by the start date, " + startdate +", and the end date, " + enddate +": 0");
						System.out.println("\nAll the parsed messages are saved in " + output);
						//System.out.println("YES");
						return;
					}
					else {
						int de = 0;
						for(String dkey : mydate.keySet()) {
							
							String[] sped = enddate.split("-");
							String[] spedk = dkey.split("-");
							String spedi = sped[1] + sped[2];
							String spedki = spedk[1] + spedk[2];
							int spedit =  Integer.parseInt(spedi);
							int spedkit =  Integer.parseInt(spedki);
							
							//System.out.println(spedit);
							//System.out.println(spedkit);
							if (dkey.equals(startdate.trim()) && de == 0) {
								
								ArrayList<String> value2 = mydate.get(dkey);
								Collections.sort(value2);
								
								for(String v : value2) {
									Parserable parser2 = null;
									
									if(v.charAt(0) == '[') {
										v = v.substring(17, v.length());
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
								
								ArrayList<String> value2 = mydate.get(dkey);
								Collections.sort(value2);
								
								for(String v : value2) {
									Parserable parser2 = null;
									
									if(v.charAt(0) == '[') {
										v = v.substring(17, v.length());
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
				
				
				
				
				if(writer == null && date == null && startdate == null && enddate == null ) {
					
					int de = 0;
					String mow = "";
					String sd = "";
					System.out.println("YES");
					
					
					if(mydate.size() == 0) {
						System.out.println("The number of all messages processing: 0");
						System.out.println("\nAll the parsed messages are saved in " + output);
						return;
					}
					for(String dt : mydate.keySet()) {
						ArrayList<String> value = mydate.get(dt);
						
						
						
						Collections.sort(value);
						//System.out.println(dt);
						//System.out.println(value);
						for(String v : value) {
							
							for(String sdate : mydate.keySet()) {
								if(mydate.get(sdate).contains(v)) {
									sd = sdate;
								}
							}
							
							if(v.charAt(0) == '[') {
								v = v.substring(17, v.length());
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
				
				
				
				if(writer != null && date == null) {
					
					int de = 0;
					String mow = "";
					String sd = "";
					
					
					if(!mywriter.containsKey(writer)) {
						System.out.println("The number of messages filtered by the writer, " + writer +": 0");
						System.out.println("\nAll the parsed messages are saved in " + output);
						return;
					}
					ArrayList<String> value = mywriter.get(writer);
					Collections.sort(value);
					for(String v : value) {
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
							
							for(String sdate : mydate.keySet()) {
								if(mydate.get(sdate).contains(v)) {
									sd = sdate;
								}
							}
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
				
				if(writer == null && date != null) {
					String mow = "";
					if(!mydate.containsKey(date.trim())) {
						System.out.println("The number of messages filtered by the date, " + date +": 0");
						System.out.println("\nAll the parsed messages are saved in " + output);
						//System.out.println("YES");
						return;
					}
					else {
						int de = 0;
						ArrayList<String> value2 = mydate.get(date.trim());
						Collections.sort(value2);
						
						for(String v : value2) {
							Parserable parser2 = null;
							
							if(v.charAt(0) == '[') {
								v = v.substring(17, v.length());
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
				
				
				
				
				
				
				
				if(date != null && writer != null) {
					
					if(!mydate.containsKey(date.trim()) && !mywriter.containsKey(writer)) {
						System.out.println("The number of messages filtered by the date, " + date +", and the writer, " + writer +": 0");
						System.out.println("\nAll the parsed messages are saved in " + output);
						//System.out.println("YES");
						return;
					}
					
					String mow = "";
					for(String dkey : mydate.keySet()) {
						if(dkey.equals(date)) {
							ArrayList<String> value3 = mydate.get(date.trim());
							int de = 0;
							for(String v : value3) {
								
								if(v.charAt(0) == '[') {
									v = v.substring(17, v.length());
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
				
				inputStream.close();
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
				.desc("Set an input data file path for chat messages")
				.hasArg()
				.argName("Input file path")
				.required()
				.build());
		
		options.addOption(Option.builder("o").longOpt("output")
				.desc("Set an output data file path for chat messages")
				.hasArg()
				.argName("Output file path")
				.required()
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
