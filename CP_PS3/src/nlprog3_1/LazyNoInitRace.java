package nlprog3_1;

public class LazyNoInitRace {
    private ExpensiveObject instance = null;

    public ExpensiveObject getInstance() {
        if (instance == null) {
            synchronized(this) {
                if(instance == null) { //needed to make sure it *still* is not constructed when we get the lock
                    instance = new ExpensiveObject();
                }
            }
        }
        return instance;
    }

    public static void main(String[] args) {
        final LazyNoInitRace lir = new LazyNoInitRace();
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

class ExpensiveObject { 
    private static int objectIDCounter = 0;
    private int objectID;

    public ExpensiveObject() {
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
