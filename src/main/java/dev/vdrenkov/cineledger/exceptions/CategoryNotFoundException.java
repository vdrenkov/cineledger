package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

/**
 * Signals that the requested category could not be found.
 */
public class CategoryNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -4243365692979270023L;

    /**
     * Creates the exception with the supplied message.
     *
     * @param message
     *     exception message to propagate
     */
    public CategoryNotFoundException(String message) {
        super(message);
    }
}


