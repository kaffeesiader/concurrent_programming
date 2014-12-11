package cp.ex3;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class MainTest {
	
	public static final int POISON_PILL = -1;
	public static final int PORT = 1234;
	
	private NumberGenerator generator;
	private PrimeChecker checker;
	private SocketServerThread server;
	private SocketClientThread client;
	private boolean started;
	
	public MainTest() {

		BlockingQueue<Integer> msgQueue = new ArrayBlockingQueue<>(10);
		
		generator = new NumberGenerator(msgQueue);
		checker = new PrimeChecker(msgQueue);
		server = new SocketServerThread();
		client = new SocketClientThread();
		
		checker.registerListener(server);
		started = false;
	}
	
	public void start() {
		if(started)
			return; 
		
		server.start();
		client.start();
		generator.start();
		checker.start();
		
		started = true;
	}
	
	public void stop() {
		if(!started)
			return;
		
		try {
			// init shutdown of NumberGenerator and wait for both threads to shut down.	
			generator.shutdown();
			generator.join();
			checker.join();
			// shutdown server and client
			server.shutdown();
			client.interrupt();
			server.join();
			client.join();
			
			started = false;
			
		} catch (InterruptedException e) {
			// nothing to do here
		}
		System.out.println("All threads shut down!");
	}
	
	public static void main(String[] args) throws InterruptedException {
		
		final MainTest test = new MainTest();
	
		// this could handle ctrl-c for example...
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				test.stop();
			}
		});
		
		test.start();
//		Thread.sleep(10000);
//		test.stop();
	}
}
