package dev.vdrenkov.cineledger.exceptions;

/**
 * Signals that the requested ticket could not be found.
 */
public class TicketNotFoundException extends RuntimeException {

    /**
     * Creates the exception with the supplied message.
     *
     * @param message
     *     exception message to propagate
     */
    public TicketNotFoundException(String message) {
        super(message);
    }
}


