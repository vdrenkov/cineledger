package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

/**
 * Signals that a submitted date violates the application validation rules.
 */
public class DateNotValidException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -5383611895522283042L;

    /**
     * Creates the exception with the supplied message.
     *
     * @param message
     *     exception message to propagate
     */
    public DateNotValidException(String message) {
        super(message);
    }
}


