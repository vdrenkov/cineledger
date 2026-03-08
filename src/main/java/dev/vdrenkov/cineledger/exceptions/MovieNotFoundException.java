package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

/**
 * Signals that the requested movie could not be found.
 */
public class MovieNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 6570840463533376412L;

    /**
     * Creates the exception with the supplied message.
     *
     * @param message
     *     exception message to propagate
     */
    public MovieNotFoundException(String message) {
        super(message);
    }
}


