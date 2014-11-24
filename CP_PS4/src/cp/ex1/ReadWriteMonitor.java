package cp.ex1;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Monitor implementation for synchronizing Read-Write operations
 * Implements the ReadWrite Interface
 * 
 */
public class ReadWriteMonitor implements ReadWrite {
	
	private int readers;
	private boolean writer;
	private Lock lock;
	private Condition condition;
	
	private int rmax;
	private double ravg;
	
	public ReadWriteMonitor() {
		readers = 0;
		rmax = 0;
		ravg = 0;
		writer = false;
		lock = new ReentrantLock();
		condition = lock.newCondition();
	}

	@Override
	public void acquireRead() {
		lock.lock();
		try {
			// wait for the writer to finish
			while(writer) 
				condition.await();
			
			readers++;
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void releaseRead() {
		lock.lock();
		try {
			readers--;
			// notify waiting writers if there are no readers left readers
			if(readers == 0)
				condition.signalAll();
			
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void acquireWrite() {
		lock.lock();
		try {
			// wait for readers or writer to finish
			while(readers > 0 || writer) {
				// compute stats
				rmax = Math.max(readers, rmax);
				ravg = (ravg+readers)/2;
				System.out.format("[acquireWrite] r: %d, rmax: %d, ravg: %.2f%n", readers, rmax, ravg);
				condition.await();
			}
			writer = true;
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void releaseWrite() {
		lock.lock();
		try {
			writer = false;
			// notify all waiting threads
			condition.signalAll();
		} finally {
			lock.unlock();
		}
	}

}
