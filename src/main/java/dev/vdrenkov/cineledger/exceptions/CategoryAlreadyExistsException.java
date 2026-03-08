package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

/**
 * Signals that the requested category already exists.
 */
public class CategoryAlreadyExistsException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -3516632937559308083L;

    /**
     * Creates the exception with the supplied message.
     *
     * @param message
     *     exception message to propagate
     */
    public CategoryAlreadyExistsException(String message) {
        super(message);
    }
}


