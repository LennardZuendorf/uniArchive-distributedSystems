package de.berlin.htw.boundary;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseEventSink;

import de.berlin.htw.boundary.dto.FibonacciTuple;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import de.berlin.htw.control.TweetController;

/**
 * @author Alexander Stanik [stanik@htw-berlin.de]
 */
@Path("/tweets")
public class TwitterResource {

    @ConfigProperty(name = "htw.tweet.history-size", defaultValue = "10")
    Integer historySize;

    @Context
    Sse sse;

    @Inject
    TweetController controller;

    @Inject
    TwitterConsumer consumer;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getTweets() {
        List<String> tweets = new ArrayList<>();
        for (int i = 0; i < historySize; i++) {
            tweets.add(controller.getTweet().toString());
        }
        return tweets;
    }

    @GET
    @Path("sse")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void getLiveTweets(@Context SseEventSink sseEventSink) {

        try (SseEventSink sink = sseEventSink) {
            int eventId = 1;
            while (true) {
                String tweet = consumer.get();

                if (tweet != null) {
                    OutboundSseEvent sseEvent = sse.newEventBuilder()
                            .name("myTweets")
                            .id(String.valueOf(eventId))
                            .mediaType(MediaType.APPLICATION_JSON_TYPE)
                            .data(String.class, tweet)
                            .reconnectDelay(3000)
                            .build();
                    sseEventSink.send(sseEvent);
                    eventId++;
                }
            }
        } catch (InterruptedException e) {
            throw new InternalServerErrorException("Exception while waitinmg for new element", e);
        }
    }

}