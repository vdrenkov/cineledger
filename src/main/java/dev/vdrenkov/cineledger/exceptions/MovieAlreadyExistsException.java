package dev.vdrenkov.cineledger.exceptions;

/**
 * Signals that the requested movie already exists.
 */
public class MovieAlreadyExistsException extends IllegalArgumentException {

    /**
     * Creates the exception with the supplied message.
     *
     * @param message
     *     exception message to propagate
     */
    public MovieAlreadyExistsException(String message) {
        super(message);
    }
}


