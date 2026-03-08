package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

/**
 * Signals that no free tickets remain for the requested projection.
 */
public class NoAvailableTicketsException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -1491924117941771259L;

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


