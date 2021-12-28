package edu.handong.csee.java.examples.thread.synchronization;

public class Counter {
	private int c = 0;
    public void RunIncrease(String threadName) {
    	System.out.println(threadName + " is " + getValue() + " and is increasing.");
        increase();
        System.out.println(threadName + "'s value is now: " + getValue() );
    }
    
    public void increase() {
    	c++;
    }
    
    public void runDecrease(String threadName) {
    	System.out.println(threadName + " is "  + getValue() + " and is decreasing.");
        decrease();
        System.out.println(threadName + "'s value is now: " + getValue() );
    }
    
    public void decrease() {
    	c--;
    }
    
    public int getValue() {
        return c;
    }
}
