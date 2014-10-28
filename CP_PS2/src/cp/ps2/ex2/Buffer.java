package cp.ps2.ex2;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer {

	private ReentrantLock lock;
	private Condition itemPut;
	private Condition itemRemoved;
	private Map<Integer, Integer> data;
	// use a queue to remove the items in fifo order...
	private Queue<Integer> queue;
	
	public Buffer() {
		queue = new LinkedList<>();
		data = new HashMap<Integer, Integer>();
		lock = new ReentrantLock();
		itemPut = lock.newCondition();
		itemRemoved = lock.newCondition();
	}
	
	public void put(int index, int value) {
		lock.lock();
		try {
			while(data.containsKey(index)) {
				itemRemoved.await();
			}
			data.put(index, value);
			queue.add(index);
			// in case we have more than one consumers...
			itemPut.signalAll();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
	
	public int[] get(int size) {
		lock.lock();
		try {
			while(data.size() < size) {
				itemPut.await();
			}
			
			int values[] = new int[size];
			for (int i = 0; i < size; i++) {
				// remove items in the same order they were added...
				int id = queue.remove();
				values[i] = data.remove(id);
			}
			
			// notify all waiting producers
			itemRemoved.signalAll();
			return values;
			
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		} finally {
			lock.unlock();
		}
	}
}
