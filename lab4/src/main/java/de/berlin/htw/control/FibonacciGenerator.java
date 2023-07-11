package de.berlin.htw.control;

import de.berlin.htw.boundary.FibonacciProducer;
import de.berlin.htw.boundary.dto.FibonacciTuple;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.jboss.logging.Logger;

@ApplicationScoped
public class FibonacciGenerator {

    @Inject
    FibonacciController controller;

    @Inject
    FibonacciProducer producer;

    @Inject
    Logger logger;

    public String initiate(FibonacciTuple tuple) {
        try{
            producer.produce(tuple);
            logger.info("Started infinite fibonacci sequence.");
            return "Started infinite fibonacci sequence.";
        }catch (Exception e){
            logger.error("Exception while producing", e);
            return "Exception while starting infinite fibonacci sequence.";
        }
    }

    public void receive(FibonacciTuple tuple) {
        FibonacciTuple next = controller.calculateNext(tuple);
        producer.produce(next);
    }
}
