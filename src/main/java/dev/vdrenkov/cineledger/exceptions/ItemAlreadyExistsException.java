package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

/**
 * Signals that the requested item already exists.
 */
public class ItemAlreadyExistsException extends IllegalArgumentException {
    @Serial
    private static final long serialVersionUID = -7133192092794659772L;

    /**
     * Creates the exception with the supplied message.
     *
     * @param message
     *     exception message to propagate
     */
    public ItemAlreadyExistsException(String message) {
        super(message);
    }
}


