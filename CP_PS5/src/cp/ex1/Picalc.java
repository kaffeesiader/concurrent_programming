package cp.ex1;

import java.util.concurrent.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;

class Picalc {
    private final FutureTask<BigDecimal> future = 
        new FutureTask<BigDecimal>(new Callable<BigDecimal>() {
                public BigDecimal call() {
                    return calculatePartialPiSum();
                }
            });
    private final Thread thread = new Thread(future);
    private static final int accuracy = 15;
    private int numberBlocks;
    private int blockStart;
    private static final BigDecimal PI = new BigDecimal("3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679821480865132823066470938446095505822317253594081284811174502841027019385211055596446229489549303819644288109756659334461284756482337867831652712019091456485669234603");

    public Picalc(int blockStart, int numberBlocks) {
        if(blockStart <= 0) {
            throw new IllegalArgumentException("blockStart needs to be 1 or higher");
        }
        if(numberBlocks < 0) {
            throw new IllegalArgumentException("numberBlocks must be positive");
        }
        this.numberBlocks = numberBlocks;
        this.blockStart   = blockStart;
    }

    public void start() {
        thread.start();
    }

    BigDecimal calculatePartialPiSum() {
        BigDecimal toAdd = BigDecimal.ZERO;
        for(int i = blockStart; i < blockStart + numberBlocks; ++i) {
            toAdd = toAdd.add(     BigDecimal.valueOf(1).divide(BigDecimal.valueOf(2*2*i - 3), accuracy, RoundingMode.HALF_UP));
            toAdd = toAdd.subtract(BigDecimal.valueOf(1).divide(BigDecimal.valueOf(2*2*i - 1), accuracy, RoundingMode.HALF_UP));
        }
        return toAdd;
    }

    public BigDecimal get() {
        try {
            return future.get();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return new BigDecimal(0);
    }
    

    public static void main(String[] args) {
        System.out.println("Welcome to PI Calculator Land");
        int numberCalculators = 8;
        final int numberOfBlocks = 5120 * 2048;

        if(args.length > 0) {
            numberCalculators = Integer.parseInt(args[0]);
        } else {
            System.out.println("If you wish, you may pass the number of calculators as parameter");
        }
        System.out.println("Running with " + numberCalculators + " calculators");
        int blocksPerTask = numberOfBlocks / numberCalculators;
        List<Picalc> futuresList = new LinkedList<Picalc>();
        for(int i = 1; i <= numberOfBlocks; i += blocksPerTask) {
            Picalc p = new Picalc(i, blocksPerTask);
            p.start();
            futuresList.add(p);
        }
        BigDecimal result = BigDecimal.ZERO;
        for(Picalc p : futuresList) {
            result = result.add(p.get());
        }
        result = result.multiply(BigDecimal.valueOf(4));
		System.out.println("Result:   " + result);
		System.out.println("PI:       " + PI.toPlainString().substring(0, accuracy + 2));
		System.out.println("Error:    " + result.subtract(PI).abs().toPlainString().substring(0, accuracy + 2));

        System.out.println("\n\nTime for benchmarks");
        int[] testCalculators = { 1, 2, 4, 80, 160, 320, 1024 };
    

        for(int tcnum : testCalculators) {
            futuresList.clear();
            blocksPerTask = numberOfBlocks / tcnum;
            result = BigDecimal.ZERO;
            long startTime = System.nanoTime();
            for(int i = 1; i <= numberOfBlocks; i += blocksPerTask) {
                Picalc p = new Picalc(i, blocksPerTask);
                futuresList.add(p);
            }
            long startTimeWithoutInit = System.nanoTime();
            for(Picalc p : futuresList) {
                p.start();
            }
            for(Picalc p : futuresList) {
                result = result.add(p.get());
            }
            result = result.multiply(BigDecimal.valueOf(4));
            long endTime = System.nanoTime();
            System.out.printf("%5d calculators with    initialisiation: %4d ms\n", tcnum,  (endTime-startTime)            / 1000000);
            System.out.printf("%5d calculators without initialisiation: %4d ms\n\n", tcnum,  (endTime-startTimeWithoutInit) / 1000000);
        }

    }
}
