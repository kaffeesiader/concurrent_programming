import java.math.BigDecimal;
import java.math.RoundingMode;

public class ThreadPI implements Runnable {
	private static int threadWorkblockSize = 4; //each work block contains this amount of sums, needs to be even
	private static final BigDecimal desiredAccuracy = new BigDecimal(0.00001); //how far away from PI the result may be
	
	private static BigDecimal result = new BigDecimal(0);
	private static boolean areFinished = false;
	private static int threadID = 0;
	private static int numberThreads;
	private static int currentBlockNumber = 0;
	private static int currentBlockThreadSubmissions = 0;
	
	public static void main(String[] args) throws InterruptedException {
		numberThreads = 2;
		if(args.length > 0) {
			numberThreads = Integer.parseInt(args[0]);
		}
		threadWorkblockSize = numberThreads * 2 * 20;
		System.out.println("Running " + numberThreads + " threads...");
		for(int i = 0; i < numberThreads; ++i) {
			(new Thread(new ThreadPI())).start();
		}
		while(!areFinished) {
			Thread.sleep(100);
		}
		System.out.println("Result: " + result);
		System.out.println("PI: " + Math.PI);
	}

	public void run() {
		int tID = getThreadID();
		int blockNumber = 0;
		while(!areFinished) {
			int pos = blockNumber*numberThreads*threadWorkblockSize*2 + tID*threadWorkblockSize*2;
			BigDecimal toAdd = new BigDecimal(0);
			for(int i = 0; i < threadWorkblockSize; i++) {
				int cur = pos + 2*i;
				//System.out.println(tID + ": + 1/" +  (2*cur + 1));
				toAdd = toAdd.add(     new BigDecimal(1).divide(new BigDecimal(2*cur + 1), 15, RoundingMode.HALF_UP));
				++cur;
				toAdd = toAdd.subtract(new BigDecimal(1).divide(new BigDecimal(2*cur + 1), 15, RoundingMode.HALF_UP));
				//System.out.println(tID + ": - 1/" +  (2*cur + 1));
			}
			blockNumber = addResult(toAdd, blockNumber);
		}
	}
	
	private static synchronized int getThreadID() {
		return threadID++;
	}
	private static synchronized int addResult(BigDecimal toAdd, int blockNumber) {
		if(!areFinished && blockNumber == currentBlockNumber) {
			//System.out.println("Submission: " + (currentBlockThreadSubmissions + 1));
			currentBlockThreadSubmissions++;
			result = result.add(toAdd);
			if(currentBlockThreadSubmissions == numberThreads) {
				if(blockNumber % (400/numberThreads) == 0) {
					System.out.println("Completed block " + blockNumber);
				}
				currentBlockThreadSubmissions = 0;
				currentBlockNumber++;
				//System.out.println("Current Result: " + result.multiply(new BigDecimal(4)));
				if(result.multiply(new BigDecimal(4)).subtract(new BigDecimal(Math.PI)).abs().compareTo(desiredAccuracy) != 1) {
					result = result.multiply(new BigDecimal(4));
					areFinished = true;
					return 0;
				}
			}
			return blockNumber + 1;
		}
		return blockNumber;
	}
}
