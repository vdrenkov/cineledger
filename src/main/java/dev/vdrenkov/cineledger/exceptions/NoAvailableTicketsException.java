package dev.vdrenkov.cineledger.exceptions;

/**
 * Signals that no free tickets remain for the requested projection.
 */
public class NoAvailableTicketsException extends RuntimeException {
    /**
     * Creates the exception with the supplied message.
     *
     * @param message
     *     exception message to propagate
     */
    public NoAvailableTicketsException(String message) {
        super(message);
    }
}


