package cp.ex3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

/**
 * The SocketClientThread connects to a SocketServerThread on port MainTest.PORT
 * and handles the published prime numbers and messages.
 * It expects text messages in the format '[NUMBER] [MESSAGE]'.
 * Received prime numbers and messages are printed to the console output.
 * The SocketClientThread is forced to shut down, using the 'interrupt' method
 */
public class SocketClientThread extends Thread {
	
	private static final Random rand = new Random();
	
	@Override
	public void run() {
		
		try (
				Socket socket = new Socket("localhost", MainTest.PORT);
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			) 
			
		{
			// isInterrupted() checks the interrupted status of the current thread
			// without clearing it.
			while(!isInterrupted()) {
				String msg = in.readLine();
				
				// null checking is necessary because null-String is returned when
				// connection to server gets closed...
				if(msg != null) {
					
					String[] tokens = msg.split(" ", 2);
					int prime = Integer.parseInt(tokens[0]);
					String data = tokens[1];
					
					System.out.format("[SocketClient] Received the number '%d'%n", prime);
					System.out.format("[SocketClient] The sender sais: '%s'%n", data);
					
				}
				
				
				sleep(rand.nextInt(3000));
			}
			
		} catch (NumberFormatException e) {
			System.out.println("[SocketClient] Error parsing message - illegal number format");
		} catch (UnknownHostException e) {
			System.out.println("[SocketClient] Unable to connect to server");
		} catch (IOException e) {
			// this should not happen...
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.out.println("[SocketClient] Interrupt received.");
			// re-call interrupt because by catching the Exception, the interrupted flag was cleared...
			interrupt();
		}	
		
		System.out.println("[SocketClient] Shutdown completed.");
		System.out.println("[SocketClient] Interrupt status: " + isInterrupted());
	}
}
