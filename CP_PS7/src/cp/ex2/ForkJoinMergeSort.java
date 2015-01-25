package cp.ex2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public final class ForkJoinMergeSort<T extends Comparable<T>> extends RecursiveAction {

	private static final long serialVersionUID = 1L;
	private T[] array;
	private T[] temp;

	@SuppressWarnings("unchecked")
	private ForkJoinMergeSort(T[] array) {
		this.array = array;
		this.temp = (T[]) new Comparable[array.length];
	}
	
	@Override
	protected void compute() {
		// uses the bottom-up approach
		int len = array.length;
		
		for(int size = 1; size < len; size <<= 1) {
			
			List<MergeSortTask> tasks = new ArrayList<>();
			// create all tasks for this iteration...
	        for(int low = 0; low < len - size; low += size*2) {
	            int mid = low + size - 1;
	            int high = Math.min(low + (size*2 - 1), len - 1);

	            tasks.add(new MergeSortTask(low, mid, high));
	        }
	        // ... and execute them at once
	        invokeAll(tasks);
		}
	}
	
	private class MergeSortTask extends RecursiveAction {

		private static final long serialVersionUID = 1L;
		private int lo, mid, hi;
		
		public MergeSortTask(int lo, int mid, int hi) {
			this.lo = lo;
			this.mid = mid;
			this.hi = hi;
		}
		
		// actually does the merging of a sub array
		@Override
		protected void compute() {
			// Copy both parts into the helper array
			for (int i = lo; i <= hi; i++) {
				temp[i] = array[i];
			}

			int i = lo;
			int j = mid + 1;
			int k = lo;
			// Copy the smallest values from either the left or the right side back
			// to the original array
			while (i <= mid && j <= hi) {
				if (temp[i].compareTo(temp[j]) <= 0) {
					array[k] = temp[i];
					i++;
				} else {
					array[k] = temp[j];
					j++;
				}
				k++;
			}
			// Copy the rest of the left side of the array into the target array
			while (i <= mid) {
				array[k] = temp[i];
				k++;
				i++;
			}
		}
	}
	
	public static <T extends Comparable<T>> void sort(T[] array) {
		ForkJoinPool pool = new ForkJoinPool();
		ForkJoinMergeSort<T> sortTask = new ForkJoinMergeSort<>(array);
		pool.invoke(sortTask);
	}
	
	public static void main(String[] args) {
		
		String names[] = {
				"Martin",
				"Manfred",
				"Ursula",
				"Daniel",
				"Lukas",
				"Monika",
				"Eva",
				"Andreas",
				"Günther"
		};
		
		ForkJoinMergeSort.sort(names);
		System.out.println(Arrays.toString(names));
	}
}