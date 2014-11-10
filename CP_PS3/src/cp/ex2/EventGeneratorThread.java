package cp.ex2;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class EventGeneratorThread extends Thread implements EventGenerator {

	private final ThreadLocal<List<SafeEventListener>> threadSafe = new ThreadLocal<>();
	private final Queue<Runnable> pendingOps = new ConcurrentLinkedQueue<Runnable>();
	private final int id;
	
	public EventGeneratorThread(int id) {
		this.id = id;
	}
	
	@Override
	public void infoListeningObjects() {
		final String askingThread = Thread.currentThread().toString(); // ListeningObjectsThread
		final String currentThread = this.toString(); // EventGeneratorThread
		// create a operation for adding the new listener
		Runnable op = new Runnable() {
			@Override
			public void run() {
				System.out.println(String.format("%n%s asks %s for registered listeners:", askingThread, currentThread));
				for (SafeEventListener listener : threadSafe.get()) {
					System.out.format(" - %s%n", listener);
				}
				System.out.println();
			}
		};
		
		pendingOps.add(op);
	}
	
	@Override
	public void run() {
		
		threadSafe.set(new ArrayList<SafeEventListener>());
		
		while(true) {
			try {
				// perform all pending operations
				while(!pendingOps.isEmpty()) {
					pendingOps.poll().run();
				}
				// fire our event
				fireEvent(new Date());
				sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println(this + " interrupted!");
				break;
			}
		}
	}
	
	private void fireEvent(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("MM/dd.MM.yyy HH:mm:ss");
		String msg = String.format("%s  %s", this, dateFormat.format(date));
		MessageEvent ev = new MessageEvent(msg);
		
		for (SafeEventListener listener : threadSafe.get()) {
			listener.handleEvent(ev);
		}
	}

	@Override
	public void registerListener(final SafeEventListener listener) {
		// create a operation for adding the new listener
		pendingOps.add(new Runnable() {
			@Override
			public void run() {
				threadSafe.get().add(listener);
			}
		});
	}
	
	@Override
	public String toString() {
		return "[Generator" + id + "]";
	}

}
