package dev.vdrenkov.cineledger.exceptions;

/**
 * Signals that the requested cinema could not be found.
 */
public class CinemaNotFoundException extends RuntimeException {

    /**
     * Creates the exception with the supplied message.
     *
     * @param message
     *     exception message to propagate
     */
    public CinemaNotFoundException(String message) {
        super(message);
    }
}

