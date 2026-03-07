package dev.vdrenkov.cineledger.exceptions;

/**
 * Signals that the requested cinema already exists.
 */
public class CinemaAlreadyExistsException extends RuntimeException {
    /**
     * Creates the exception with the supplied message.
     *
     * @param message
     *     exception message to propagate
     */
    public CinemaAlreadyExistsException(String message) {
        super(message);
    }
}


