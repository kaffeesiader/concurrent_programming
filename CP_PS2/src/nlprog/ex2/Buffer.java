package nlprog.ex2;

import java.util.LinkedList;
import java.util.Queue;

public class Buffer {
	public Buffer() {
		m_queue = new LinkedList<Integer>();
	}
	
	public synchronized void put(int elem) {
		m_queue.add(elem);
		notify();
	}
	
	public synchronized Integer get() { 
		return m_queue.remove();	
	}
	
	public synchronized void waitForData() throws InterruptedException {
		if(m_queue.isEmpty()) {
			System.out.println("Consumer has to wait() on data");
			wait();
		}
	}
	
	private Queue<Integer> m_queue;
}
