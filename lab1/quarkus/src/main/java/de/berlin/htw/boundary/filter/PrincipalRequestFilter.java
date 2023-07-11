package de.berlin.htw.boundary.filter;

import java.io.IOException;
import java.security.Principal;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

/**
 * @author Alexander Stanik [stanik@htw-berlin.de]
 */
@Provider
@Priority(Priorities.AUTHENTICATION)
public class PrincipalRequestFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext)
        throws IOException {
        final String userName = requestContext.getHeaderString("X-User-Name");
        final String userRole = requestContext.getHeaderString("X-User-Role");
        if (userName != null || userRole != null) {
            SecurityContext securityContext = requestContext.getSecurityContext();
            securityContext = extendSecurityContext(securityContext, userName, userRole);
            requestContext.setSecurityContext(securityContext);
        }
    }

    private SecurityContext extendSecurityContext(final SecurityContext securityContext,
        final String userName, final String userRole) {
        return new SecurityContext() {
            @Override
            public Principal getUserPrincipal() {
                return new Principal() {
                    @Override
                    public String getName() {
                        return userName;
                    }
                };
            }

            @Override
            public boolean isUserInRole(String role) {
                return userRole != null && userRole.equals(role);
            }

            @Override
            public boolean isSecure() {
                return securityContext.isSecure();
            }

            @Override
            public String getAuthenticationScheme() {
                return "SpezialUserHeader";
            }
        };
    }

}
