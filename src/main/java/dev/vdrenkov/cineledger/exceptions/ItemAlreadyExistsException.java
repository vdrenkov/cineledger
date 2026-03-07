package dev.vdrenkov.cineledger.exceptions;

/**
 * Signals that the requested item already exists.
 */
public class ItemAlreadyExistsException extends IllegalArgumentException {

    /**
     * Creates the exception with the supplied message.
     *
     * @param message
     *     exception message to propagate
     */
    public ItemAlreadyExistsException(String message) {
        super(message);
    }
}


