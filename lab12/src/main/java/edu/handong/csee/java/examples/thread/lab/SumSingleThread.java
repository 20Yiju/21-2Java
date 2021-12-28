package edu.handong.csee.java.examples.thread.lab;

public class SumSingleThread {
	
	long from, to;

	public SumSingleThread(long from, long to) {
		this.from = from;
		this.to= to;
	}

	private long getTotalSumFromTo() {
		long total = 0;
		for(long i=from;i<=to;i++)
			total+=i;
		
		return total;
	}

	public static void main(String[] args) {
		SumSingleThread runner = new SumSingleThread(1,10000000);
		System.out.println("Grand Total = " + runner.getTotalSumFromTo());
		
	}
}
