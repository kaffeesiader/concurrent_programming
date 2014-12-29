package cp.ex2;

import static org.junit.Assert.*;

import java.lang.Thread.State;

import org.junit.Test;

public class SemaphoreTests {

	@Test
	public void testSemaphoreWithSingleThread() throws InterruptedException {
		Semaphore semaphore = new Semaphore(1);
		assertEquals(semaphore.getState(), 0);
		assertEquals(semaphore.getCapacity(), 1);
		
		SemaphoreTestThread th = new SemaphoreTestThread(semaphore, 1);
		th.start();
		th.join();

		assertTrue(th.isDone());
	}
	
	@Test
	public void testSemaphoreBlocksIfNotEnoughCapacity() throws InterruptedException {
		Semaphore semaphore = new Semaphore(1);
		
		semaphore.p(1);
		assertEquals(semaphore.getState(), 1);
		
		SemaphoreTestThread th = new SemaphoreTestThread(semaphore, 1);
		th.start();
		
		Thread.sleep(50);
		assertEquals(th.getState(), State.WAITING);
		
		semaphore.v(1);
		Thread.sleep(50);
		
		assertTrue(th.isDone());
	}
	
	@Test
	public void testSemaphoreWithMultipleThreads() throws InterruptedException {
		Semaphore semaphore = new Semaphore(3);
		// aquire all permits bevore starting the test threads
		// and then release subsequently...
		semaphore.p(3);
		assertEquals(semaphore.getState(), 3);
		
		SemaphoreTestThread th1 = new SemaphoreTestThread(semaphore, 1);
		SemaphoreTestThread th2 = new SemaphoreTestThread(semaphore, 2);
		SemaphoreTestThread th3 = new SemaphoreTestThread(semaphore, 3);
		
		th1.start();
		th2.start();
		th3.start();
		
		Thread.sleep(50);
		assertEquals(th1.getState(), State.WAITING);
		assertEquals(th2.getState(), State.WAITING);
		assertEquals(th3.getState(), State.WAITING);
		
		semaphore.v(1);
		Thread.sleep(50);
		
		assertTrue(th1.isDone());
		assertFalse(th2.isDone());
		assertFalse(th3.isDone());
		assertEquals(semaphore.getState(), 2);
		
		semaphore.v(1);
		Thread.sleep(50);
		
		assertTrue(th2.isDone());
		assertFalse(th3.isDone());
		assertEquals(semaphore.getState(), 1);
		
		semaphore.v(1);
		Thread.sleep(50);
		
		assertTrue(th3.isDone());
		assertEquals(semaphore.getState(), 0);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testSemaphoreExceptionOnZeroCapacity() {
		Semaphore sem = new Semaphore(0);
		sem.getState();
		fail("Initializing Semaphore with zero capacity possible!");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testSemaphoreExceptionOnNegativeCapacity() {
		Semaphore sem = new Semaphore(-1);
		sem.getState();
		fail("Initializing Semaphore with negative capacity possible!");
	}
	
	@Test(expected = IllegalStateException.class)
	public void testSemaphoreExceptionOnReleasingPermits() {
		Semaphore sem = new Semaphore(1);
		sem.v(1); // this would lead to a negative state
		fail("Negative state possible!");
	}

	private class SemaphoreTestThread extends Thread {

		private int x;
		private Semaphore sem;
		private boolean done;

		public SemaphoreTestThread(Semaphore semaphore, int x) {
			this.sem = semaphore;
			this.x = x;
			this.done = false;
		}
		
		@Override
		public void run() {
			System.out.format("[Thread%d] started%n", this.getId());
			sem.p(x);
			System.out.format("[Thread%d] does it's stuff in critical section...%n", this.getId());
			sem.v(x);
			done = true;
			System.out.format("[Thread%d] finished%n", this.getId());
		}
		
		public boolean isDone() {
			return done;
		}

	}

}
