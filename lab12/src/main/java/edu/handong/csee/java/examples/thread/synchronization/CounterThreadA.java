package edu.handong.csee.java.examples.thread.synchronization;

public class CounterThreadA implements Runnable{
	
	private Counter myCounter;
	private String threadID;

	@Override
	public void run() {
		
		myCounter.RunIncrease(threadID);
		
	}
	
	public CounterThreadA(Counter counter, String threadID) {
		myCounter = counter;
		this.threadID = threadID;
	}
}
