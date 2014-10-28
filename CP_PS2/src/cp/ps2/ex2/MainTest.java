package cp.ps2.ex2;

import java.util.ArrayList;
import java.util.List;

public class MainTest {
	
	public static void main(String[] args) throws InterruptedException {
		final int bufSize = 4;
		List<Producer> producers = new ArrayList<>();
		Buffer buffer = new Buffer();
		
		System.out.println("Starting Producers and Cosumer.");
		
		for(int i = 0; i < bufSize; ++i) {
			Producer prod = new Producer(buffer, i);
			producers.add(prod);
			prod.start();
		}
		
		Consumer cons = new Consumer(buffer, producers.size());
		cons.start();
		
		cons.join();
		
		System.out.println("Producer/Consumer test finished!");
	}

}
