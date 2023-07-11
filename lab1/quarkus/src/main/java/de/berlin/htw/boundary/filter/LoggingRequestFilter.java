package de.berlin.htw.boundary.filter;

import java.io.IOException;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;

import org.jboss.logging.Logger;

/**
 * @author Alexander Stanik [stanik@htw-berlin.de]
 */
@Provider
@PreMatching
public class LoggingRequestFilter implements ContainerRequestFilter {
    
    @Inject
    Logger logger;

    @Override
    public void filter(ContainerRequestContext requestContext)
            throws IOException {
        logger.info("Request Method = " + requestContext.getMethod());
    }

}
