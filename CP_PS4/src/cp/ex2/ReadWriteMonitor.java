package cp.ex2;


/**
 * Monitor implementation for synchronizing Read-Write operations
 * Implements the ReadWrite Interface, using wait() and notify()
 * 
 */
public class ReadWriteMonitor implements ReadWrite {
	
	private enum LockState { READING, WRITING, IDLE };
	
	private int readers;
	private boolean writer;
	private Object lock;
	
	private int rmax;
	private double ravg;
	private int accWriteCount;
	private int rTotal;
	
	public ReadWriteMonitor() {
		readers = 0;
		writer = false;
		rmax = 0;
		ravg = 0;
		accWriteCount = 0;
		rTotal = 0;
		lock = new Object();
	}

	@Override
	public void acquireRead() {
		synchronized (lock) {
			try {
				// wait for the writer to finish
				while(getState() == LockState.WRITING) 
					lock.wait();
				
				readers++;
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
		}
	}

	@Override
	public void releaseRead() {
		synchronized (lock) {
			readers--;
			// notify waiting writers if there are no readers left
			if(getState() == LockState.IDLE) 
				lock.notifyAll();
		}
	}

	@Override
	public void acquireWrite() {
		accWriteCount++;
		synchronized(lock) {
			try {
				// wait for readers or writer to finish
				while(getState() != LockState.IDLE) {
					// compute stats
					rTotal += readers;
					rmax = Math.max(readers, rmax);
					System.out.format("[Writer-%d] Still waiting for %d readers%n", 
							Thread.currentThread().getId(), readers);
					
					lock.wait();
				}
				ravg = (double)rTotal / accWriteCount;
				System.out.format("[Writer-%d] rmax: %d, ravg: %.2f%n", 
						Thread.currentThread().getId(), rmax, ravg);
				
				writer = true;
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
		}
		
	}

	@Override
	public void releaseWrite() {
		synchronized(lock) {
			writer = false;
			// notify all waiting threads
			lock.notifyAll();
		} 
	}
	
	
	/**
	 * Get the current state of the lock, based on the
	 * writer and readers values
	 * 
	 * @return
	 */
	private LockState getState() {
		
		if(writer)
			return LockState.WRITING;
		else if(readers > 0)
			return LockState.READING;
		else
			return LockState.IDLE;
		
	}

}
