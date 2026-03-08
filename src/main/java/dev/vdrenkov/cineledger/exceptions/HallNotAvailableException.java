package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

/**
 * Signals that the requested hall is not available for the selected program or projection.
 */
public class HallNotAvailableException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 5230654874246110433L;

    /**
     * Creates the exception with the supplied message.
     *
     * @param message
     *     exception message to propagate
     */
    public HallNotAvailableException(String message) {
        super(message);
    }

}


