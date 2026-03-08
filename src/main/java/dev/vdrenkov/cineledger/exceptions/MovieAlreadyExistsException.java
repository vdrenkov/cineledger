package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

/**
 * Signals that the requested movie already exists.
 */
public class MovieAlreadyExistsException extends IllegalArgumentException {
    @Serial
    private static final long serialVersionUID = -7185141585628484776L;

    /**
     * Creates the exception with the supplied message.
     *
     * @param message
     *     exception message to propagate
     */
    public MovieAlreadyExistsException(String message) {
        super(message);
    }
}


