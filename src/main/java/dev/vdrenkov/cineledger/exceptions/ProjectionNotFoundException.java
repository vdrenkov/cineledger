package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

/**
 * Signals that the requested projection could not be found.
 */
public class ProjectionNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -2573394428928006927L;

    /**
     * Creates the exception with the supplied message.
     *
     * @param message
     *     exception message to propagate
     */
    public ProjectionNotFoundException(String message) {
        super(message);
    }
}


