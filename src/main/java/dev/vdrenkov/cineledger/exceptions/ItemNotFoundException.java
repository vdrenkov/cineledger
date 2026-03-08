package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

/**
 * Signals that the requested item could not be found.
 */
public class ItemNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -5738561736569945881L;

    /**
     * Creates the exception with the supplied message.
     *
     * @param message
     *     exception message to propagate
     */
    public ItemNotFoundException(String message) {
        super(message);
    }
}

