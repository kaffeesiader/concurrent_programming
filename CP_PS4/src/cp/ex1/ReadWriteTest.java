package cp.ex1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ReadWriteTest {
	
	public static int LENGTH = 10;
	private static Random rand = new Random();
	
	private ReadWrite lock;
	private int data[];
	private boolean running;
	private List<Thread> workers;
	
	public ReadWriteTest(int readers, int writers) {
		lock = new ReadWriteMonitor();
		data = new int[LENGTH];
		running = false;
		workers = new ArrayList<>();
		
		for (int i = 0; i < readers; i++)
			workers.add(new Reader());
		
		for (int i = 0; i < writers; i++) 
			workers.add(new Writer());
	}
	
	public void start() {
		if(!running) {
			running = true;
			for (Thread worker : workers) {
				worker.start();
			}
		}
	}
	
	public void stop() {
		if(running) 
			running = false;
	}
	
	private class Reader extends Thread {
		@Override
		public void run() {
			while(running) {
				
				int index = rand.nextInt(LENGTH);
				// read operation
				lock.acquireRead();
				try {
					int val = data[index];
					System.out.format("[Reader-%d] read value '%d' from index '%d'%n", this.getId(), val, index);
					sleep(rand.nextInt(500));	
				} catch (InterruptedException handled) {
					break;
				// ensure to always release lock
				} finally {
					lock.releaseRead();
				}
				
				// sleep some random time before next iteration
				try { sleep(rand.nextInt(2000)); }
				catch (InterruptedException handled) {}
			}
		}
	}
	
	private class Writer extends Thread {
		@Override
		public void run() {
			while(running) {
				int val = rand.nextInt(100);
				int index = rand.nextInt(LENGTH);
				// write operation
				lock.acquireWrite();
				
				try {
					data[index] = val;
					System.out.format("[Writer-%d] wrote value '%d' to index '%d'%n", this.getId(), val, index);
					sleep(rand.nextInt(500));
				} catch (InterruptedException handled) {
					break;
				// ensure to always release lock
				} finally {
					lock.releaseWrite();
				}
				// sleep some random time before next iteration
				try { sleep(rand.nextInt(2000)); }
				catch (InterruptedException handled) {}
			}
		}
	}
	
	public static void main(String[] args) {
		new ReadWriteTest(20, 2).start();
	}

}
