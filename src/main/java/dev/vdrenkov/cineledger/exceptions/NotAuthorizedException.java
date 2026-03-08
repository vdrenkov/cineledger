package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

/**
 * Signals that the current user is not allowed to perform the requested action.
 */
public class NotAuthorizedException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -4660620627455830124L;

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


