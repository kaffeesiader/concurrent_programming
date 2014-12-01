package nlprog;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class Benchmark {
	static Random random = new Random();
	
	public static void main(String[] args) {
		System.out.println("Welcome to benchmark town");
		HashMap<Integer, Integer>           nhm = new HashMap<Integer, Integer>();
		SyncMap<Integer, Integer>           shm = new SyncMap<Integer, Integer>();
		ConcurrentHashMap<Integer, Integer> chm = new ConcurrentHashMap<Integer, Integer>();
		
		for(int numThreads = 1; numThreads <= 16; numThreads <<= 1) {
			System.out.println("\n\n############### " + numThreads + " threads #############");
			System.out.println("##### Running with 50% insert, 25% read, 25% remove");
			measureMap(nhm, 0.5, 0.25, "Unsynchronized", numThreads);
			measureMap(shm, 0.5, 0.25, "Synchronized", numThreads);
			measureMap(chm, 0.5, 0.25, "Concurrent", numThreads);
			
			nhm.clear();
			shm.clear();
			chm.clear();
			
			System.out.println("##### Running with 80% insert, 15% read, 5% remove");
			measureMap(nhm, 0.8, 0.15, "Unsynchronized", numThreads);
			measureMap(shm, 0.8, 0.15, "Synchronized", numThreads);
			measureMap(chm, 0.8, 0.15, "Concurrent", numThreads);
			
			nhm.clear();
			shm.clear();
			chm.clear();
		}
	
		System.out.println("Bye!");
	}
	
	public static void measureMap(final AbstractMap<Integer, Integer> m, final double insertChance, double readChance, String name, final int numThreads) {
		Thread[] tarr = new Thread[numThreads];
		long startTime = System.nanoTime();
		for(int t = 0; t < numThreads; ++t) {
			tarr[t] = new Thread(new Runnable() {
				public void run() {
					for(int i = 0; i < 1000 * 1024 / numThreads; ++i) {
						int randomInt = random.nextInt();
						double percent = random.nextDouble();
						if(percent < insertChance) { //e.g. 50% insert
							m.put(randomInt, randomInt);
						} else if(percent <= 0.75) { //e.g. 25% read
							m.containsKey(randomInt);
						} else { //e.g. 25% remove
							m.remove(randomInt);
						}
					}
				}
			});
			tarr[t].start();
		}
		for(int t = 0; t < numThreads; ++t) {
			try {
				tarr[t].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		long endTime = System.nanoTime();
		System.out.println(name + " took " + ((endTime - startTime) / 1000000) + "ms"); 
	}
}
