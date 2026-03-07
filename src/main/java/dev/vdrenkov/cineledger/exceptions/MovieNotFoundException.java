package dev.vdrenkov.cineledger.exceptions;

/**
 * Signals that the requested movie could not be found.
 */
public class MovieNotFoundException extends RuntimeException {
    /**
     * Creates the exception with the supplied message.
     *
     * @param message
     *     exception message to propagate
     */
    public MovieNotFoundException(String message) {
        super(message);
    }
}


