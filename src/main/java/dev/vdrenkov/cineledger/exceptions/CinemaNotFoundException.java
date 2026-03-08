package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

/**
 * Signals that the requested cinema could not be found.
 */
public class CinemaNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -1141551388494735118L;

    /**
     * Creates the exception with the supplied message.
     *
     * @param message
     *     exception message to propagate
     */
    public CinemaNotFoundException(String message) {
        super(message);
    }
}

