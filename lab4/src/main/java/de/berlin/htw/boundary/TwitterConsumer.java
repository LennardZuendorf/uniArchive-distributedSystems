package de.berlin.htw.boundary;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import de.berlin.htw.boundary.dto.Tweet;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

/**
 * @author Alexander Stanik [stanik@htw-berlin.de]
 */
@ApplicationScoped
public class TwitterConsumer {

    @Inject
    Logger logger;

    BlockingQueue<Tweet> queue = new LinkedBlockingQueue<>(50);

    @Incoming("twitter-consumer")
    public void onEvent(Tweet tweet) {
        try {
            queue.put(tweet);
        } catch (InterruptedException e) {
            logger.error("Error while putting tweet into queue: " + e.getMessage());
        }
    }
    
    public String get() throws InterruptedException {
        return queue.take().toString();
    }
}
