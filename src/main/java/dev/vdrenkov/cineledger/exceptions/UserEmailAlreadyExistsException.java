package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

/**
 * Signals that the requested user email already exists.
 */
public class UserEmailAlreadyExistsException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -1136346203858810779L;

    /**
     * Creates the exception with the supplied message.
     *
     * @param message
     *     exception message to propagate
     */
    public UserEmailAlreadyExistsException(String message) {
        super(message);
    }
}


