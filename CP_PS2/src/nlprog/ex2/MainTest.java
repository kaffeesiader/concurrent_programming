package nlprog.ex2;

import java.util.ArrayList;
import java.util.Random;

public class MainTest {
	private static Random ran = new Random();
	public static synchronized int getRandomInt(int from, int to) {
		return ran.nextInt(to - from + 1) + from;
	}

	public static void main(String[] args) {
		System.out.println("Welcome to nlprog Blatt2, Aufgabe 1");
		ArrayList<Producer> pArr = new ArrayList<Producer>();
		for(int i = 0; i < 4; ++i) {
			pArr.add(new Producer("Producer" + i));
		}
		new Consumer(pArr);
	}

}
