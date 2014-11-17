package nlprog3_3;

import java.util.concurrent.atomic.*;
import java.util.Random;

public class NumberRangeSafe {
    // INVARIANT: lower <= upper
    private int lower = 0;
    private int upper = 0;


    private static Random ran = new Random();
    public static synchronized int getRandomInt(int from, int to) {
        return ran.nextInt(to - from + 1) + from;
    }

    public synchronized void setLowerAndUpper(int l, int u) {
        // Warning -- unsafe check-then-act
        if (l > u) {
            throw new IllegalArgumentException("can't set lower to " + l + " > " + u);
        }
        lower = l;
        upper = u;
    }

    public synchronized boolean isInRange(int i) {
        return (i >= lower && i <= upper);
    }

    public static void main(String[] args) {
        System.out.println("Welcome to number range crazyness");
        final NumberRangeSafe nr = new NumberRangeSafe();
        for(int i = 0; i < 3; ++i) {
            (new Thread(new Runnable() {
                    public void run() {
                        while(true) {
                            int high = getRandomInt(0, 10);
                            int low = getRandomInt(0, high);
                            nr.setLowerAndUpper(low, high);
                            try {
                                Thread.sleep(getRandomInt(500, 2500));
                            } catch(Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                })).start();
        }

        //use main thread for isInRangeTests
        while(true) {
            int i = getRandomInt(-1, 11);
            if(nr.isInRange(i)) {
                System.out.println(i + " was in range");
            } else {
                System.out.println(i + " was not in range");
            }
            try {
                Thread.sleep(100);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
