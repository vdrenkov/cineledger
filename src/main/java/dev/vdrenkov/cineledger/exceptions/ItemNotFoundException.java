package dev.vdrenkov.cineledger.exceptions;

/**
 * Signals that the requested item could not be found.
 */
public class ItemNotFoundException extends RuntimeException {
    /**
     * Creates the exception with the supplied message.
     *
     * @param message
     *     exception message to propagate
     */
    public ItemNotFoundException(String message) {
        super(message);
    }
}

