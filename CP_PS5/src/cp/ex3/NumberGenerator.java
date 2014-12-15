package cp.ex3;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

/**
 * Implementation of a simple NumberGenerator Thread.
 * The generator creates random numbers in the range [1,31]
 * and places them into the given message queue.
 */
public class NumberGenerator extends Thread {

	private static final Random rand = new Random();
	private boolean running;
	private BlockingQueue<Integer> msgQueue;
	
	/**
	 * Creates a new instance of NumberGenerator
	 *
	 * @param queue The shared message queue to use
	 */
	public NumberGenerator(BlockingQueue<Integer> queue) {
		this.msgQueue = queue;
		running = false;
	}
	
	@Override
	public void run() {
		if(running)
			return;
		
		running = true;
		
		while(running) {
			int value = rand.nextInt(31) + 1;
			System.out.println("[NumberGenerator] Putting value '" + value + "' into queue.");
			
			try {
				msgQueue.put(value);
				sleep(rand.nextInt(2000));
			} catch (InterruptedException e) {
				shutdown();
			}
		}
		
		System.out.println("[NumberGenerator] Shutdown completed.");
	}
	
	/**
	 * This message initiates a graceful shutdown of the number generation thread.
	 * When asked for shutdown, the generator places a MainTest.POISON_PILL into the message queue,
	 * before exiting the main loop.
	 */
	public void shutdown() {
		if(!running)
			return;
		
		System.out.println("[NumberGenerator] Received shutdown request.");
		System.out.println("[NumberGenerator] Placing poison pill...");
		
		try { msgQueue.put(MainTest.POISON_PILL); }
		catch (InterruptedException handled) {};
		
		running = false;
	}
}
