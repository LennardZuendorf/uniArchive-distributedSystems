package de.berlin.htw.boundary;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import de.berlin.htw.boundary.dto.Orders;
import de.berlin.htw.control.OrderService;

/**
 * @author Alexander Stanik [stanik@htw-berlin.de]
 */
@Path("/users/{id}")
public class UserResource {

    @Inject
    OrderService service;

    @GET
    @Path("/orders")
    @Produces("application/json")
    public Orders filterOrdersByName(
            @PathParam("id") String userId, 
            @QueryParam("name") String name) {
        
        return service.getOrdersByName(userId, name);
    }

}