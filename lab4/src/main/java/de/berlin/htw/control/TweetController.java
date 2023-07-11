package de.berlin.htw.control;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import de.berlin.htw.boundary.dto.Tweet;
import org.jboss.logging.Logger;

import de.berlin.htw.boundary.TwitterProducer;
import io.quarkus.scheduler.Scheduled;

/**
 * @author Alexander Stanik [stanik@htw-berlin.de]
 */
@Dependent
public class TweetController {

	@Inject
	TweetGenerator generator;
	
	@Inject
	TwitterProducer producer;
	
    public Tweet getTweet() {
        return generator.generateTweet();
    }
    
    @Scheduled(every="10s")     
    void produceTweet() {
        final Tweet tweet = this.getTweet();
        producer.produce(tweet);
    }
}