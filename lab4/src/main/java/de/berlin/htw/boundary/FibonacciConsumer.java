package de.berlin.htw.boundary;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import de.berlin.htw.boundary.dto.FibonacciTuple;
import de.berlin.htw.control.FibonacciGenerator;
import io.smallrye.reactive.messaging.kafka.Record;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

/**
 * @author Alexander Stanik [stanik@htw-berlin.de]
 */

@ApplicationScoped
public class FibonacciConsumer {

    @Inject
    Logger logger;

    @Inject
    FibonacciGenerator generator;

    @ConfigProperty(name = "quarkus.profile")
    String profile;

    @Incoming("fibonacci-consumer")
    public void consume(Record<String, FibonacciTuple> record) {
        generator.receive(record.value());
    }
}
