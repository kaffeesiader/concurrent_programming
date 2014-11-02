package nlprog.ex2;

public class Producer extends Thread {
	public Producer(String name) {
		m_buffer = new Buffer();
		m_name = name;
		start();
	}

	public void run() {
		System.out.println(m_name + " running");
		try {
			int nextElement;
			do {
				nextElement = MainTest.getRandomInt(0, 100);
				System.out.println(m_name + " generated " + nextElement);
				m_buffer.put(nextElement);
				sleep(MainTest.getRandomInt(0,  3) * 1000);	
			} while(nextElement != 0);
		} catch (InterruptedException e) {
			System.out.println(m_name + " execution was interrupted");
			e.printStackTrace();
		}
		System.out.println(m_name  + " stopped running");
	}
	
	public Buffer getBuffer() {
		return m_buffer;
	}
	
	


	private Buffer m_buffer;
	private String m_name;
}
