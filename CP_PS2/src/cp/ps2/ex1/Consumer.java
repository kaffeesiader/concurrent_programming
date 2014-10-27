package cp.ps2.ex1;

public class Consumer extends Thread {

	private Buffer buffer;
	
	public Consumer(Buffer buffer) {
		this.buffer = buffer;
	}
	
	@Override
	public void run() {
		int data;
		while(true) {
			data = buffer.get();
			System.out.println("[Consumer] received value '" + data + "' from buffer.");
			if(data == 0) {
				break;
			}
			
			try {
				sleep(2000);
			} catch (InterruptedException e) {
				System.out.println("[Consumer] interrupted!");
				break;
			}
		}
		
		System.out.println("[Consumer] shut down!");
	}
}
