package de.berlin.htw.control.producer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.jboss.logging.Logger;

/**
 * @author Alexander Stanik [stanik@htw-berlin.de]
 */
@ApplicationScoped
public class LoggerProducer {

    @Produces
    public Logger createLogger(final InjectionPoint ip) {
        return Logger.getLogger(ip.getMember().getDeclaringClass());
    }
}
