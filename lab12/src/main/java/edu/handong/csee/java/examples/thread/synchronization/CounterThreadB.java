package edu.handong.csee.java.examples.thread.synchronization;

public class CounterThreadB implements Runnable{
	
	private Counter myCounter;
	private String threadID;

	@Override
	public void run() {
		myCounter.runDecrease(threadID);
	}

	public CounterThreadB(Counter counter, String threadID) {
		myCounter = counter;
		this.threadID = threadID;
	}
}
