package cp.ex3;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


/**
 * The SocketServerThread listens on port MainTest.PORT for incomming connections.
 * If a client is connected it will be notified about received prime numbers.
 * The message is a string in the format '[NUMBER] [MESSAGE]'
 * The server implements the PrimeListener interface and can therefore be registered
 * on a PrimeChecker. Prime numbers are sent to the server, using the corresponding 
 * 'handlePrime' method.
 */
public class SocketServerThread extends Thread implements PrimeListener {
	
	private static final Random rand = new Random();
	private BlockingQueue<Integer> queue;
	
	public SocketServerThread() {
		queue = new ArrayBlockingQueue<>(10);
	}
	
	@Override
	public void run() {
		
		try (	ServerSocket server = new ServerSocket(MainTest.PORT);
				Socket client = server.accept();
				PrintStream out = new PrintStream(new BufferedOutputStream(client.getOutputStream()));)
		{
			while(!isInterrupted()) {
				int value = queue.take();
				String message = String.format("%d I'm pretty sure, '%d' is one of the coolest primes ever discovered!", value, value);
				System.out.println("[SocketServer] Sending prime '" + value + "' to client");
				out.println(message);
				out.flush();
				
				sleep(rand.nextInt(3000));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException handled) {
			// nothing to do here...
		}
		
		System.out.println("[SocketServer] Shutdown completed.");
	}
	
	/**
	 * Forces the server to shut down.
	 * The server then closes the client connection (if existing) and exits 
	 * the main loop.
	 */
	public void shutdown() {
		// send interrupt to stop server
		interrupt();
	}

	@Override
	public void handlePrime(int prime) {
		try { queue.put(prime); }
		catch (InterruptedException e) {}
	}
	
}
