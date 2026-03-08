package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

/**
 * Signals that the requested order could not be found.
 */
public class OrderNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -7185545981023502498L;

    /**
     * Creates the exception with the supplied message.
     *
     * @param message
     *     exception message to propagate
     */
    public OrderNotFoundException(String message) {
        super(message);
    }
}


