package cp.ex3;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class is now thread safe
 */

public class ThreadSafeNumberRange {
    // INVARIANT: lower <= upper
    private final AtomicInteger lower = new AtomicInteger(0);
    private final AtomicInteger upper = new AtomicInteger(0);

    public synchronized void setLower(int i) {
        // Warning -- unsafe check-then-act
        if (i > upper.get())
            throw new IllegalArgumentException("can't set lower to " + i + " > upper");
        lower.set(i);
    }

    public synchronized void setUpper(int i) {
        // Warning -- unsafe check-then-act
        if (i < lower.get())
            throw new IllegalArgumentException("can't set upper to " + i + " < lower");
        upper.set(i);
    }
    // synchronized is not absolutely required here
    // but it can also happen that the lower/upper values are changed during evaluation...
    public boolean isInRange(int i) {
        return (i >= lower.get() && i <= upper.get());
    }
}
