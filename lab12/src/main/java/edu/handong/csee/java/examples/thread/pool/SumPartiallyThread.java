package edu.handong.csee.java.examples.thread.pool;

public class SumPartiallyThread implements Runnable{

	private long from, to, totalSum;
	
	@Override
	public void run() {
		sumTotalSumFromTo();
		System.out.println("Computed from " + from + " to " + to);
	}

	public SumPartiallyThread(long from, long to) {
		this.from = from;
		this.to= to;
	}

	public void sumTotalSumFromTo() {
		long total = 0;
		for(long i=from;i<=to;i++)
			total+=i;
		
		totalSum = total;
		
	}
	
	public long getTotalSum() {
		return totalSum;
	}
}
