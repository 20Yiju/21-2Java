package edu.handong.csee.java.chatparser;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

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
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.util.Arrays;

import edu.handong.csee.java.chatparser.datamodel.Message;
import edu.handong.csee.java.chatparser.ds.CustomLinkedList;
import edu.handong.csee.java.chatparser.parser.ParserForMac;
import edu.handong.csee.java.chatparser.parser.ParserForWindows;
import edu.handong.csee.java.chatparser.parser.Parserable;
import edu.handong.csee.java.chatparser.parser.exception.AlreadyExistingOutputFileException;
import edu.handong.csee.java.chatparser.parser.exception.DataFileNotFoundException;

/**
 * 
 * @author jeong-yiju
 * class for directory file 
 */
@SuppressWarnings("unchecked")
public class DirFileReader implements Runnable {
	
	/**
	 * map for writer
	 */
	HashMap<String, CustomLinkedList> myw = new HashMap<String, CustomLinkedList>();
	
	/**
	 * map for date
	 */
	HashMap<String, CustomLinkedList> myd = new HashMap<String, CustomLinkedList>();	
	ChatParser ch1 = new ChatParser();
	 
	private String file;
	 ArrayList<String> fNl = new ArrayList<String>();
	 
	 /**
	  * save filename
	  * @param file filename
	  */
	 public DirFileReader (String file) {
	    this.file = file;
	    fNl.add(ch1.input + file);
	  }

	/**
	 * run 
	 */
	@Override
	public void run() {
		
		
		
		// TODO Auto-generated method stub
		for(String fileN : fNl) {
			String input = fileN.trim();
			//String output = ch1.out;
			
			try {
				
				File g = new File(input); //data
				//System.out.println(input);
				
				Reader inf = new FileReader(input);
				BufferedReader inputStream= new BufferedReader(inf);
				//BufferedWriter filewriter = new BufferedWriter(new FileWriter(output));
				Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(inf);
				
				
				if(fileN.contains("csv")) {
					
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
						
						if(!myw.containsKey(newMes.writergetter())) {
							//System.out.println("YES");
							CustomLinkedList messagesForWriter = new CustomLinkedList();
							messagesForWriter.addANodeToStart(currentChatMessage);
							myw.put(newMes.writergetter(),  messagesForWriter);
							
						}
						else {
							
							CustomLinkedList existingArrayList = myw.get(newMes.writergetter());
							existingArrayList.addANodeToStart(currentChatMessage);
							myw.put(newMes.writergetter(),  existingArrayList);
						}
						
						if(!myd.containsKey(dat.trim())) {
							//System.out.println("NO");
							
							CustomLinkedList messagesForDate = new CustomLinkedList();
							String[] curr = currentChatMessage.split(",");
							String dte = curr[0];
							dte = dte.substring(0, dte.length() - 3);
							currentChatMessage = dte + "," + curr[1] + "," + newMes.messagegetter();
							messagesForDate.addANodeToStart(currentChatMessage);
							myd.put(dat.trim(),  messagesForDate);
							
						}
						else {
							
							CustomLinkedList existingArrayList1 = myd.get(dat.trim());
							String[] curr = currentChatMessage.split(",");
							String dte = curr[0];
							dte = dte.substring(0, dte.length() - 3);
							currentChatMessage = dte + "," + curr[1] + "," + newMes.messagegetter();
							
							
							//System.out.println(currentChatMessage);
							existingArrayList1.addANodeToStart(currentChatMessage);
							myd.put(dat.trim(),  existingArrayList1);
						}
						
					}
					
				}
				
				
				
				else {
					String ChatMessages = "";
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
							
							if(!myw.containsKey(newMes.writergetter())) {
								CustomLinkedList messagesForWriter = new CustomLinkedList();
								
								String[] cur = currentChatMessage.split("]");
								String ch = "[" + newMes.dategetter();
								//currentChatMessage = currentChatMessage.replace(cur[1], ch);
								currentChatMessage = "[" + dat + newMes.dategetter() + currentChatMessage;
								
								messagesForWriter.addANodeToStart(currentChatMessage.trim());
								
								myw.put(newMes.writergetter(),  messagesForWriter);
								
							}
							else {
								//System.out.println("YES");
								
								CustomLinkedList existingArrayList = myw.get(newMes.writergetter());
								String[] cur = currentChatMessage.split("]");
								String ch = "[" + newMes.dategetter();
								//currentChatMessage = currentChatMessage.replace(cur[1], ch);
								currentChatMessage = "[" + dat + newMes.dategetter() + currentChatMessage;
								
								existingArrayList.addANodeToStart(currentChatMessage.trim());
								myw.put(newMes.writergetter(),  existingArrayList);
							}
							
							if(!myd.containsKey(dat.trim())) {
								
								
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
								myd.put(dat.trim(),  messagesForDate);
							}
							else {
								//System.out.println("NO");
								CustomLinkedList existingArrayList1 = myd.get(dat.trim());
								
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
								myd.put(dat.trim(),  existingArrayList1);
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
								
								CustomLinkedList existingArrayList = myw.get(wri.trim());
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
								myw.put(wri.trim(),  existingArrayList);
								
								CustomLinkedList existingArrayList1 = myd.get(dat.trim());
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
								myd.put(dat.trim(),  existingArrayList1);
								
								
							}
							
						}
						
						else if(currentChatMessage.length() == 0 && index > 4 && parser == null) {
							//System.out.println("YES");
							
							CustomLinkedList existingArrayList = myw.get(wri);
							
							ArrayList<String> arrList = existingArrayList.sort(new StringComparator());
							
							//existingArrayList.deleteHeadNode();
							
							String nt = arrList.get(arrList.size() - 1);
							nt = nt + "\n";
							
							//existingArrayList.addANodeToStart(nt);
							//existingArrayList1.addANodeToStart("\n");
							myw.put(wri,  existingArrayList);
							
							CustomLinkedList existingArrayList1 = myd.get(dat.trim());
							
							ArrayList<String> arrList1 = existingArrayList1.sort(new StringComparator());
							
							//existingArrayList1.deleteHeadNode();
							
							String nt1 = arrList1.get(arrList1.size() - 1);
							nt1 = nt1 + "\n";
							//existingArrayList1.addANodeToStart(nt1);
							existingArrayList1.addANodeToStart("\n");
							myd.put(dat.trim(),  existingArrayList1);
						
							
						}
						index++;
						
						
					}
					
					
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
			
			
		}
	}
	
	/**
	 * get hashmap data
	 * @return mywriter map
	 */
	public HashMap<String, CustomLinkedList> retunWriter(){
		
		return myw;
	}
	/**
	 * get hashmap data
	 * @return mydate map
	 */
	public HashMap<String, CustomLinkedList> retunDate(){
		
		return myd;
	}
	

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
