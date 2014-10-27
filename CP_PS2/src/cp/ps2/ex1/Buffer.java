package cp.ps2.ex1;

public class Buffer {

	private int data;
	private boolean hasValue = false;
	
	public synchronized void put(int value) {
		
		while(hasValue) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.out.println(Thread.currentThread().getName() + " interrupted while putting!");
				return;
			}
		}
		
		data = value;
		hasValue = true;
		// as we have only one porducer and consumer, notify is sufficient...
		notify();
	}
	
	public synchronized int get() {
		
		while(!hasValue) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.out.println(Thread.currentThread().getName() + " interrupted while getting!");
				return -1;
			}
		}
		
		hasValue = false;
		notify();
		return data;
	}
}
