package nlprog3_1;

public class LazyInitRace {
    private ExpensiveObject1 instance = null;

    public ExpensiveObject1 getInstance() {
        if (instance == null) {
            instance = new ExpensiveObject1();
        }
        return instance;
    }

    public static void main(String[] args) {
        final LazyInitRace lir = new LazyInitRace();
        Thread[] tarr = new Thread[3];
        for(int i = 0; i < 3; ++i) {
            tarr[i] = new Thread(
                new Runnable() {
                    public void run() {
                        System.out.println("Thread " + Thread.currentThread().getId() + " got: " + lir.getInstance().toString());
                    }
                });
            tarr[i].start();
        }
     }

}

class ExpensiveObject1 { 
    private static int objectIDCounter = 0;
    private int objectID;

    public ExpensiveObject1() {
        objectID = objectIDCounter;
        ++objectIDCounter;
        try {
            Thread.sleep(500);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String toString() {
        return "ExpensiveObject" + objectID;
    }
}