package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

/**
 * Signals that a requested concession item is no longer available.
 */
public class NoAvailableItemsException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 7448656300640151601L;

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


