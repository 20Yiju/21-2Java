package edu.handong.csee.java.examples.thread;

public class HelloThread extends Thread{

	static int staticCount = 0;
	int count = 0;
	
	public static void main(String[] args) {
		int numThreads = 10;
		HelloThread[] theads = new HelloThread[numThreads];
		
		for(int i=0;i<numThreads;i++) {
			theads[i] = new HelloThread();
			theads[i].start();
		}
	}
	
	@Override
	public void run() {
		staticCount++;
		count++;
		System.out.println(staticCount + " Hello from a thread! " + (staticCount));	
		System.out.println(staticCount + " staticCount = " + staticCount);	
		System.out.println(staticCount + " count = " + count);	
	}
}
