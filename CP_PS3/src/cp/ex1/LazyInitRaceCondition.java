package cp.ex1;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class LazyInitRaceCondition {
	
	private ExpensiveObject instance = null;
//	private Object lock = new Object();
	
	public ExpensiveObject getInstance() {
//		synchronized (lock) {
			if(instance == null) {
				instance = new ExpensiveObject();
			}
//		}
		return instance;
//		return LazyHolder.INSTANCE;
	}
	
	public static void main(String[] args) {
		
		final LazyInitRaceCondition l = new LazyInitRaceCondition();
		
		for (int i = 0; i < 3; i++) {
			final int tId = i;
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					// aquire an instance of our expensive object...
					ExpensiveObject o = l.getInstance();
					System.out.format("Thread[%d] got instance '%s'%n", tId, o);
				}
			});	
			t.start();
		}	
	}
	
//	private static class LazyHolder {
//		public static final ExpensiveObject INSTANCE = new ExpensiveObject();
//	}
}

class ExpensiveObject {
	
	private static AtomicInteger instanceCounter = new AtomicInteger(0);
	private String name;
	
	public ExpensiveObject() {
		name = "ExpensiveObject" + instanceCounter.getAndIncrement();
//		System.out.println(this + " constructed!");
		int rnd = new Random().nextInt(10000);
		for (int i = 0; i < rnd; i++) ;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
