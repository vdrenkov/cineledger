package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

/**
 * Signals that the requested ticket could not be found.
 */
public class TicketNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -8020090034668360149L;

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


