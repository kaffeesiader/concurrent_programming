package cp.ex2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class MergeSortTest {
	
	private static final String FILE_NAME = "words.txt";
	private static Random rand = new Random();
	
	private List<String> words;
	
	public MergeSortTest() {
		File file = new File(FILE_NAME);
		
		if(!file.exists()) {
			throw new IllegalStateException("Unable to open input file '" + FILE_NAME + "'");
		}
		
		System.out.format("Reading input file '%s'...%n", FILE_NAME);
		words = new ArrayList<>();
		
		try(Scanner scn = new Scanner(file)) {
			while(scn.hasNext())
				words.add(scn.next());
			
		} catch (FileNotFoundException consumed) {}
		
		System.out.format("%d words read.%n", words.size());
	}
	
	public String[] createTestSet(int n) {
		List<String> testSet = new ArrayList<>();
		
		int size = words.size();
		
		for(int i = 0; i < n; ++i) {
			testSet.add(words.get(rand.nextInt(size)));
		}
		
		return testSet.toArray(new String[n]);
	}
	
	private void pause() {
		System.out.println("Hit return to continue...");
		try {
			System.in.read();
		} catch (IOException consumed) {}
	
	}
	
	public void runTest(int n) {
		System.out.println("Running test with size " + n);
		String[] testSet = createTestSet(n);
		
		pause();
		// ForkJoinMergeSort
		String[] array = testSet.clone();
		long startTime = System.nanoTime();
		ForkJoinMergeSort.sort(array);
		long duration = System.nanoTime() - startTime;
		System.out.format("ForkJoinMergeSort took %.2f ms%n", duration / 1e6);
		
		pause();
		// MergeSort
		array = testSet.clone();
		startTime = System.nanoTime();
		MergeSort.sort(array);
		duration = System.nanoTime() - startTime;
		System.out.format("MergeSort took %.2f ms%n", duration / 1e6);
		
		pause();
		// Java sort
		array = testSet.clone();
		startTime = System.nanoTime();
		Arrays.sort(array);
		duration = System.nanoTime() - startTime;
		System.out.format("Java sort took %.2f ms%n", duration / 1e6);
	}
	
	
	public static void main(String[] args) {
		int testSizes[] = {1000, 100000, 1000000, 10000000};
		MergeSortTest test = new MergeSortTest();
		
		System.out.println("Starting test series...");
		System.out.println();
		
		for (int i = 0; i < testSizes.length; i++) {
			int testSize = testSizes[i];
			test.runTest(testSize);
			System.out.println();
		}
		
		System.out.println("Test finished!");
	}

}
