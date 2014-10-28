package cp.ps2.ex2;

import java.util.Random;

public class Producer extends Thread {

	private Buffer buffer;
	private int index;
	
	public Producer(Buffer buffer, int index) {
		this.buffer = buffer;
		this.index = index;
	}
	
	@Override
	public void run() {
		Random rnd = new Random();
		int value;
		while(true) {
			value = rnd.nextInt(20);
			buffer.put(index, value);
			System.out.println(this + " put value '" + value + "' to buffer.");
			if(value == 0) {
				break;
			} 
			// compute sleep duration
			long duration = rnd.nextInt(4) * 1000;
			try {
				sleep(duration);
			} catch (InterruptedException e) {
				System.out.println(this + " interrupted.");
				break;
			}
			
		}
		
		System.out.println(this + " shut down.");
	}
	
	@Override
	public String toString() {
		return "[Producer" + index + "]";
	}
}
