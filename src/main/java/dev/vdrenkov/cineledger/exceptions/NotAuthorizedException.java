package dev.vdrenkov.cineledger.exceptions;

/**
 * Signals that the current user is not allowed to perform the requested action.
 */
public class NotAuthorizedException extends RuntimeException {
    /**
     * Creates the exception with the supplied message.
     *
     * @param message
     *     exception message to propagate
     */
    public NotAuthorizedException(String message) {
        super(message);
    }
}


