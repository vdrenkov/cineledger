package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

/**
 * Signals that the requested cinema already exists.
 */
public class CinemaAlreadyExistsException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 5790359979792968056L;

    /**
     * Creates the exception with the supplied message.
     *
     * @param message
     *     exception message to propagate
     */
    public CinemaAlreadyExistsException(String message) {
        super(message);
    }
}


