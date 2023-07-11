package de.berlin.htw.boundary;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import de.berlin.htw.control.FibonacciGenerator;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import de.berlin.htw.boundary.dto.FibonacciTuple;
import de.berlin.htw.control.FibonacciController;

/**
 * @author Alexander Stanik [stanik@htw-berlin.de]
 */
@Path("/fibonacci")
public class FibonacciResource {

    @ConfigProperty(name = "htw.fibonacci.sequence-size", defaultValue="10")
    Integer sequenceSize;

	@Inject
	FibonacciController controller;

    @Inject
    FibonacciGenerator generator;
	
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<Integer> getFibonacciSequence(FibonacciTuple current) {
    	List<Integer> sequence = new ArrayList<>();
        sequence.add(current.getLast());
        sequence.add(current.getCurrent());
    	for (int i = 2; i < sequenceSize; i++) {
    	    current = controller.calculateNext(current);
    	    sequence.add(current.getCurrent());
        }
        return sequence;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String initiate(@Valid final FibonacciTuple tuple) {
    	return generator.initiate(tuple);
    }

}