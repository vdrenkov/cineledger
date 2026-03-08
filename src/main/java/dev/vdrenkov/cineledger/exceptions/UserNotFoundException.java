package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

/**
 * Signals that the requested user could not be found.
 */
public class UserNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -8961627294884145213L;

    /**
     * Creates the exception with the supplied message.
     *
     * @param message
     *     exception message to propagate
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}

