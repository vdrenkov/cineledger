package dev.vdrenkov.cineledger.exceptions;

/**
 * Signals that a requested concession item is no longer available.
 */
public class NoAvailableItemsException extends RuntimeException {

    /**
     * Creates the exception with the supplied message.
     *
     * @param message
     *     exception message to propagate
     */
    public NoAvailableItemsException(String message) {
        super(message);
    }
}


