package dev.vdrenkov.cineledger.exceptions;

/**
 * Signals that the requested category already exists.
 */
public class CategoryAlreadyExistsException extends RuntimeException {
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


