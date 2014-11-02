package nlprog.ex1;

public class Producer extends Thread {
	public Producer(Buffer b) {
		m_buffer = b;
	}

	public void run() {
		System.out.println("Producer running");
		try {
			int nextElement;
			do {
				nextElement = MainTest.getRandomInt(0, 100);
				System.out.println("Producer generated " + nextElement);
				m_buffer.put(nextElement);
				sleep(MainTest.getRandomInt(0,  3) * 1000);	
			} while(nextElement != 0);
		} catch (InterruptedException e) {
			System.out.println("Execution was interrupted");
			e.printStackTrace();
		}
		System.out.println("Producer stopped running");
	}


	private Buffer m_buffer;
}
