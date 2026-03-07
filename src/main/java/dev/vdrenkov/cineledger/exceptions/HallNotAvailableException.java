package dev.vdrenkov.cineledger.exceptions;

/**
 * Signals that the requested hall is not available for the selected program or projection.
 */
public class HallNotAvailableException extends RuntimeException {
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


