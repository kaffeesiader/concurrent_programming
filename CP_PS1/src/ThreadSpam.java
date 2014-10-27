
public class ThreadSpam implements Runnable {
	public static void main(String[] args) throws InterruptedException {
		int threads = 10;
		if(args.length > 0) {
			threads = Integer.parseInt(args[0]);
		}
		System.out.println("Running " + threads + " threads...");
		for(int i = 0; i < threads; ++i) {
			(new Thread(new ThreadSpam())).start();
		}
		Thread.sleep(9999999999l);
	}
	
	public void run() {
		System.out.println(Thread.currentThread().getId());
		try {
			Thread.sleep(9999999999l);
		} catch (InterruptedException e) {
			System.out.println("Sleep interrupted :(");
			e.printStackTrace();
		}
	}
}
