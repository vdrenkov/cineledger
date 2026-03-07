package dev.vdrenkov.cineledger.exceptions;

/**
 * Signals that the requested hall could not be found.
 */
public class HallNotFoundException extends RuntimeException {
    /**
     * Creates the exception with the supplied message.
     *
     * @param message
     *     exception message to propagate
     */
    public HallNotFoundException(String message) {
        super(message);
    }
}


