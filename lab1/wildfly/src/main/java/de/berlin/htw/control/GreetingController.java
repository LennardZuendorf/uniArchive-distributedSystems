package de.berlin.htw.control;

import jakarta.enterprise.context.Dependent;

/**
 * @author Alexander Stanik [stanik@htw-berlin.de]
 */
@Dependent
public class GreetingController {

    public String getGreeting(final String name) {
        if (name == null || name.isEmpty())
            return "Hello Wildfly";
        else
            return "Hello " + name;
    }

}