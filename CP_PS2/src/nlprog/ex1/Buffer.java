package nlprog.ex1;

import java.util.LinkedList;
import java.util.Queue;

public class Buffer {
	public Buffer() {
		m_queue = new LinkedList<Integer>();
	}
	
	
	public synchronized void put(int elem) {
		m_queue.add(elem);
	}
	
	public synchronized Integer get() { //return null when queue is empty, else the next element
		return m_queue.poll();
	}
	
	private Queue<Integer> m_queue;
}
