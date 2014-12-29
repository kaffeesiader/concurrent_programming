package cp.ex3;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class AtomicFutureTasks {
	
	private final static int MAX_VALUE = 30;
	private static final Random rand = new Random();
	
	public void run() {
		// use a fixed thread pool with 8 threads
		ExecutorService executor = Executors.newFixedThreadPool(8);
		AtomicIntegerArray array = new AtomicIntegerArray(100);
		
		// create 8 FutureTasks and execute them in different threads
		for(int i = 0; i < 8; ++i) {
			executor.execute(new FutureTask<Object>(new Task(array), null));
		}
		
		// execute 2 FutureTasks in main thread, just to meet the exercise specification
		// this makes absolutely no sense to me...
		new FutureTask<Object>(new Task(array), null).run();
		new FutureTask<Object>(new Task(array), null).run();
		
		executor.shutdown();
		try {
			executor.awaitTermination(1, TimeUnit.HOURS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		validateResult(array);
	}
	
	private void validateResult(AtomicIntegerArray array) {
		for(int i = 0; i < array.length(); ++i) {
//			System.out.format("%3d: %d%n", i, array.get(i));
			if(array.get(i) > 39) {
				System.err.format("Element %d is above max value: %d%n", i, array.get(i));
				throw new IllegalStateException("Illegal value!");
			}
		}
	}
	
	private class Task implements Runnable {
		
		private AtomicIntegerArray array;
		
		public Task(AtomicIntegerArray arr) {
			array = arr;
		}

		@Override
		public void run() {
			for(int i = 0; i < array.length(); ++i) {
				double method = rand.nextDouble();
				int toAdd;
				
				if(method < 0.5) 
					toAdd = rand.nextInt(10) + 1;
				else
					toAdd = -(rand.nextInt(3) + 1);
				
				addValue(i, toAdd);	
			}
		}
		
		private void addValue(int index, int toAdd) {
			boolean done = false;
		
			while(!done) {
//				System.out.format("Adding value %d at index %d%n", value, index);
				int current = array.get(index);
				int newValue = current + toAdd;
				// do nothing if current value is greater or equal 30
				if(current >= MAX_VALUE) {
					done = true;
				} else {
					// make sure that the current value is still the one we read before,
					// otherwise retry with correct current value...
					done = array.compareAndSet(index, current, newValue);
				}
			}
		}
	}
	
	public static void main(String[] args) {
		
		int testRuns = 100;
		long minTime = Long.MAX_VALUE, maxTime = 0, avgTime = 0;
		
		AtomicFutureTasks aft = new AtomicFutureTasks();
		
		for(int i = 0; i < testRuns; ++i) {
			long startTime = System.nanoTime();
			aft.run();
			long duration = System.nanoTime() - startTime;
			
			minTime = Math.min(duration, minTime);
			maxTime = Math.max(duration, maxTime);
			avgTime += duration;
			
			System.out.format("Test run %d completed in %.3f ms.%n", i, duration/1e6);
		}
		
		avgTime /= testRuns;
		
		System.out.format("Minimum Time: %.3f ms%n", minTime/1e6);
		System.out.format("Maximum Time: %.3f ms%n", maxTime/1e6);
		System.out.format("Average Time: %.3f ms%n", avgTime/1e6);
	}

}
