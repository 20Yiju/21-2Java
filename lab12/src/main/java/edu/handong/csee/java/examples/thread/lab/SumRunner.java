package edu.handong.csee.java.examples.thread.lab;

public class SumRunner implements Runnable{

	long from, to, totalSum;

	@Override
	public void run() {
		totalSum = getTotalSumFromTo();
		System.out.println("Computed from " + from + " to " + to);
	}

	public SumRunner(long from, long to) {
		this.from = from;
		this.to= to;
	}

	public long getTotalSumFromTo() {
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		long total = 0;
		for(long i=from;i<=to;i++)
			total+=i;

		return total;
	}
}
