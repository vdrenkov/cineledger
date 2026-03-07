package dev.vdrenkov.cineledger.exceptions;

/**
 * Signals that the current request requires an authenticated user.
 */
public class NotLoggedInException extends RuntimeException {

    /**
     * Creates the exception with the supplied message.
     *
     * @param message
     *     exception message to propagate
     */
    public NotLoggedInException(String message) {
        super(message);
    }
}


