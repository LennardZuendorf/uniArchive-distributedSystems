package de.berlin.htw.boundary;

import de.berlin.htw.control.OrderController;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;

/**
 * @author Alexander Stanik [stanik@htw-berlin.de]
 */
@Path("/orders")
public class OrderResource {

    @Context
    UriInfo uri;
    
    @Context
    SecurityContext context;
    
    @Inject
    OrderController orderController;

    @Inject
    Logger logger;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Retrieve all completed orders of a users.")
    @APIResponse(responseCode = "200", description = "Retrieved all completed orders successfully")
    @APIResponse(responseCode = "401", description = "No or wrong User Id provided as header")
    public Response getCompletedOrders(
            @HeaderParam("X-User-Id") String userId
    ) {
    	logger.info(context.getUserPrincipal().getName() 
    			+ " is calling " + uri.getAbsolutePath());

        return Response.ok(orderController.getOrders(Integer.valueOf(userId))).build();
    }

}