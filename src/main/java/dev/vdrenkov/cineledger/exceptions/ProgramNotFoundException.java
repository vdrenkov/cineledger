package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

/**
 * Signals that the requested program could not be found.
 */
public class ProgramNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -5111116158791848252L;

    /**
     * Creates the exception with the supplied message.
     *
     * @param message
     *     exception message to propagate
     */
    public ProgramNotFoundException(String message) {
        super(message);
    }
}


