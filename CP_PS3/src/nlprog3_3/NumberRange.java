package nlprog3_3;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class NumberRange {
    // INVARIANT: lower <= upper
    private final AtomicInteger lower = new AtomicInteger(0);
    private final AtomicInteger upper = new AtomicInteger(0);


    private static Random ran = new Random();
    public static synchronized int getRandomInt(int from, int to) {
        return ran.nextInt(to - from + 1) + from;
    }

    public void setLower(int i) {
        // Warning -- unsafe check-then-act
        if (i > upper.get()) {
            throw new IllegalArgumentException("can't set lower to " + i + " > upper");
        }
        lower.set(i);
    }

    public void setUpper(int i) {
        // Warning -- unsafe check-then-act
        if (i < lower.get()) {
            throw new IllegalArgumentException("can't set upper to " + i + " < lower");
        }
        upper.set(i);
    }

    public boolean isInRange(int i) {
        return (i >= lower.get() && i <= upper.get());
    }

    public static void main(String[] args) {
        System.out.println("Welcome to number range crazyness");
        final NumberRange nr = new NumberRange();
        for(int i = 0; i < 3; ++i) {
            (new Thread(new Runnable() {
                    public void run() {
                        while(true) {
                            int high = getRandomInt(1, 10);
                            int low = getRandomInt(0, high - 1);
                            nr.setLower(low);
                            nr.setUpper(high);
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
