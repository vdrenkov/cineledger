package dev.vdrenkov.cineledger.exceptions;

/**
 * Signals that the requested category could not be found.
 */
public class CategoryNotFoundException extends RuntimeException {

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


