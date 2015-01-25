package cp.ex2;

import java.util.Arrays;

public final class MergeSort<T extends Comparable<T>> {
	
	private final T[] array;
	private final T[] helper;
	
	@SuppressWarnings("unchecked")
	private MergeSort(T[] array) {
		this.array = array;
		this.helper = (T[])new Comparable[array.length];
	}
	
	public void sort() {
		sort(0, array.length - 1);
	}
	
	private void sort(int lo, int hi) {
		if(lo < hi) {
			// Get the index of the element which is in the middle
		      int middle = lo + (hi - lo) / 2;
		      // Sort the left side of the array
		      sort(lo, middle);
		      // Sort the right side of the array
		      sort(middle + 1, hi);
		      // Combine them both
		      merge(lo, middle, hi);
		}
	}
	
	private void merge(int lo, int mid, int hi) {
		// Copy both parts into the helper array
		for (int i = lo; i <= hi; i++) {
			helper[i] = array[i];
		}

		int i = lo;
		int j = mid + 1;
		int k = lo;
		// Copy the smallest values from either the left or the right side back
		// to the original array
		while (i <= mid && j <= hi) {
			if (helper[i].compareTo(helper[j]) <= 0) {
				array[k] = helper[i];
				i++;
			} else {
				array[k] = helper[j];
				j++;
			}
			k++;
		}
		// Copy the rest of the left side of the array into the target array
		while (i <= mid) {
			array[k] = helper[i];
			k++;
			i++;
		}
	}
	
	public static <T extends Comparable<T>> void sort(T[] array) {
		MergeSort<T> ms = new MergeSort<>(array);
		ms.sort();
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
		
		MergeSort.sort(names);
		System.out.println(Arrays.toString(names));
	}

}
