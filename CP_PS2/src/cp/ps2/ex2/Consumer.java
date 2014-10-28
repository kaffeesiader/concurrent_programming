package cp.ps2.ex2;

import java.util.Arrays;

public class Consumer extends Thread {

	private Buffer buffer;
	private int availableThreads;
	
	public Consumer(Buffer buffer, int threads) {
		super("[Consumer" + threads + "]");
		this.buffer = buffer;
		this.availableThreads = threads;
	}
	
	@Override
	public void run() {

		while(availableThreads > 0) {
			// retreave as much values from buffer as there are threads available...
			int values[] = buffer.get(availableThreads);
			// check if we retrieved any zero values...
			for (int i = 0; i < values.length; i++) {
				if(values[i] == 0) {
					availableThreads--;
				}
			}
			System.out.println(String.format(this + " received values %s from buffer.", Arrays.toString(values)));
		}
		
		System.out.println(this + " shut down!");
	}
	
	@Override
	public String toString() {
		return getName();
	}
}
