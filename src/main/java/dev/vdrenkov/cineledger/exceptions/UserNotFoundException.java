package dev.vdrenkov.cineledger.exceptions;

/**
 * Signals that the requested user could not be found.
 */
public class UserNotFoundException extends RuntimeException {
    /**
     * Creates the exception with the supplied message.
     *
     * @param message
     *     exception message to propagate
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}

