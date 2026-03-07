package dev.vdrenkov.cineledger.exceptions;

/**
 * Signals that the requested projection could not be found.
 */
public class ProjectionNotFoundException extends RuntimeException {

    /**
     * Creates the exception with the supplied message.
     *
     * @param message
     *     exception message to propagate
     */
    public ProjectionNotFoundException(String message) {
        super(message);
    }
}


