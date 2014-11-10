package cp.ex3;

import java.util.concurrent.atomic.*;

/**
 * NumberRange
 * <p/>
 * Number range class that does not sufficiently protect its invariants
 *
 * @author Brian Goetz and Tim Peierls
 */

public class NumberRange {
    // INVARIANT: lower <= upper
    private final AtomicInteger lower = new AtomicInteger(0);
    private final AtomicInteger upper = new AtomicInteger(0);

    public void setLower(int i) {
    	// check our invariant to prove that class is not thread safe...
    	if(lower.get() > upper.get()) 
    		throw new IllegalStateException("Invariant broken! Lower is now greater than upper!");
    	
        // Warning -- unsafe check-then-act
        if (i > upper.get())
            throw new IllegalArgumentException("can't set lower to " + i + " > upper");
        
        lower.set(i);
    }

    public void setUpper(int i) {
    	// check our invariant to prove that class is not thread safe...
    	if(lower.get() > upper.get()) 
    		throw new IllegalStateException("Invariant broken! Lower is now greater than upper!");
    	
        // Warning -- unsafe check-then-act
        if (i < lower.get())
            throw new IllegalArgumentException("can't set upper to " + i + " < lower");
        
        upper.set(i);
    }

    public boolean isInRange(int i) {
        return (i >= lower.get() && i <= upper.get());
    }
}
