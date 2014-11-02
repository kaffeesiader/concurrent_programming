package nlprog.ex1;

public class Consumer extends Thread {
	public Consumer(Buffer b) {
		m_buffer = b;
	}

	public void run() {
		System.out.println("Consumer running");
		try {
			Integer receivedElement;
			do {
				receivedElement = m_buffer.get();
				if(receivedElement == null) {
					System.out.println("Consumer buffer is empty, sleeping");
					sleep(2000);
					continue;
				}
				System.out.println("Consumer received " + receivedElement);
			} while(receivedElement == null || receivedElement != 0);
		} catch (InterruptedException e) {
			System.out.println("Execution was interrupted");
			e.printStackTrace();
		}
		System.out.println("Consumer stopped running");
	}
	
	private Buffer m_buffer;
}
