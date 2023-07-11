package de.berlin.htw.boundary;

import io.smallrye.reactive.messaging.kafka.Record;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import de.berlin.htw.boundary.dto.Tweet;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import org.jboss.logging.Logger;

/**
 * @author Alexander Stanik [stanik@htw-berlin.de]
 */
@ApplicationScoped
public class TwitterProducer {

    @Inject
    @Channel("twitter-producer")
    Emitter<Tweet> producer;

    @Inject
    Logger logger;

    public void produce(final Tweet tweet) {
        producer.send(tweet);
    }
}
