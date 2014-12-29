package cp.ex2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Semaphore {
	
	private final int capacity;
	private int state;
	private Lock lock;
	private Condition cond;
	
	public Semaphore(int permits) {
		if(permits <= 0) 
			throw new IllegalArgumentException("Capacity needs to be greater than zero!");
		
		this.capacity = permits;
		state = 0;
		lock = new ReentrantLock();
		cond = lock.newCondition();
	}
	
	public int getCapacity() {
		return capacity;
	}
	
	public int getState() {
		return state;
	}
	
	public void p(int x) {
		try {
			System.out.format("[Thread%d] aquires %d permits.%n", Thread.currentThread().getId(), x);
			lock.lock();
			
			while((state + x) > capacity) {
				System.out.format("[Thread%d] has to wait.%n", Thread.currentThread().getId());
				cond.await();
			}
			state += x;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
	
	public void v(int x) {
		if((state - x) < 0)
			throw new IllegalStateException("Cannot release more permits than previously locked!");
		
		try {
			lock.lock();
			state -= x;
			System.out.format("[Thread%d] releases %d permits.%n", Thread.currentThread().getId(), x);
			// notify waiting threads
			cond.signalAll();	
		} finally {
			lock.unlock();
		}
	}

	public static void main(String[] args) throws InterruptedException {
		System.out.println("Testing with initial capacity of 1.");
		final Semaphore sem = new Semaphore(2);
		sem.p(1);
		
		Runnable r = new Runnable() {
			public void run() {
				sem.p(2);
				System.out.format("[Thread%d] does it's stuff in critical section...%n", Thread.currentThread().getId());
				try { Thread.sleep(1000); } catch (InterruptedException e) {}
				sem.v(2);
			}
		};
		
		Thread t = 	new Thread(r);
		t.start();
		
		Thread.sleep(500);
		sem.v(1);
		t.join();
		System.out.println("Semaphore state: " + sem.getState());
		
		System.out.println("Test run completed!");
	}
}
