package de.berlin.htw.control;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import de.berlin.htw.boundary.dto.FibonacciTuple;

/**
 * @author Alexander Stanik [stanik@htw-berlin.de]
 */
@Dependent
public class FibonacciController {
	
	@Inject
	Logger logger;

    public FibonacciTuple calculateNext(final FibonacciTuple current) {
        final FibonacciTuple next = new FibonacciTuple();
        if(isOverflowPossbile(current)) {
            logger.info("Overflow is possible! Resetting fibonacci sequence.");
            next.setLast(1);
            next.setCurrent(1);
        } else {
            next.setLast(current.getCurrent());
            next.setCurrent(calculateNext(current.getLast(), current.getCurrent()));
        }
        // slow down the calculation
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            logger.error("Exception while slow down", e);
        }
        return next;
    }

    private boolean isOverflowPossbile(final FibonacciTuple tuple) {
        return tuple.getCurrent() > (Integer.MAX_VALUE / 2);
    }
    
    private int calculateNext(final int last, final int current) {
        final int next = last + current;
        logger.infov("when fn-1={0}, fn={1} then fn+1={2}",
                String.valueOf(last), String.valueOf(current), String.valueOf(next));
        return next;
    }
}
