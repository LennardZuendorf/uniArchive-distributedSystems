package de.berlin.htw.boundary;

import org.jboss.logging.Logger;

import de.berlin.htw.control.GreetingController;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

/**
 * @author Alexander Stanik [stanik@htw-berlin.de]
 */
@Path("/")
public class GreetingResource {
    
    private static Logger LOGGER = Logger.getLogger(GreetingResource.class.getName());
    
    @Context
    UriInfo uri;
    
    @Inject
    GreetingController controller;

    @GET
    @Path("hello")
    @Produces(MediaType.TEXT_PLAIN)
    public Response hello(@QueryParam("name") String name) {
        LOGGER.info("hello was called! url=" + uri.getRequestUri().toASCIIString());
        String greetingMsg = controller.getGreeting(name);
        LOGGER.info("sending greeting: " + greetingMsg);
        return Response.ok(greetingMsg).build();
    }
}