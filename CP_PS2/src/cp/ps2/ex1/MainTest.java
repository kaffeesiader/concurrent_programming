package cp.ps2.ex1;

public class MainTest {
	
	public static void main(String[] args) throws InterruptedException {
		Buffer buffer = new Buffer();
		Producer prod = new Producer(buffer);
		Consumer cons = new Consumer(buffer);
		
		System.out.println("Starting Producer and Cosumer.");
		
		cons.start();
		prod.start();
		
		prod.join();
		cons.join();
		
		System.out.println("Producer/Consumer test finished!");
	}

}
