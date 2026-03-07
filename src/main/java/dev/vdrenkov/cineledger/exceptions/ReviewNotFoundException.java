package dev.vdrenkov.cineledger.exceptions;

/**
 * Signals that the requested review could not be found.
 */
public class ReviewNotFoundException extends RuntimeException {

    /**
     * Creates the exception with the supplied message.
     *
     * @param message
     *     exception message to propagate
     */
    public ReviewNotFoundException(String message) {
        super(message);
    }
}


