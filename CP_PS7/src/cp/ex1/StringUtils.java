package cp.ex1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class StringUtils {
	
	private static final String FILE_NAME = "words.txt";
	private static Random rand = new Random();
	
	public static String[] createTestSet(int size) {
		File file = new File(FILE_NAME);
		
		if(!file.exists()) {
			throw new IllegalStateException("Unable to open input file '" + FILE_NAME + "'");
		}
		
		List<String> words = new ArrayList<>();
		
		try(Scanner scn = new Scanner(file)) {
			while(scn.hasNext())
				words.add(scn.next());
			
		} catch (FileNotFoundException consumed) {}
		
		String[] testSet = new String[size];
		
		for(int i = 0; i < size; ++i)
			testSet[i] = words.get(rand.nextInt(words.size()));
		
		return testSet;
	}

}
