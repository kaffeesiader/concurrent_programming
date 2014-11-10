package cp.ex3;

import java.util.Random;

public class TestThreadSafe {
	
	static final int MAXNUMBER = 100;
	
	public static void main(String[] args) {
		final ThreadSafeNumberRange nr = new ThreadSafeNumberRange();
		
		SafeChecker checker = new SafeChecker(nr);
		
		for (int i = 0; i < 100; i++) {
			SafeSetter setter = new SafeSetter(nr, i);
			setter.start();
		}
		
		checker.start();
	}
}

class SafeSetter extends Thread {
	
	private ThreadSafeNumberRange nr;
	
	public SafeSetter(ThreadSafeNumberRange numberRange, int pId) {
		super("Setter" + pId);
		nr = numberRange;
	}
	@Override
	public void run() {
		Random rand = new Random();
		
		while(true) {
			// let upper never be < 1, otherwise the next statement throws an exception if upper is zero...
			int upper = rand.nextInt(TestThreadSafe.MAXNUMBER) + 1;
			int lower = rand.nextInt(upper);
			
			// synchronize over the whole class and set upper and lower value
			// no exception handling necessary any more...
			synchronized (nr) {
				// set lower to zero before setting upper value...
				nr.setLower(0);
				nr.setUpper(upper);
				System.out.format("%s set upper value to '%d'%n", getName(), upper);
				nr.setLower(lower);
				System.out.format("%s set lower value to '%d'%n", getName(), lower);
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
					break;
				}
			}
		}
	}
}

class SafeChecker extends Thread {
	
	private ThreadSafeNumberRange nr;
	
	public SafeChecker(ThreadSafeNumberRange numberRange) {
		super("Checker");
		nr = numberRange;
	}
	
	@Override
	public void run() {
		Random rand = new Random();
		
		while(true) {
			int value = rand.nextInt(TestThreadSafe.MAXNUMBER);
			boolean inRange = nr.isInRange(value);
			System.out.format("Value '%d' is%s in range%n", value, inRange ? "" : " not");
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
		}
	}
}
