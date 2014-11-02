package nlprog.ex2;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Consumer extends Thread {
	public Consumer(List<Producer> pArr) {
		m_pArr = new LinkedList<Producer>(pArr);
		start();
	}

	public void run() {
		System.out.println("Consumer running");
		do {
			consumeData();
		} while(!m_pArr.isEmpty());
		System.out.println("Consumer stopped running");
	}
	
	private void consumeData() {
		for(Iterator<Producer> iter = m_pArr.iterator(); iter.hasNext();) {
			Producer prod = iter.next();
			try {
				prod.getBuffer().waitForData();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		int[] elements = new int[m_pArr.size()];
		int count = 0;
		for(Iterator<Producer> iter = m_pArr.iterator(); iter.hasNext(); ++count) {
			Producer prod = iter.next();
			elements[count] = prod.getBuffer().get();
			if(elements[count] == 0) {
				iter.remove();
			}
		}
		System.out.println("Consumer retrieved " + printIntArray(elements));
	}
	
	private String printIntArray(int[] elements) {
		String result = new String();
		for(int i = 0; i < elements.length; ++i) {
			result += elements[i] + " ";
		}
		return result.trim();
	}
	
	private List<Producer> m_pArr;
}
