package cp.ex3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

interface PrimeListener {	
	void handlePrime(int prime);
}

/**
 * The PrimeChecker checks all within the provided message queue for beeing primes or not.
 * It also notifies all registered PrimeListener instances if a new prime number was found.
 * The shutdown of the PrimeChecker is initiated, by placing a MainTest.POISON_PILL into the
 * message queue.
 */
public class PrimeChecker extends Thread {
	
	private static Random rand = new Random();
	
	private BlockingQueue<Integer> msgQueue;
	private List<PrimeListener> listeners;
	
	/**
	 * Creates a new instance of PrimeChecker, using the provided message queue
	 * 
	 * @param queue The message queue to use.
	 */
	public PrimeChecker(BlockingQueue<Integer> queue) {
		msgQueue = queue;
		listeners = new ArrayList<>();
	}
	
	@Override
	public void run() {
		
		while(true) {
			int value;
			
			try {
				value = msgQueue.take();
			
				if(value == MainTest.POISON_PILL) {
					System.out.println("[PrimeChecker] received poison pill.");
					break;
				}
				
				System.out.println("[PrimeChecker] Checking value '" + value + "'.");
				
				if(isPrime(value)) {
					System.out.println("[PrimeChecker] Discovered prime '" + value + "'.");
					notifyListeners(value);
				}
				
				sleep(rand.nextInt(2000));
				
			} catch (InterruptedException e) {
				break;
			}
		}
		
		listeners.clear();
		System.out.println("[PrimeChecker] Shutdown completed.");
	}
	
	/**
	 * Register a PrimeListener instance. All registered listeners are notified
	 * when a new prime number was found in queue.
	 * 
	 * @param listener The listener instance to add.
	 */
	public synchronized void registerListener(PrimeListener listener) {
		listeners.add(listener);
	}
	
	private void notifyListeners(int prime) {
		for (PrimeListener listener : listeners) {
			listener.handlePrime(prime);
		}
	}
	
	private boolean isPrime(int num) {
        if (num % 2 == 0) 
        	return false;
        
        for (int i = 3; i * i <= num; i += 2)
            if (num % i == 0) 
            	return false;
        
        return true;
	}
}
