package nlprog.ex1;

import java.util.Random;

public class MainTest {
	private static Random ran = new Random();
	public static synchronized int getRandomInt(int from, int to) {
		return ran.nextInt(to - from + 1) + from;
	}

	public static void main(String[] args) {
		System.out.println("Welcome to nlprog Blatt2, Aufgabe 1");
		Buffer b = new Buffer();
		Producer p = new Producer(b);
		Consumer c = new Consumer(b);
		p.start();
		c.start();
	}

}
