package uibk.cp.exercise1;

import java.io.IOException;

public class Exercise1 {

	public static void main(String[] args) throws InterruptedException, IOException {

		// ------------------ Create 8 threads and print thread ID ------------------------

		int n = 8;

		System.out.println("Creating " + n + " threads: \n");

		for (int i = 0; i < n; i++) {
			Thread thread = new Thread(new Runnable() {

				@Override
				public void run() {
					long id = Thread.currentThread().getId();
					System.out.println("Thread with ID '" + id + "' started!");
				}
			});

			thread.start();
		}
		
		Thread.sleep(1000);
		
		System.in.read();

		// ---------------------- Create threads until system crashes---------------------------

		int i = 1;

		while (true) {
			System.out.println("Creating thread " + i);

			Thread thread = new Thread(new Runnable() {

				@Override
				public void run() {
					while(true) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							System.err.println(e);
						}
					}
				}
			});

			thread.start();
			i++;
			
//			Thread.sleep(1000);
		}
	}

}
