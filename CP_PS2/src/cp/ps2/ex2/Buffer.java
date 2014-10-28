package cp.ps2.ex2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer {

	private Integer[] data;
	private ReentrantLock lock;
	private Condition bufferFull;
	private Condition bufferClear;
	
	public Buffer(int size) {
		data = new Integer[size];
		lock = new ReentrantLock();
		bufferFull = lock.newCondition();
		bufferClear = lock.newCondition();
	}
	
	public void put(int index, int value) {
		if(index < 0 || index >= data.length) {
			throw new IndexOutOfBoundsException();
		}
		lock.lock();
		try {
			while(hasValue(index)) {
				try {
					bufferClear.await();
				} catch (InterruptedException e) {
					System.out.println(Thread.currentThread() + " interrupted while putting!");
					return;
				}
			}
			data[index] = value;
			// check if buffer is filled
			if(bufferFull()) {
				bufferFull.signal();
			}
		} finally {
			lock.unlock();
		}
	}
	
	public synchronized int get(int index) {
		if(index < 0 || index >= data.length) {
			throw new IndexOutOfBoundsException();
		}
		lock.lock();
		try {
			while(!bufferFull()) {
				try {
					bufferFull.await();
				} catch (InterruptedException e) {
					System.out.println(Thread.currentThread().getName() + " interrupted while getting!");
					return -1;
				}
			}
			
			int value = data[index];
			if(value != 0) {
				data[index] = null;
			}
			if(bufferEmpty()) {
				bufferClear.signalAll();
			}
			
			return value;
			
		} finally {
			lock.unlock();
		}
	}
	
	public int size() {
		return data.length;
	}
	
	private boolean hasValue(int index) {
		return data[index] != null;
	}
	
	private boolean bufferFull() {
		boolean filled = true;
		for (Integer value : data) {
			if(value == null) {
				filled = false;
			}
		}
		return filled;
	}
	
	private boolean bufferEmpty() {
		boolean empty = true;
		for (Integer value : data) {
			if(value != null) {
				empty = false;
			}
		}
		return empty;
	}
}
