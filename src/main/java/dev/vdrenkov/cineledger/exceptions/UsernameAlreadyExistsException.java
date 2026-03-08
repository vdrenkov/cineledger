package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

/**
 * Signals that the requested username already exists.
 */
public class UsernameAlreadyExistsException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -6239758079714036337L;

    /**
     * Creates the exception with the supplied message.
     *
     * @param message
     *     exception message to propagate
     */
    public UsernameAlreadyExistsException(String message) {
        super(message);
    }
}


