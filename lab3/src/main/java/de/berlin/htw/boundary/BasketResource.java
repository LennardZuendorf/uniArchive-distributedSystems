package de.berlin.htw.boundary;

import de.berlin.htw.boundary.dto.Basket;
import de.berlin.htw.boundary.dto.Item;
import de.berlin.htw.boundary.dto.Order;
import de.berlin.htw.control.BasketController;
import de.berlin.htw.control.exception.NotEnoughFundsException;
import de.berlin.htw.control.exception.ProductAlreadyExistException;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;
import java.net.URI;

/**
 * @author Alexander Stanik [stanik@htw-berlin.de]
 */
@Path("/basket")
public class BasketResource {

    @Context
    UriInfo uri;

    @Context
    SecurityContext context;

    @Inject
    BasketController basket;

    @Inject
    Logger logger;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Retrieve the basket with all items.")
    @APIResponse(responseCode = "200", description = "Retrieved all items in basket successfully")
    @APIResponse(responseCode = "401", description = "No or wrong User Id provided as header")
    @APIResponse(responseCode = "404", description = "No basket for this user")
    public Basket getBasket(@HeaderParam("X-User-Id") String userId) {
    	logger.info(context.getUserPrincipal().getName()
    			+ " is calling " + uri.getAbsolutePath());
        return basket.getBasket(userId);
    }

    @DELETE
    @Operation(summary = "Remove all items from basket.")
    @APIResponse(responseCode = "204", description = "Items removed successfully")
    @APIResponse(responseCode = "401", description = "No or wrong User Id provided as header")
    public Response clearBasket(@HeaderParam("X-User-Id") String userId) {
    	logger.info(context.getUserPrincipal().getName()
    			+ " is calling " + uri.getAbsolutePath());
        basket.clearBasket(userId);
    	return Response.noContent().build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Checkout the basket and complete the order.")
    @APIResponse(responseCode = "201", description = "Checkout successfully",
            headers = @Header(name = "Location", description = "URL to retrive all orders"),
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Order.class)) )
    @APIResponse(responseCode = "401", description = "No or wrong User Id provided as header")
    @APIResponse(responseCode = "404", description = "No product with this ID in the basket")
    @APIResponse(responseCode = "402", description = "Not enough funds to checkout the order")
    public Response checkout(@HeaderParam("X-User-Id") String userId) {
    	logger.info(context.getUserPrincipal().getName()
    			+ " is calling " + uri.getAbsolutePath());
        try{
            return Response.created(URI.create(uri.getBaseUri()+"orders/"+basket.checkout(userId))).build();
        } catch (NotEnoughFundsException e) {
            return Response.status(Status.PAYMENT_REQUIRED).build();
        }

    }

    @POST
    @Path("{productId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Add an item to basket.")
    @APIResponse(responseCode = "201", description = "Item added successfully",
        content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Basket.class)) )
    @APIResponse(responseCode = "400", description = "Invalid request message")
    @APIResponse(responseCode = "401", description = "No or wrong User Id provided as header")
    @APIResponse(responseCode = "409", description = "Another product with this ID already exist in the basket")
    @APIResponse(responseCode = "402", description = "Not enough funds to add the item")
    public Response addItem(
            @HeaderParam("X-User-Id") String userId,
            @Parameter(description = "ID of the product", required = true) @PathParam("productId") final String productId,
            @Parameter(description = "The item to add in the basket", required = true)@Valid final Item item
    ){
        logger.info(context.getUserPrincipal().getName()
    			+ " is calling " + uri.getAbsolutePath());

        if(productId.equals(item.getProductId())){
            try {
                basket.addItem(item, userId);
                final URI location = uri.getAbsolutePath();
                return Response.created(location).build();
            } catch (NotEnoughFundsException e) {
                return Response.status(Status.PAYMENT_REQUIRED).build();
            } catch (ProductAlreadyExistException e) {
                return Response.status(Status.CONFLICT).build();
            }

        }else return Response.status(Status.BAD_REQUEST).build();
    }

    @DELETE
    @Path("{productId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Remove an item from basket.")
    @APIResponse(responseCode = "200", description = "Item removed successfully",
        content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Basket.class)) )
    @APIResponse(responseCode = "401", description = "No or wrong User Id provided as header")
    @APIResponse(responseCode = "404", description = "No product with this ID in the basket")
    public Response removeItem(
            @HeaderParam("X-User-Id") String userId,
            @Parameter(description = "ID of the product", required = true) @PathParam("productId") final String productId
    ){
    	logger.info(context.getUserPrincipal().getName()
    			+ " is calling " + uri.getAbsolutePath());

        basket.removeItem(productId, userId);
        return Response.ok().build();
    }

    @PATCH
    @Path("{productId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Change the number of an item in the basket.")
    @APIResponse(responseCode = "200", description = "Number changed successfully",
        content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Basket.class)) )
    @APIResponse(responseCode = "400", description = "Invalid request message")
    @APIResponse(responseCode = "401", description = "No or wrong User Id provided as header")
    @APIResponse(responseCode = "404", description = "No product with this ID in the basket")
    @APIResponse(responseCode = "402", description = "Not enough funds to change the number of items")
    public Response changeCount(
            @HeaderParam("X-User-Id") String userId,
            @Parameter(description = "ID of the product", required = true) @PathParam("productId") final String productId,
            @Parameter(description = "The number of that product in the basket", required = true) final Item item
    ){
        logger.info(context.getUserPrincipal().getName()
    			+ " is calling " + uri.getAbsolutePath());

        if (productId.equals(item.getProductId())) {
            try{
                basket.updateItem(item, userId);
                return Response.ok().build();
            }catch (NotEnoughFundsException e){
                return Response.status(Status.PAYMENT_REQUIRED).build();
            }
        }else{
            return Response.status(Status.BAD_REQUEST).build();
        }
    }

}
