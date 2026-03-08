package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

/**
 * Signals that the requested program already exists.
 */
public class ProgramAlreadyExistsException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -8325731241704512988L;

    /**
     * Creates the exception with the supplied message.
     *
     * @param message
     *     exception message to propagate
     */
    public ProgramAlreadyExistsException(String message) {
        super(message);
    }
}


