package edu.handong.csee.java.chatparser.ds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * 
 * @author jeong-yiju
 * class for CustomLinkedListv
 * @param <D> LinkedList
 */
public class CustomLinkedList<D> {
	private ListNode head;
	private ListNode tail;
	
	/**
	 * set head
	 */
	public CustomLinkedList() {
		head = null;
	}
	/**
	 * print list elements
	 */
	public void showList() {
		ListNode position = head;
		while(position != null) {
			System.out.println(position.data);
			position = position.link;
		}
	}
	
	/**
	 * show length of list
	 * @return length of list
	 */
	public int length() {
		int count = 0;
		ListNode position = head;
		while(position != null) {
			count++;
			position = position.link;
		}
		return count;
	}

	/**
	 * add elements to head
	 * @param addData Data to save
	 */
	public void addANodeToStart(D addData) {
		if(length()<2)
			tail = head;
		head = new ListNode(addData,head);
	}
	
	/**
	 * remove elements to head
	 */
	public void deleteHeadNode() {
		//System.out.println(head.getData());
		if(head != null) {
			head = head.link;
			if(head==null)
				tail=null;
		}	
		else {
			System.out.println("Deleting from an empty list.");
			System.exit(0);
		}
	}
	/**
	 * add elements to tail
	 * @param addData Data to save
	 */
	public void addANodeToTail(D addData) {
		if(length()==0) {
			tail=new ListNode(addData,tail);
			head=tail;
		}else {
			tail.link = new ListNode(addData,tail.link);
		}
	}
	/**
	 * remove elements to tail
	 */
	public void deleteTailNode() {
		// Get previous node
		ListNode previous = getPreviousNode();
		
		// assing previous node to tail and 
		// make the previous node link to null.
		tail = previous;
		previous.link = null;
		
	}
	
	/**
	 * get PreviousNode
	 * @return PreviousNode
	 */
	private ListNode getPreviousNode() {
		ListNode current = head;
		ListNode previous = null;

		while(true) {
			if(current == tail)
				return previous;
			
			previous = current; // set previons with current at it will be changed into the next node.
			current = current.link; // move to next
		}
	}

	/**
	 * check the data is on list or not
	 * @param target target to find on list
	 * @return true or false
	 */
	public boolean onList(D target) {
		return find(target) != null;
	}
	
	/**
	 * find the data is on list or not
	 * @param target to find on list
	 * @return the target node
	 */
	private ListNode find(D target) {
		boolean found = false;
		ListNode position = head;
		while((position != null) && !found) {
			D dataAtPosition = position.data;
			if(dataAtPosition.equals(target))
				found = true;
			else
				position = position.link;
		}
		return position;
	}


	/**
	 * change CustomLinkedList to ArrayList
	 * @return ArrayList
	 */
	public ArrayList<D> toArrayList() {
		ArrayList<D> list = new ArrayList<D>(length());
		ListNode position = head;
		while(position != null) {
			list.add(position.data);
			position = position.link;
		}
		return list;
	}
	
	/**
	 * class for Node
	 * @author jeong-yiju
	 *
	 */
	private class ListNode{
		/**
		 * data
		 */
		private D data;
		
		/**
		 * method to get data
		 * @return data
		 */
		public D getData() {
			return data;
		}

		/**
		 * method to get Link of Node
		 * @return Link
		 */
		public ListNode getLink() {
			return link;
		}

		/**
		 * link
		 */
		private ListNode link;
		
		/**
		 * set the Node
		 * @param newData data in Node
		 * @param linkedNode link in Node
		 */
		public ListNode(D newData, ListNode linkedNode) {
			data = newData;
			link = linkedNode;
		}
	}
	

	/**
	 * method for Sorting
	 * @param comparator Comparator to sort
	 * @return sorting list
	 */
	public ArrayList<D> sort(Comparator<D> comparator) {
		
		ArrayList<D> list = (ArrayList<D>) toArrayList();
		Collections.sort(list,comparator);
		
		return list;
	}


}
