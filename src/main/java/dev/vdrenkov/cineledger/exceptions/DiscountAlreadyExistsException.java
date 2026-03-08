package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

/**
 * Signals that the requested discount already exists.
 */
public class DiscountAlreadyExistsException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -2768977154786826477L;

    /**
     * Creates the exception with the supplied message.
     *
     * @param message
     *     exception message to propagate
     */
    public DiscountAlreadyExistsException(String message) {
        super(message);
    }
}


