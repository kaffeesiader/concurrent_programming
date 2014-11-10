package cp.ex3;

import java.util.Random;

public class TestNotSafe {
	
	static final int MAXNUMBER = 100;
	
	public static void main(String[] args) {
		final NumberRange nr = new NumberRange();
		
		Checker checker = new Checker(nr);
		
		for (int i = 0; i < 100; i++) {
			Setter setter = new Setter(nr, i);
			setter.start();
		}
		
		checker.start();
	}
}

class Setter extends Thread {
	
	private NumberRange nr;
	
	public Setter(NumberRange numberRange, int pId) {
		super("Setter" + pId);
		nr = numberRange;
	}
	@Override
	public void run() {
		Random rand = new Random();
		
		while(true) {
			// let upper never be < 1, otherwise the next statement throws an exception if upper is zero...
			int upper = rand.nextInt(TestNotSafe.MAXNUMBER) + 1;
			int lower = rand.nextInt(upper);
			
			try {
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
			} catch (IllegalArgumentException e) {
				System.out.println("Setting values failed");
			}
		}
	}
}

class Checker extends Thread {
	
	private NumberRange nr;
	
	public Checker(NumberRange numberRange) {
		super("Checker");
		nr = numberRange;
	}
	
	@Override
	public void run() {
		Random rand = new Random();
		
		while(true) {
			int value = rand.nextInt(TestNotSafe.MAXNUMBER);
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
