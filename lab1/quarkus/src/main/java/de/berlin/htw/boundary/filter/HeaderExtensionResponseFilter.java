package de.berlin.htw.boundary.filter;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

/**
 * @author Alexander Stanik [stanik@htw-berlin.de]
 */
@Provider
@Priority(Priorities.HEADER_DECORATOR + 1)
public class HeaderExtensionResponseFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext,
            ContainerResponseContext responseContext) throws IOException {
        responseContext.getHeaders().add("X-My-Header", "Filter was executed");
    }

}
