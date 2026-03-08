package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

/**
 * Signals that the requested review could not be found.
 */
public class ReviewNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -5904097386891683607L;

    /**
     * Creates the exception with the supplied message.
     *
     * @param message
     *     exception message to propagate
     */
    public ReviewNotFoundException(String message) {
        super(message);
    }
}


