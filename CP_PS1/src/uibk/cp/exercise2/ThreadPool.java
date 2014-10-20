package uibk.cp.exercise2;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Simple implementation of a ThreadPool that allows to use a variable amount
 * of WorkerThreads
 */
public class ThreadPool {
	
	private List<WorkerThread> workers;
	private Queue<Runnable> tasks;
	private boolean shutdown;
	
	public ThreadPool(int threadCount) {
		workers = new ArrayList<>();
		tasks = new ConcurrentLinkedQueue<>();
		shutdown = false;
		
		for (int i = 0; i < threadCount; i++) {
			WorkerThread thread = new WorkerThread();
			workers.add(thread);
			thread.start();
		}
	}
	
	public synchronized void executeTask(Runnable task) {
		
		if(shutdown) {
			throw new IllegalStateException("Sorry, we are shutting down and cannot accept new jobs...");
		}
		
		// only allow as much tasks as we have workers
		while(tasks.size() >= workers.size()) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// add the new task to queue and notify the others
		tasks.add(task);
		notifyAll();
	}
	
	private synchronized Runnable nextTask() {
		// wait until there is a new task or until shutdown...
		while(tasks.isEmpty() && !shutdown) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("Thread " + Thread.currentThread().getId() + " interrupted!");
				// interruption means the thread has to shut down, so we return null
				return null;
			}
		}
		
		Runnable task = tasks.poll();
		// we have to notify all, otherwise we can get a deadlock...
		notifyAll();
		
		return task;
	}
	
	public void shutdown() {
		shutdown = true;
		
		for (WorkerThread thread : workers) {
			try {
				thread.interrupt();
				thread.join();
			} catch (InterruptedException e) {
				System.out.println("Thread '" + thread.getId() + "' caused InterruptedException!");
			}
		}
	}
	
	private class WorkerThread extends Thread {
		@Override
		public void run() {
			
			boolean stopped = false;
			
			System.out.println("Thread '" + this.getId() + "' started!");
			
			while(!stopped) {
				// if there is a task, execute it...
				Runnable task = ThreadPool.this.nextTask();
				if(task == null) {
					stopped = true;
				} else {
					task.run();
				}
			}
			
			System.out.println("Thread '" + this.getId() + "' stopped!");
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		System.out.println("Creating ThreadPool...");
		
		ThreadPool tp = new ThreadPool(4);
		
		for (int i = 0; i < 10000; i++) {
			final int n = i;
			Runnable t = new Runnable() {
				@Override
				public void run() {
					System.out.println("[" + Thread.currentThread().getId() + "]This is number " + n);
				}
			};
			
			tp.executeTask(t);
		}
		
		tp.shutdown();
		
		System.out.println("Shutdown complete");
	}

}
