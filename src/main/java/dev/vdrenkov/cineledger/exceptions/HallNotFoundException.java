package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

/**
 * Signals that the requested hall could not be found.
 */
public class HallNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -478944443126647862L;

    /**
     * Creates the exception with the supplied message.
     *
     * @param message
     *     exception message to propagate
     */
    public HallNotFoundException(String message) {
        super(message);
    }
}


