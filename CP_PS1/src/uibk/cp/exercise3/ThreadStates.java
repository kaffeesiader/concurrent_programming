package uibk.cp.exercise3;

import java.lang.Thread.State;
import java.util.Random;
import java.util.Set;

import uibk.cp.exercise2.ComputePi;

/**
 * Test the various states of the threads.
 * Creates a number of threads and brings them into different states.
 * The thread information is printed each second.
 */
public class ThreadStates implements Runnable {
	
	private Random rand = new Random(System.currentTimeMillis());
	private boolean waiting = false;
	
	/**
	 * Either computes PI, sleeps or waits, with a certain probability.
	 */
	private synchronized void doSomething() throws InterruptedException {
		
		int rnd = rand.nextInt(3);

		switch (rnd) {
		case 0:
			// do something expensive
			ComputePi cp = new ComputePi(4, 6);
			cp.compute();
			
			notifyAll();
			break;
		case 1:
			// sleep a random amount of time
			Thread.sleep(1000 * rand.nextInt(5));
			notifyAll();
			break;
		default:
			// check if a thread is already waiting
			if(waiting) {
				notifyAll();
				waiting = false;
			} else {
				// or just wait
				waiting = true;
				wait();
			}
		}
	}
	
	@Override
	public void run() {
		try {
			while(true) {
				doSomething();
			}
			
		} catch (InterruptedException e) {
			System.out.println("Thread[" + Thread.currentThread().getName() + "] interrupted.");
		}
		
	}	
	
	public static void main(String[] args) throws InterruptedException {
		// one thread in the NEW state
		Thread newThread = new Thread("New");
		
		// do something that immediately completes 
		Runnable r = new Runnable() {
			@Override
			public void run() {
				System.out.format("Thread[%s]: I did something...%n", Thread.currentThread().getName());
			}
		};
		// one thread in the terminated state...
		Thread termThread = new Thread(r, "Terminated");
		termThread.start();
		
		ThreadStates ts = new ThreadStates();
		// 4 worker threads
		for(int i = 0; i < 4; ++i) {
			Thread worker = new Thread(ts, "Worker" + i);
			worker.start();
		}
		// check and print thread information
		while(true) {
			Set<Thread> threads = Thread.getAllStackTraces().keySet();
			for (Thread thread : threads) {
				String name = thread.getName();
				State state = thread.getState();
				int priority = thread.getPriority();
				
				System.out.format("%-30s %-20s Priority: %d%n", "Thread[" + name + "]", "State: " + state, priority);
			}
			
			System.out.format("%-30s %-20s Priority: %d%n", "Thread[New]", "State: " + newThread.getState(), newThread.getPriority());
			System.out.format("%-30s %-20s Priority: %d%n", "Thread[Terminated]", "State: " + termThread.getState(), termThread.getPriority());
			
			System.out.println();
			Thread.sleep(1000);
		}
	}

}
