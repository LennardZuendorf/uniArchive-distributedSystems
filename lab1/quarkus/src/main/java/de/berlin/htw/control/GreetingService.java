package de.berlin.htw.control;

import java.security.Principal;

import javax.enterprise.context.Dependent;
import javax.ws.rs.NotAuthorizedException;

/**
 * @author Alexander Stanik [stanik@htw-berlin.de]
 */
@Dependent
public class GreetingService {

    public String getGeeting(final Principal userPrincipal) {
        if (userPrincipal == null || userPrincipal.getName() == null) {
            throw new NotAuthorizedException("Authentication is required!");
        } else {
            return "Hello " + userPrincipal.getName() + ", ";
        }
    }
}
