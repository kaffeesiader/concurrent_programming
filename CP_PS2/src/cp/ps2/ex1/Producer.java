package cp.ps2.ex1;

import java.util.Random;

public class Producer extends Thread {

	private Buffer buffer;
	
	public Producer(Buffer buffer) {
		this.buffer = buffer;
	}
	
	@Override
	public void run() {
		Random rnd = new Random();
		int value;
		while(true) {
			value = rnd.nextInt(20);
			buffer.put(value);
			System.out.println("[Producer] put value '" + value + "' to buffer.");
			if(value == 0) {
				break;
			} 
			// compute sleep duration
			long duration = rnd.nextInt(4) * 1000;
			try {
				sleep(duration);
			} catch (InterruptedException e) {
				System.out.println("[Producer] interrupted.");
				break;
			}
			
		}
		
		System.out.println("[Producer] shut down.");
	}
}
