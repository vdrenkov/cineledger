package dev.vdrenkov.cineledger.exceptions;

/**
 * Signals that the requested role could not be found.
 */
public class RoleNotFoundException extends RuntimeException {

    /**
     * Creates the exception with the supplied message.
     *
     * @param message
     *     exception message to propagate
     */
    public RoleNotFoundException(String message) {
        super(message);
    }
}


