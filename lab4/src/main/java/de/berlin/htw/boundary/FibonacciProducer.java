package de.berlin.htw.boundary;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import de.berlin.htw.boundary.dto.FibonacciTuple;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import io.smallrye.reactive.messaging.kafka.Record;
import org.jboss.logging.Logger;

/**
 * @author Alexander Stanik [stanik@htw-berlin.de]
 */

@ApplicationScoped
public class FibonacciProducer {

    @Inject
    @Channel("fibonacci-producer")
    Emitter<Record<String, FibonacciTuple>> producer;

    @Inject
    Logger logger;

    @ConfigProperty(name = "quarkus.profile")
    String profile;
    
    public void produce(FibonacciTuple next){
        Record<String, FibonacciTuple> record = Record.of(profile, next);
        producer.send(record);
    }
}
