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
	
	public ReadWriteMonitor() {
		readers = 0;
		writer = false;
		rmax = 0;
		ravg = 0;
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
		
		synchronized(lock) {
			try {
				// wait for readers or writer to finish
				while(getState() != LockState.IDLE) {
					// compute stats
					rmax = Math.max(readers, rmax);
					ravg = (ravg+readers)/2;
					System.out.format("[acquireWrite] r: %d, rmax: %d, ravg: %.2f%n", readers, rmax, ravg);
					lock.wait();
				}
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
